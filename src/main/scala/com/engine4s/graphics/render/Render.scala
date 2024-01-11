package com.engine4s.graphics.render

import com.engine4s.graphics.scene.Scene
import com.engine4s.{Cleanable, Window}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

trait Render extends Cleanable {
  def render(window: Window, scene: Scene): Unit
}

class OpenGlRender extends Render {
  GL.createCapabilities();

  override def cleanUp(): Unit = {}

  override def render(window: Window, scene: Scene): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }
}
