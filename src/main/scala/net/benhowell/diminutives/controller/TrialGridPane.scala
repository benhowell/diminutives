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
import javafx.scene.layout.{BorderPane, GridPane}
import javafx.scene.control.{TextField, Label, Button}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.Node
import net.benhowell.diminutives.core._
import java.net.URL
import java.util.ResourceBundle
import javafx.geometry.{VPos, HPos}
import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.event.ActionEvent
import akka.actor.ActorRef

/**
 * Created by Ben Howell [ben@benhowell.net] on 04-May-2014.
 */
trait TrialGridPane extends ControllerLoader with Initializable{

  @FXML private[controller] var trialGridPane : GridPane = null
  @FXML private[controller] var nextButton: Button = null
  @FXML private[controller] var prevButton: Button = null
  @FXML private[controller] var imageView: ImageView = null
  @FXML private[controller] var label: Label = null
  @FXML private[controller] var textField: TextField = null
  @FXML private[controller] var borderPane: BorderPane = null
  @FXML private[controller] var validationLabel: Label = null

  val channel: String
  val publisher: ActorRef
  val loader: Node

  def update(trial: Map[String, String]) {
    println(s"trial: $trial")
    val (id, text, imgPath) = Trial.compose(trial)
    Display.fxRun( () => {
      textField.clear()
      label.setText(text)
      imageView.setImage(new Image(imgPath))
      Display.loadScreen(id, loader)
    })
  }

  def initialize(url: URL, rb: ResourceBundle) {
    println(this.getClass.getSimpleName + ".initialise")

    borderPane.setCenter(imageView)
    GridPane.setHalignment(borderPane, HPos.CENTER)
    GridPane.setValignment(borderPane, VPos.CENTER)
    nextButton.setDisable(true)

    textField.textProperty().addListener(new ChangeListener[String]() {
      val valid = """^[a-zA-Z]+$""".r
      val empty = "^[\\s]*$".r

      override def changed(observable: ObservableValue[_ <: String], oldVal: String, newVal: String) = newVal match {
        case valid() => validInput
        case empty() => emptyInput
        case _ => invalidInput
      }
    })
  }

  def reset(){
    textField.requestFocus()
  }

  def emptyInput(){
    nextButton.setDisable(true)
    switchStyle(validationLabel, "validation_error", "validation_valid")
    switchStyle(textField, "validation_error", "validation_valid")
    textField.requestFocus()
  }

  def invalidInput(){
    nextButton.setDisable(true)
    switchStyle(validationLabel, "validation_valid", "validation_error")
    switchStyle(textField, "validation_valid", "validation_error")
    textField.requestFocus()
  }

  def validInput(){
    nextButton.setDisable(false)
    switchStyle(validationLabel, "validation_error", "validation_valid")
    switchStyle(textField, "validation_error", "validation_valid")
    textField.requestFocus()
  }

  def switchStyle(node: Node, oldStyle: String, newStyle: String){
    removeStyle(node, oldStyle)
    addStyle(node, newStyle)
  }

  def addStyle(node: Node, style: String) {
    node.getStyleClass().add(style)
  }

  def removeStyle(node: Node, style: String) {
    node.getStyleClass().remove(style)
  }

}
