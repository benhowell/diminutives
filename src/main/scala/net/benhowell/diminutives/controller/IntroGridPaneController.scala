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

import javafx.fxml.{Initializable, FXML}
import javafx.scene.layout.GridPane
import javafx.scene.control.{TextArea, Label, Button}
import java.net.URL
import java.util.ResourceBundle
import javafx.event.{ActionEvent, EventHandler}
import net.benhowell.diminutives.core._

/**
 * Created by Ben Howell [ben@benhowell.net] on 03-May-2014.
 */
class IntroGridPaneController(resource: String) extends ControllerLoader with Initializable{

  @FXML private[controller] var introGridPane : GridPane = null
  @FXML private[controller] var nextButton: Button = null
  @FXML private[controller] var prevButton: Button = null
  @FXML private[controller] var introBodyTextArea: TextArea = null
  @FXML private[controller] var introHeadingLabel: Label = null

  val introGridPanePublisher = Actors.create(
    classOf[Subscription], "introGridPanePublisher", null)

  val loader = controllerLoader(resource)

  def load(intro: Map[String, String]) {
    val (id, heading, text) = Intro.compose(intro)
    introHeadingLabel.setText(heading)
    introBodyTextArea.setText(text)
    Display.loadScreen(id, loader)
  }

  def update(intro: Map[String, String]) {
    val (id, heading, text) = Intro.compose(intro)
    Display.fxRun( () => {
      introHeadingLabel.setText(heading)
      introBodyTextArea.setText(text)
      Display.loadScreen(id, loader)
    })
  }

  def initialize(url: URL, rb: ResourceBundle) {
    println(this.getClass.getSimpleName + ".initialise")

    nextButton.setOnAction(new EventHandler[ActionEvent]() {
      def handle(event: ActionEvent) {
        SCEventBus.publish(("/event/introGridPane", "next", introGridPanePublisher))
      }
    })

    prevButton.setOnAction(new EventHandler[ActionEvent]() {
      def handle(event: ActionEvent) {
        SCEventBus.publish(("/event/introGridPane", "prev", introGridPanePublisher))
      }
    })
  }
}
