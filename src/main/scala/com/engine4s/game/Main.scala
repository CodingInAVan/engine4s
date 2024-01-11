package com.engine4s.game

import com.engine4s.graphics.render.Render
import com.engine4s.graphics.scene.Scene
import com.engine4s.{Engine, GameLogic, Window, WindowOptions}

object Main:
  def main(args: Array[String]): Unit = {
    val gameLogic = new GameLogic {
      override def init(window: Window, scene: Scene, render: Render): Unit = {
        println("init")
      }

      override def input(window: Window, scene: Scene, diffTimeMillis: Long): Unit = {
        println("input")
      }

      override def update(window: Window, scene: Scene, diffTimeMillis: Long): Unit = {
        println("update")
      }

      override def cleanUp(): Unit = {
        println("cleanUp")
      }
    }
    val gameEngine = Engine("test", WindowOptions(true, 0, 800, 600), gameLogic)
    gameEngine.start()
  }
