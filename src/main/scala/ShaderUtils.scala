import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

import scala.io.Source

object ShaderUtils {

    var currentShader: Int = 0

    def readFromFile(name: String): String = Source.fromInputStream(getClass.getResourceAsStream(name)).mkString

    def generateProgramId: Int = glCreateProgram()

    private def attachShader(programId: Int, shaderPath: String, shaderConstant: Int): Int = {
        val shaderSource: String = readFromFile(shaderPath)

        val shaderId: Int = glCreateShader(shaderConstant)

        glShaderSource(shaderId, shaderSource)

        glCompileShader(shaderId)

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException(s"Error creating vertex shader:\n${glGetShaderInfoLog(shaderId, glGetShaderi(shaderId, GL_INFO_LOG_LENGTH))}")

        glAttachShader(programId, shaderId)

        shaderId
    }

    def attachVertShader(programId: Int, name: String): Int = attachShader(programId, s"shaders/$name.vert", GL_VERTEX_SHADER)

    def attachFragShader(programId: Int, name: String): Int = attachShader(programId, s"shaders/$name.frag", GL_FRAGMENT_SHADER)

    def useShader(programId: Int): Unit =
        if (currentShader != programId) {
            currentShader = programId
            glUseProgram(programId)
        }

}
