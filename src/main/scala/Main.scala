import java.nio.IntBuffer

import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWVidMode}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.{GL, GLCapabilities}
import org.lwjgl.system.{MemoryStack, MemoryUtil}


object Main {

    var windowId: Long = _

    var basicShader: Shader = _

    var cubeShader: Shader = _

    var cubeModel: Model = _

    var monkeyModel: Model = _

    var floorShader: Shader = _

    var floorModel: Model = _

    var HEIGHT: Int = 720

    var ASPECT_RATIO: Float = 16.0f / 9.0f

    var WIDTH: Int = (HEIGHT * ASPECT_RATIO).toInt

    var GLCapabilities: GLCapabilities = _

    var screenShader: Shader = _

    var screenModel: Model = _

    val modelRotValue: Float = Math.toRadians(1.0).toFloat

    var disableColor: Int = 0

    def main(args: Array[String]): Unit = {
        try {
            println("Hello, World!")
            init()
            loop()
        } finally {
            destroy()
        }
    }

    def init(): Unit = {
        initGLFW()
        initGL()
        initGLData()
    }

    def initGLFW(): Unit = {
        GLFWErrorCallback.createPrint(System.err).set()

        if (!glfwInit())
            throw new IllegalStateException("Unable to init GLFW")

        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        windowId = glfwCreateWindow(WIDTH, HEIGHT, "Edge Detection", MemoryUtil.NULL, MemoryUtil.NULL)

        if (windowId == MemoryUtil.NULL)
            throw new RuntimeException("Failed to create GLFW window")

        glfwSetKeyCallback(windowId, InputHandler.handleKeypress)

        val stack: MemoryStack = MemoryStack.stackPush()
        val pWidth: IntBuffer = stack.mallocInt(1)
        val pHeight: IntBuffer = stack.mallocInt(1)

        glfwGetWindowSize(windowId, pWidth, pHeight)

        val vidmode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

        glfwSetMouseButtonCallback(windowId, InputHandler.handleMouseButton)
        glfwSetCursorPosCallback(windowId, InputHandler.handleCursorMove)
        glfwSetCursorEnterCallback(windowId, InputHandler.handleCursorEnter)


        glfwSetWindowPos(
            windowId,
            (vidmode.width() - pWidth.get(0)) / 2,
            (vidmode.height() - pHeight.get(0)) / 2
        )

        glfwMakeContextCurrent(windowId)

        glfwSwapInterval(1)

        glfwShowWindow(windowId)
    }

    def initGL(): Unit = {
        GLCapabilities = GL.createCapabilities()

        glClearColor(0.4f, 0.6f, 0.8f, 1.0f)


        glEnable(GL_DEPTH_TEST)
        glDepthFunc(GL_LEQUAL)

        glfwSetWindowSizeCallback(windowId, (windowId: Long, width: Int, height: Int) => {
            WIDTH = width
            HEIGHT = height
            ASPECT_RATIO = width.toFloat / height.toFloat
            Camera.onResizeHandler(windowId, width, height)
            Screen.onResizeHandler(windowId, width, height)
        })

        if (!GLCapabilities.GL_EXT_framebuffer_object) {
            System.err.println("Frame Buffer Objects not supported!")
            System.exit(0)
        }
    }

    def initGLData(): Unit = {
        basicShader = new Shader("basic")

        cubeShader = new Shader("basic", "cube")

        cubeModel = OBJLoader.loadObjModel("models/cube.obj")

        monkeyModel = OBJLoader.loadObjModel("models/monkey.obj")

        floorShader = new Shader("basic", "floor")

        floorModel = OBJLoader.loadObjModel("models/floor.obj")

        cubeModel.modelMatrix.translate(0.0f, 1.0f, 0.0f)

        monkeyModel.modelMatrix.translate(4.0f, 1.0f, 0.0f)

        screenShader = new Shader("screen")

        screenModel = new Model(Model.screenVerts)

        Screen.init()
    }

    def loop(): Unit = {
        while (!glfwWindowShouldClose(windowId)) {

            tick()

            glfwSwapBuffers(windowId)

            glfwPollEvents()
        }
    }

    def tick(): Unit = {
        InputHandler.tick()
        render()
        postProcessing()
        GLUtils.checkErrors()
    }

    def render(): Unit = {

        Screen.bindBaseFrameBuffer()


        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        cubeShader.use()

        cubeModel.modelMatrix
            //            .rotateLocalX(modelRotValue)
            .rotateY(modelRotValue)
            .rotateZ(modelRotValue)

        Camera.uploadAll(cubeModel.modelMatrix)

        cubeModel.draw()

        basicShader.use()

        Camera.uploadAll(monkeyModel.modelMatrix)

        monkeyModel.draw()

        floorShader.use()

        Camera.uploadAll(floorModel.modelMatrix)

        floorModel.draw()
    }

    def postProcessing(): Unit = {
        Screen.bindScreenFrameBuffer()

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

        screenShader.use()

        Screen.setResolutionUniform()

        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, Screen.fboTexture)
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, Screen.fboNormal)
        glUniform1i(screenShader.getUniformLocation("fbo_texture"), 0)
        glUniform1i(screenShader.getUniformLocation("fbo_normal"), 1)

        glUniform1i(screenShader.getUniformLocation("disable_color"), disableColor)

        screenModel.draw()
    }

    def destroy(): Unit = {
        if (basicShader != null)
            basicShader.destroy()

        if (cubeShader != null)
            cubeShader.destroy()

        if (cubeModel != null)
            cubeModel.destroy()

        if (monkeyModel != null)
            monkeyModel.destroy()

        if (floorShader != null)
            floorShader.destroy()

        if (floorModel != null)
            floorModel.destroy()

        if (screenShader != null)
            screenShader.destroy()

        if (Screen.isInitalised)
            Screen.destroy()

        glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL)

        glfwTerminate()
        glfwSetErrorCallback(null).free()
    }

}
