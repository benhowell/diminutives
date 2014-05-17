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

import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout.GridPane
import javafx.scene.control._
import net.benhowell.diminutives.core._
import java.net.URL
import java.util.ResourceBundle

/**
 * Created by Ben Howell [ben@benhowell.net] on 17-May-2014.
 */
class DetailController(resource: String) extends ControllerLoader with Initializable{

  @FXML private[controller] var detailGridPane : GridPane = null
  @FXML private[controller] var nextButton: Button = null
  @FXML private[controller] var prevButton: Button = null
  @FXML private[controller] var sexComboBox: ComboBox = null
  @FXML private[controller] var ageComboBox: ComboBox = null
  @FXML private[controller] var yearComboBox: ComboBox = null
  @FXML private[controller] var monthComboBox: ComboBox = null
  @FXML private[controller] var firstLanguageComboBox: ComboBox = null
  @FXML private[controller] var languageTableView: TableView = null
  @FXML private[controller] var languageColumn: TableColumn = null
  @FXML private[controller] var fluencyColumn: TableColumn = null

  val publisher = Actors.create(
    classOf[Subscription], "detailPublisher", null)
  val channel = "/event/detailController"
  val loader = controllerLoader(resource)

  ActionEvents.setButtonOnAction(nextButton, channel, "next", publisher)
  ActionEvents.setButtonOnAction(prevButton, channel, "prev", publisher)


  /*def load(intro: Map[String, String]) {
    val (id, heading, text) = Intro.compose(intro)
    headingLabel.setText(heading)
    bodyTextArea.setText(text)
    Display.loadScreen(id, loader)
  }

  def update(intro: Map[String, String]) {
    val (id, heading, text) = Intro.compose(intro)
    Display.fxRun( () => {
      headingLabel.setText(heading)
      bodyTextArea.setText(text)
      Display.loadScreen(id, loader)
    })
  }*/

  def initialize(url: URL, rb: ResourceBundle) {
    println(this.getClass.getSimpleName + ".initialise")

  }

}
