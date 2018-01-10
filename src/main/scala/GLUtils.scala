import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL30._

object GLUtils {

    def glErrorToString(glError: Int): String = glError match {
        case GL_NO_ERROR => "No Error"
        case GL_INVALID_ENUM => "Invalid Enum"
        case GL_INVALID_VALUE => "Invalid Value"
        case GL_INVALID_OPERATION => "Invalid Operation"
        case GL_INVALID_FRAMEBUFFER_OPERATION => "Invalid Framebuffer Operation"
        case GL_OUT_OF_MEMORY => "GL Out of Memory"
        case e: Int => s"Unknown Error ($e)"
    }

    def checkErrors(): Unit = {
        val glError: Int = glGetError()

        if (glError != GL_NO_ERROR)
            System.err.println(s"OpenGL Error: ${GLUtils.glErrorToString(glError)}")
    }

    def resize(windowId: Long, width: Int, height: Int): Unit = {

    }

}
