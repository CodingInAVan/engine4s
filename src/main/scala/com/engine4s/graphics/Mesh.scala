package com.engine4s.graphics


import java.nio.FloatBuffer
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil

case class Mesh(vaoId: Int, vboId: Int, vertexCount: Int):
  def getVaoId: Int = vaoId;
  def getVertexCount: Int = vertexCount;

  def cleanUp(): Unit = {
    glDisableVertexAttribArray(0);

    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glDeleteBuffers(vboId)

    glBindVertexArray(0)
    glDeleteVertexArrays(vaoId)
  }


object Mesh:
  def apply(positions: Array[Float]): Mesh = {
    val verticesBuffer: Option[FloatBuffer] = try {
      val buffer = MemoryUtil.memAllocFloat(positions.length)
      buffer.put(positions).flip()

      val vaoId = glGenVertexArrays()
      glBindVertexArray(vaoId)

      val vboId = glGenBuffers()
      glBindBuffer(GL_ARRAY_BUFFER, vboId);
      glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
      glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0)
      glBindBuffer(GL_ARRAY_BUFFER, 0)

      glBindVertexArray(0)

      Some(buffer)
    } finally {
      None
    }

    val vertexCount = positions.length / 3

    verticesBuffer.foreach(MemoryUtil.memFree)

    new Mesh(vaoId = glGenVertexArrays(), vboId = glGenBuffers(), vertexCount = vertexCount)
  }
