import java.nio.FloatBuffer

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._


class Model(verts: Array[Float]) {

    val modelMatrix: Matrix4f = new Matrix4f().identity()
    
    val floatBuffer: FloatBuffer = BufferUtils.createFloatBuffer(verts.length)
    floatBuffer.put(verts).flip()

    val vaoId: Int = glGenVertexArrays()
    glBindVertexArray(vaoId)

    val vboId: Int = glGenBuffers()
    glBindBuffer(GL_ARRAY_BUFFER, vboId)
    glBufferData(GL_ARRAY_BUFFER, floatBuffer, GL_STATIC_DRAW)
    glEnableVertexAttribArray(0)

    glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * 4, 0)
    glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * 4, 3 * 4)

    glDisableVertexAttribArray(0)

    def draw(): Unit = {
        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawArrays(GL_TRIANGLES, 0, verts.length / 3)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
    }

    def destroy(): Unit = {
        glBindVertexArray(0)
        glDeleteVertexArrays(vaoId)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
        glDeleteBuffers(vboId)
    }

}

object Model {
    val cubeVerts: Array[Float] = Array[Float](
        -1.0f,-1.0f,-1.0f, // triangle 1 : begin
        -1.0f,-1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f, // triangle 1 : end
        1.0f, 1.0f,-1.0f, // triangle 2 : begin
        -1.0f,-1.0f,-1.0f,
        -1.0f, 1.0f,-1.0f, // triangle 2 : end
        1.0f,-1.0f, 1.0f,
        -1.0f,-1.0f,-1.0f,
        1.0f,-1.0f,-1.0f,
        1.0f, 1.0f,-1.0f,
        1.0f,-1.0f,-1.0f,
        -1.0f,-1.0f,-1.0f,
        -1.0f,-1.0f,-1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f,-1.0f,
        1.0f,-1.0f, 1.0f,
        -1.0f,-1.0f, 1.0f,
        -1.0f,-1.0f,-1.0f,
        -1.0f, 1.0f, 1.0f,
        -1.0f,-1.0f, 1.0f,
        1.0f,-1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f,-1.0f,-1.0f,
        1.0f, 1.0f,-1.0f,
        1.0f,-1.0f,-1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f,-1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        1.0f, 1.0f,-1.0f,
        -1.0f, 1.0f,-1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f,-1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
        -1.0f, 1.0f, 1.0f,
        1.0f,-1.0f, 1.0f
    )
    
    val floorVerts: Array[Float] = Array[Float](
        -10.0f, 0.0f, -10.0f,
        -10.0f, 0.0f,  10.0f,
         10.0f, 0.0f,  10.0f,
        -10.0f, 0.0f, -10.0f,
         10.0f, 0.0f,  10.0f,
         10.0f, 0.0f, -10.0f
    )

    val screenVerts: Array[Float] = Array[Float](
        -1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
         1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
        -1.0f,  1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
        -1.0f,  1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
         1.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
         1.0f,  1.0f, 0.0f, 0.0f, 0.0f, -1.0f
    )
}