package com.engine4s

import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWVidMode}
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil
import scala.util.Try
import org.lwjgl.system.MemoryUtil.NULL

case class Window(
  title: String,
  opts: WindowOptions,
  resizeFunc: () => Unit,
  var windowHandle: Long = 0
) extends Cleanable {

  private var width: Int = opts.width
  private var height: Int = opts.height

  def init(): Unit = {

    if (!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW")
    }

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)

    if (opts.compatibleProfile) {
      glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE)
    } else {
      // removes deprecated features of OpenGL. More morden version of OpenGL
      glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
      glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    }

    val (w, h) = if (opts.width > 0 && opts.height > 0) {
      (opts.width, opts.height)
    } else {
      glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE)
      val vidMode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
      (vidMode.width(), vidMode.height())
    }

    windowHandle = glfwCreateWindow(width, height, title, NULL, NULL)
    if (windowHandle == NULL) {
      throw new RuntimeException("Failed to create the GLFW window")
    }

    glfwSetFramebufferSizeCallback(windowHandle, (window, w, h) => resized(w, h))
    glfwSetErrorCallback((errorCode, msgPtr) => println(s"Error code [$errorCode], msg [${MemoryUtil.memUTF8(msgPtr)}]"))
    glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) => keyCallBack(key, action))
    glfwMakeContextCurrent(windowHandle)
    if (opts.fps > 0) glfwSwapInterval(0) else glfwSwapInterval(1)
    glfwShowWindow(windowHandle)

    val arrWidth = Array(1)
    val arrHeight = Array(1)
    glfwGetFramebufferSize(windowHandle, arrWidth, arrHeight)
    width = arrWidth(0)
    height = arrHeight(0)
  }

  init()

  def getHeight: Int = height
  def getWidth: Int = width
  def getWindowHandle: Long = windowHandle
  def isKeyPressed(keyCode: Int): Boolean = glfwGetKey(windowHandle, keyCode) == GLFW_PRESS
  def pollEvents(): Unit = glfwPollEvents()
  def update(): Unit = glfwSwapBuffers(windowHandle)
  def windowShouldClose: Boolean = glfwWindowShouldClose(windowHandle)

  override def cleanUp(): Unit = {
    glfwFreeCallbacks(windowHandle)
    glfwDestroyWindow(windowHandle)
    glfwTerminate()
    Option(glfwSetErrorCallback(null)).foreach(_.free())
  }

  private def keyCallBack(key: Int, action: Int): Unit = {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
      glfwSetWindowShouldClose(windowHandle, true)
    }
  }

  private def resized(w: Int, h: Int): Unit = {
    width = w
    height = h
    Try(resizeFunc()).recover {
      case exception => println("Error calling resize callback "+ exception)
    }
  }
}
case class WindowOptions(compatibleProfile: Boolean, fps: Int, width: Int, height: Int, ups: Int = Engine.TARGET_UPS)