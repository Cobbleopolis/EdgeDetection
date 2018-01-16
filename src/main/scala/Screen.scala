import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

object Screen {

    var baseFrameBuffer: Int = _

    var rboDepth: Int = _

    var fboTexture: Int = _

    var fboNormal: Int = _

    var isInitalised: Boolean = false

    def init(): Unit = {
        fboTexture = glGenTextures()
        fboNormal = glGenTextures()
        rboDepth = glGenRenderbuffers()
        baseFrameBuffer = glGenFramebuffers()

        glBindFramebuffer(GL_FRAMEBUFFER, baseFrameBuffer)


        glBindTexture(GL_TEXTURE_2D, fboTexture)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Main.WIDTH, Main.HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, fboTexture, 0)

        glBindTexture(GL_TEXTURE_2D, fboNormal)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, Main.WIDTH, Main.HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D, fboNormal, 0)

        glBindRenderbuffer(GL_RENDERBUFFER, rboDepth)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, Main.WIDTH, Main.HEIGHT)
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboDepth)

        glDrawBuffers(Array[Int](GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1))

        glBindFramebuffer(GL_FRAMEBUFFER, 0)

        checkFrameBufferIsComplete()
        GLUtils.checkErrors()
        isInitalised = true
    }

    def checkFrameBufferIsComplete(): Unit = {
        import org.lwjgl.opengl.EXTFramebufferObject
        val framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT)
        framebuffer match {
            case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT =>
                println(s"FrameBuffer: $baseFrameBuffer, is complete")
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT =>
                throw new RuntimeException(s"FrameBuffer: $baseFrameBuffer, has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception")
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT =>
                throw new RuntimeException(s"FrameBuffer: $baseFrameBuffer, has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception")
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT =>
                throw new RuntimeException(s"FrameBuffer: $baseFrameBuffer, has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception")
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT =>
                throw new RuntimeException(s"FrameBuffer: $baseFrameBuffer, has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception")
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT =>
                throw new RuntimeException(s"FrameBuffer: $baseFrameBuffer, has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception")
            case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT =>
                throw new RuntimeException(s"FrameBuffer: $baseFrameBuffer, has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception")
            case _ =>
                throw new RuntimeException(s"Unexpected reply from glCheckFramebufferStatusEXT: $framebuffer")
        }
    }

    def setResolutionUniform(width: Float = Main.WIDTH, height: Float = Main.HEIGHT): Unit = {
        if (Main.screenShader != null) {
            val resLoc: Int = Main.screenShader.getUniformLocation("resolution")
            glUniform2f(resLoc, width, height)
        }
    }

    def bindBaseFrameBuffer(): Unit = {
        glBindFramebuffer(GL_FRAMEBUFFER, baseFrameBuffer)
        glViewport(0, 0, Main.WIDTH, Main.HEIGHT)
    }

    def bindScreenFrameBuffer(): Unit = {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glViewport(0, 0, Main.WIDTH, Main.HEIGHT)
    }

    def destroy(): Unit = {
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glBindTexture(GL_TEXTURE_2D, 0)
        glBindRenderbuffer(GL_RENDERBUFFER, 0)

        glDeleteFramebuffers(baseFrameBuffer)
        glDeleteTextures(fboTexture)
        glDeleteTextures(fboNormal)
        glDeleteRenderbuffers(rboDepth)
    }

    def onResizeHandler(windowId: Long, width: Int, height: Int): Unit = {
        glBindTexture(GL_TEXTURE_2D, fboTexture)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glBindTexture(GL_TEXTURE_2D, fboNormal)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0)
        glBindTexture(GL_TEXTURE_2D, 0)


        glBindRenderbuffer(GL_RENDERBUFFER, rboDepth)
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height)
        glBindRenderbuffer(GL_RENDERBUFFER, 0)
    }


}
