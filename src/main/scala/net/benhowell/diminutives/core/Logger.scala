/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Ben Howell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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

