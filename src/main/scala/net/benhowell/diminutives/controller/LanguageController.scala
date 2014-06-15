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

import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.layout.GridPane
import javafx.scene.control._

import java.net.URL
import java.util.ResourceBundle
import javafx.collections.{ObservableList, FXCollections}


import scala.collection.JavaConverters._
import scala.io.Source
import javafx.scene.control.cell.{CheckBoxTableCell, PropertyValueFactory}
import javafx.beans.property.{BooleanProperty, SimpleBooleanProperty, SimpleStringProperty}

import javafx.event.{EventHandler, ActionEvent}
import java.util
import javafx.util.Callback
import scala.collection.JavaConversions._
import net.benhowell.diminutives.core.{Display, ActionEvents, Actors, Subscription}


/**
 * Created by Ben Howell [ben@benhowell.net] on 17-May-2014.
 */

class LangRow(l: String, f: String) {

  var language = new SimpleStringProperty(l)
  var fluency = new SimpleStringProperty(f)
  var remove = new SimpleBooleanProperty(false)

  def getLanguage(): String = {
    language.get()
  }

  def setLanguage(l: String) {
    language.set(l)
  }

  def getFluency(): String = {
    fluency.get()
  }

  def setFluency(f: String) {
    fluency.set(f)
  }

  /*def remove() {
    remove.get()
  }

  def setRemove(b: Boolean) {
    remove.set(b)
  }*/
}

class LanguageController(resource: String) extends ControllerLoader with Initializable{

  @FXML private[controller] var languageGridPane : GridPane = null
  @FXML private[controller] var nextButton: Button = null
  @FXML private[controller] var prevButton: Button = null

  @FXML private[controller] var languageTableView: TableView[LangRow] = null
  @FXML private[controller] var languageColumn: TableColumn[LangRow, String] = null
  @FXML private[controller] var fluencyColumn: TableColumn[LangRow, String] = null
  @FXML private[controller] var removeColumn: TableColumn[LangRow, Boolean] = null
  @FXML private[controller] var languageComboBox: ComboBox[String] = null
  @FXML private[controller] var fluencyComboBox: ComboBox[String] = null
  @FXML private[controller] var addButton: Button = null

  val publisher = Actors.create(
    classOf[Subscription], "languagePublisher", null)
  val channel = "/event/languageController"
  val loader = controllerLoader(resource)

  ActionEvents.setButtonOnAction(nextButton, channel, "next", publisher)
  ActionEvents.setButtonOnAction(prevButton, channel, "prev", publisher)


  def load() {
    Display.fxRun( () => {
      Display.loadScreen("detail-1", loader)
    })
  }

  /*def update(intro: Map[String, String]) {
    val (id, heading, text) = Intro.compose(intro)
    Display.fxRun( () => {
      headingLabel.setText(heading)
      bodyTextArea.setText(text)
      Display.loadScreen(id, loader)
    })
  }*/

  def initialize(url: URL, rb: ResourceBundle) {
    println(this.getClass.getSimpleName + ".initialise")

    val langList: List[String] =
      Source.fromURL(getClass().getResource("/iso639-1.txt")).getLines().toList

    val fluencyList: List[String] = List(
      "Fluent",
      "Semi-fluent",
      "Basic"
    )

    val list: util.ArrayList[LangRow] = new util.ArrayList[LangRow]()
    val data: ObservableList[LangRow] = FXCollections.observableList(list)

    languageComboBox.getItems().addAll(FXCollections.observableList(langList.asJava))
    languageComboBox.setValue("English")

    fluencyComboBox.getItems().addAll(FXCollections.observableList(fluencyList.asJava))
    fluencyComboBox.setValue("Basic")

    languageColumn.setCellValueFactory(
      new PropertyValueFactory[LangRow,String]("language")
    )

    fluencyColumn.setCellValueFactory(
      new PropertyValueFactory[LangRow,String]("fluency")
    )

    val removeColumn: TableColumn[LangRow, Boolean] = new TableColumn[LangRow, Boolean]()
    /*
    val c = languageTableView.getColumns.get(2).asInstanceOf[TableColumn[LangRow, CheckBox]]
    removeColumn.setCellValueFactory(new PropertyValueFactory[LangRow, Boolean]("Remove"))
    removeColumn.setCellFactory(CheckBoxTableCell.forTableColumn(removeColumn))*/

    //removeColumn.setCellFactory(cellFactory(x => new CheckBoxCell()))
    //removeColumn.setEditable(true)


    //val c = languageTableView.getColumns.get(2).asInstanceOf[TableColumn[LangRow, CheckBox]]
    //c.setCellValueFactory(new PropertyValueFactory[LangRow, CheckBox]("Remove"))
    //c.setCellFactory(cellFactory(x => new CheckBoxCell()))


    languageTableView.setEditable(true)
    languageTableView.setItems(data)

    addButton.setOnAction(new EventHandler[ActionEvent]() {
      override def handle(e: ActionEvent) {
        data.add(new LangRow(languageComboBox.getValue(),fluencyComboBox.getValue()))
      }
    })
  }

  class CheckBoxCell extends TableCell[LangRow, Boolean] {
    override def updateItem(item: Boolean, empty: Boolean) {
      super.updateItem(item, empty)
      if(empty) {
        setItem(false)
      }
      else {
        setItem(true)
      }
    }
  }

  def cellFactory[S, T](func: TableColumn[S, T] => TableCell[S, T]): Callback[TableColumn[S, T], TableCell[S, T]] = {
    callBack[TableColumn[S, T], TableCell[S, T]]((x: TableColumn[S, T]) => {
      func(x)
    })
  }

  def callBack[S, T](func: S => T): Callback[S, T] = {
    new Callback[S, T]() {
      def call(x: S): T = {
        func(x)
      }
    }
  }

}
