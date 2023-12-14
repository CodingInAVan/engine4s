package com.engine4s.graphics

import com.engine4s.Cleanable
import org.lwjgl.opengl.GL20.*

import scala.util.Try

case class ShaderProgram(programId: Int, vertexShaderId: Option[Int] = None, fragmentShaderId: Option[Int] = None) extends Cleanable {
  def createVertexShader(shaderCode: String): Try[ShaderProgram] = Try {
    val shaderId = createShader(shaderCode, GL_VERTEX_SHADER)
    this.copy(vertexShaderId = Some(shaderId))
  }

  def createFragmentShader(shaderCode: String): Try[ShaderProgram] = Try {
    val shaderId = createShader(shaderCode, GL_FRAGMENT_SHADER)
    this.copy(fragmentShaderId = Some(shaderId))
  }

  private def createShader(shaderCode: String, shaderType: Int): Int = {
    val shaderId = glCreateShader(shaderType)
    if (shaderId == 0) {
      throw new Exception(s"Error creating shader. Type: $shaderType")
    }

    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
      throw new Exception(s"Error compiling Shader code: ${glGetShaderInfoLog(shaderId, 1024)}")
    }

    glAttachShader(programId, shaderId)

    shaderId
  }

  def link(): ShaderProgram = {
    glLinkProgram(programId)
    if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
      throw new Exception(s"Error linking Shader code: ${glGetProgramInfoLog(programId, 1024)}")
    }

    vertexShaderId.foreach(glDetachShader(programId, _))
    fragmentShaderId.foreach(glDetachShader(programId, _))

    glValidateProgram(programId)
    if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
      println(s"Warning validating Shader code: ${glGetProgramInfoLog(programId, 1024)}")
    }

    this
  }

  def bind(): Unit = glUseProgram(programId)

  def unbind(): Unit = glUseProgram(0)

  override def cleanUp(): Unit = {
    unbind()
    if (programId != 0) {
      glDeleteProgram(programId)
    }
  }
}

object ShaderProgram {
  def create(): Try[ShaderProgram] = Try {
    val programId = glCreateProgram()
    if (programId == 0) {
      throw new Exception("Could not create Shader")
    }
    ShaderProgram(programId)
  }
}
