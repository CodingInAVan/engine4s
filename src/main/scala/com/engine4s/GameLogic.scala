package com.engine4s

import com.engine4s.graphics.render.Render
import com.engine4s.graphics.scene.Scene

trait GameLogic extends Cleanable {
  def init(window: Window, scene: Scene, render: Render): Unit
  def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit
  def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit
}
