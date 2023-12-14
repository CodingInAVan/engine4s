package com.engine4s.graphics

import com.engine4s.scene.Scene
import com.engine4s.{Cleanable, Window}
import org.lwjgl.opengl.GL
import com.engine4s.Window
import org.lwjgl.opengl.GL11.*


class Render extends Cleanable {
  GL.createCapabilities();

  override def cleanUp(): Unit = {}

  def render(window: Window, scene: Scene): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)
  }
}
