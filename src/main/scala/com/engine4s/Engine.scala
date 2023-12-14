package com.engine4s

import com.engine4s.graphics.Render
import com.engine4s.scene.Scene

case class Engine(
  windowTitle: String,
  opts: WindowOptions,
  gameLogic: GameLogic) extends Cleanable {

  private val window = Window(windowTitle, opts, () => {
    resize();
  })
  private val targetFps = opts.fps
  private val targetUps = opts.ups
  private val render = new Render
  private val scene = new Scene
  private var running = false

  gameLogic.init(window, scene, render);

  override def cleanUp(): Unit = {
    gameLogic.cleanUp()
    render.cleanUp()
    scene.cleanUp()
    window.cleanUp()
  }
  def resize(): Unit = {

  }
  def start(): Unit = {
    running = true;
    run()
  }

  def stop(): Unit = {
    running = false
  }

  def run(): Unit = {
    var initialTime = System.currentTimeMillis()
    val timeU: Float = 1000.0f / targetUps
    val timeR: Float = if(targetFps > 0) 1000.0f / targetFps else 0
    var deltaUpdate: Float = 0
    var deltaFps: Float = 0

    var updateTime: Long = initialTime
    while(running && !window.windowShouldClose) {
      window.pollEvents()

      val now = System.currentTimeMillis()
      deltaUpdate = deltaUpdate + (now - initialTime) / timeU
      deltaFps = deltaFps + (now - initialTime) / timeR

      if(targetFps <= 0 || deltaFps >= 1) {
        gameLogic.input(window, scene, now - initialTime)
      }

      if(deltaUpdate >= 1) {
        val diffTimeMillis = now - updateTime;
        gameLogic.update(window, scene, diffTimeMillis)
        updateTime = now
        deltaUpdate = deltaUpdate - 1
      }

      if (targetFps <= 0 || deltaFps >= 1) {
        render.render(window, scene)
        deltaFps = deltaFps - 1
        window.update()
      }
      initialTime = now
    }

    cleanUp()
  }
}

object Engine {
  val TARGET_UPS = 30
}