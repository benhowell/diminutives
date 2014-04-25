package net.benhowell.diminutives.controller

import java.util.ResourceBundle
import java.net.URL
import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout._
import javafx.event.{EventHandler, ActionEvent}
import javafx.scene.control.{TextField, Label, Button}
import javafx.scene.image.{Image, ImageView}
import net.benhowell.diminutives.core.EventStream
import javafx.geometry.{VPos, HPos}
import javafx.scene.Node
import javafx.beans.value.{ObservableValue, ChangeListener}
import scala.collection.immutable.HashMap


/**
 * "TrialGridPane.fxml" Controller Class
 * Created by Ben Howell [ben@benhowell.net] on 06-Mar-2014.
 */
class TrialGridPaneController() extends Initializable {

  @FXML private[controller] var trialGridPane : GridPane = null
  @FXML private[controller] var nextButton: Button = null
  @FXML private[controller] var prevButton: Button = null
  @FXML private[controller] var imageView: ImageView = null
  @FXML private[controller] var label: Label = null
  @FXML private[controller] var textField: TextField = null
  @FXML private[controller] var borderPane: BorderPane = null
  @FXML private[controller] var validationLabel: Label = null

  def update(text: String, image: Image){
    textField.clear()
    label.setText(text)
    imageView.setImage(image)
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

    nextButton.setOnAction(new EventHandler[ActionEvent]() {
      def handle(event: ActionEvent) {
        EventStream.publish("uievent", "next")
      }
    })

    prevButton.setOnAction(new EventHandler[ActionEvent]() {
      def handle(event: ActionEvent) {
        EventStream.publish("uievent", "prev")
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
