import java.nio.DoubleBuffer

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW._
import org.lwjgl.system.MemoryStack

object InputHandler {

    private val keys: Array[Boolean] = new Array(35565)

    private var lastCursorPos: (Double, Double) = (0.0, 0.0)

    private var cursorEntered: Boolean = false

    private var isCaptured: Boolean = false

    def handle(key: Int, action: Int): Unit = {
        keys(key) = action != GLFW.GLFW_RELEASE
    }

    def isDown(key: Int): Boolean = keys(key)

    def handleKeypress(windowId: Long, key: Int, scancode: Int, action: Int, mods: Int): Unit = {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
            isCaptured = false
        } else if (key == GLFW_KEY_R && action == GLFW_RELEASE) {
            Camera.location.set(0.0f, 0.0f, 0.0f)
            Camera.setRotation(0.0f, 0.0f, 0.0f)
        } else if (key == GLFW_KEY_G && action == GLFW_RELEASE)
            Camera.setRotation(Math.PI.toFloat, 0.0f, 0.0f)
        else if (key == GLFW_KEY_C && action == GLFW_RELEASE)
            Main.disableColor = if (Main.disableColor == 0) 1 else 0


        handle(key, action)
    }

    def tick(): Unit = {
        if (InputHandler.isDown(GLFW_KEY_W))
            Camera.translateWithRot(0.0f, 0.0f, ControlRef.MOVE_SPEED)
        if (InputHandler.isDown(GLFW_KEY_A))
            Camera.translateWithRot(ControlRef.MOVE_SPEED, 0.0f, 0.0f)
        if (InputHandler.isDown(GLFW_KEY_S))
            Camera.translateWithRot(0.0f, 0.0f, -ControlRef.MOVE_SPEED)
        if (InputHandler.isDown(GLFW_KEY_D))
            Camera.translateWithRot(-ControlRef.MOVE_SPEED, 0.0f, 0.0f)

        if (InputHandler.isDown(GLFW_KEY_SPACE))
            Camera.translate(0.0f, -ControlRef.MOVE_SPEED, 0.0f)
        if (InputHandler.isDown(GLFW_KEY_LEFT_SHIFT))
            Camera.translate(0.0f, ControlRef.MOVE_SPEED, 0.0f)
    }

    def handleMouseButton(windowId: Long, button: Int, action: Int, mods: Int): Unit = {
        if (cursorEntered && button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
            glfwSetInputMode(windowId, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
            val stack: MemoryStack = MemoryStack.stackPush()
            val cXPos: DoubleBuffer = stack.mallocDouble(1)
            val cYPos: DoubleBuffer = stack.mallocDouble(1)

            glfwGetCursorPos(windowId, cXPos, cYPos)
            lastCursorPos = (cXPos.get(0), cYPos.get(0))
            isCaptured = true
        }
    }

    def handleCursorEnter(windowId: Long, entered: Boolean): Unit = cursorEntered = entered

    def handleCursorMove(windowId: Long, xPos: Double, yPos: Double): Unit = {
        if (isCaptured) {
            val (deltaX, deltaY): (Double, Double) = (lastCursorPos._1 - xPos, lastCursorPos._2 - yPos)

            Camera.rotate(
                -Math.toRadians(deltaY).toFloat * ControlRef.ROT_SPEED,
                -Math.toRadians(deltaX).toFloat * ControlRef.ROT_SPEED,
                0.0f
            )

            lastCursorPos = (xPos, yPos)
        }
    }

}
