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

package net.benhowell.diminutives.controller

import javafx.scene.{Parent, Node}
import java.net.URL
import java.io.IOException
import javafx.fxml.FXMLLoader

/**
 * Created by Ben Howell [ben@benhowell.net] on 05-May-2014.
 */
trait ControllerLoader {
  def controllerLoader(resource: String): Node = {
    val r: URL = getClass.getResource("../view/" + resource)
    if (r == null) {
      throw new IOException("Cannot load resource: " + r)
    }
    val loader: FXMLLoader = new FXMLLoader(r)
    loader.setController(this)
    val fxmlLoad = loader.load().asInstanceOf[Parent]
    fxmlLoad
  }
}
