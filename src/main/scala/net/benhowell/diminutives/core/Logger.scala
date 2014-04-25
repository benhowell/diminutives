package net.benhowell.diminutives.core

import java.io._

/**
 * Created by Ben Howell [ben@benhowell.net] on 25-Mar-2014.
 */
object Logger {

  def onEvent(ps: PrintStream) = (topic: String, payload: Any) => {

      println("yay Logger received message: " + topic)
      try {
        ps.println("[" + topic + "]" + topic)
      }
      catch {
        case ioe: IOException => println("IOException: " + ioe.toString)
        case e: IOException => println("Exception: " + e.toString)
      }
  }

  def stop(ps: PrintStream) = ps.close()
}

