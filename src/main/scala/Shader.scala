import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._


class Shader(vertName: String, fragName: String) {

    def this(name: String) = this(name, name)

    val programId: Int = ShaderUtils.generateProgramId

    val vertShaderId: Int = ShaderUtils.attachVertShader(programId, vertName)

    val fragShaderId: Int = ShaderUtils.attachFragShader(programId, fragName)

    glLinkProgram(programId)

    if (glGetProgrami(programId, GL_LINK_STATUS) == GL_FALSE)
        throw new RuntimeException(s"Unable to link shader program:\n${glGetProgramInfoLog(programId, glGetProgrami(programId, GL_INFO_LOG_LENGTH))}")

    def use(): Unit = ShaderUtils.useShader(programId)

    def destroy(): Unit = {
        glUseProgram(0)

        glDetachShader(programId, vertShaderId)
        glDetachShader(programId, fragShaderId)

        glDeleteShader(vertShaderId)
        glDeleteShader(fragShaderId)

        glDeleteProgram(programId)
    }

    def getUniformLocation(uniformName: String): Int = glGetUniformLocation(programId, uniformName)

}
