import java.nio.FloatBuffer

import Main.ASPECT_RATIO
import org.joml.{AxisAngle4f, Matrix4f, Vector3f}
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

object Camera {

    var perspectiveMatrix: Matrix4f = new Matrix4f().perspective(Math.toRadians(45.0f).toFloat, ASPECT_RATIO, 0.01f, 100.0f)

    val location: Vector3f = new Vector3f(0.0f, 1.0f, 5.0f)

    private val rotationX: AxisAngle4f = new AxisAngle4f(0.0f, 1.0f, 0.0f, 0.0f)
    private val rotationY: AxisAngle4f = new AxisAngle4f(0.0f, 0.0f, 1.0f, 0.0f)
    private val rotationZ: AxisAngle4f = new AxisAngle4f(0.0f, 0.0f, 0.0f, 1.0f)

    def translate(x: Float, y: Float, z: Float): Unit = location.add(x, y, z)

    def translateWithRot(x: Float, y: Float, z:Float): Unit = {
        val angle: Float = rotationY.angle
        location.add(Math.cos(angle).toFloat * x, y, -Math.sin(angle).toFloat * x)
        location.add(Math.sin(angle).toFloat * z, 0.0f, Math.cos(angle).toFloat * z)
    }

    def setRotation(x: Float, y: Float, z: Float): Unit = {
        rotationX.angle = x
        rotationY.angle = y
        rotationZ.angle = z
    }

    def rotate(x: Float, y: Float, z: Float): Unit = {
        rotationX.rotate(x)
        rotationY.rotate(y)
        rotationZ.rotate(z)
        if (rotationX.angle > ControlRef.X_MAX_ROT)
            if (rotationX.angle < Math.PI)
                rotationX.angle = ControlRef.X_MAX_ROT
            else if (rotationX.angle < ControlRef.X_MIN_ROT)
                rotationX.angle = ControlRef.X_MIN_ROT
    }

    def getRotationAsVector: Vector3f = new Vector3f(0.0f, 0.0f, -1.0f)
        .rotateY(rotationY.angle)
        .rotateX(rotationX.angle)
        .rotateZ(rotationZ.angle)

    private def uploadUniformMatrix(matrix4f: Matrix4f, uniformName: String): Unit = {
        val floatBuffer: FloatBuffer = BufferUtils.createFloatBuffer(16)
        val mat4Location: Int = glGetUniformLocation(ShaderUtils.currentShader, uniformName)
        if (mat4Location != -1) {
            matrix4f.get(floatBuffer)
            glUniformMatrix4fv(mat4Location, false, floatBuffer)
        }
    }

    def uploadPerspective(): Unit = uploadUniformMatrix(perspectiveMatrix, "perspectiveMatrix")

    def uploadView(): Unit = {
        val viewMatrix: Matrix4f = new Matrix4f()
            .setTranslation(location)
        viewMatrix.rotate(rotationY)
        viewMatrix.rotate(rotationX)
        viewMatrix.rotate(rotationZ)
        uploadUniformMatrix(viewMatrix.invertLookAt(new Matrix4f()), "viewMatrix")
    }

    def uploadModel(modelMatrix: Matrix4f): Unit = uploadUniformMatrix(modelMatrix, "modelMatrix")

    def uploadAll(modelMatrix: Matrix4f): Unit = {
        uploadPerspective()
        uploadView()
        uploadModel(modelMatrix)
    }

    def setPerspective(fovy: Float, aspectRatio: Float, near: Float = 0.01f, far: Float = 100.0f): Unit =
        perspectiveMatrix = new Matrix4f().perspective(fovy, aspectRatio, near, far)

    def onResizeHandler(windowId: Long, width: Int, height: Int): Unit = {
        setPerspective(Math.toRadians(45.0f).toFloat, Main.ASPECT_RATIO)
        glViewport(0, 0, width, height)
    }
}
