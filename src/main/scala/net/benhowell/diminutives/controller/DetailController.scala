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
import javafx.collections.{ListChangeListener, ObservableList, FXCollections}
import scala.collection.JavaConverters._
import scala.io.Source
import java.io.File
import javafx.scene.control.cell.{PropertyValueFactory, ComboBoxTableCell}
import javafx.beans.value.ObservableValue
import javafx.util.{Callback, StringConverter}
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.scene.control.TableColumn.CellDataFeatures
import javafx.event.{EventHandler, ActionEvent}
import scala.collection.mutable.ArrayBuffer
import java.util
import javafx.collections.ListChangeListener.Change

/**
 * Created by Ben Howell [ben@benhowell.net] on 17-May-2014.
 */

class LangRow(langs: List[String], fluency: List[String]) {
  val langList: ComboBoxTableCell[StringConverter[String], String] =
    new ComboBoxTableCell(FXCollections.observableList(langs.asJava))
  val fluencyList: ComboBoxTableCell[StringConverter[String], String] =
    new ComboBoxTableCell(FXCollections.observableList(langs.asJava))
}

class DetailController(resource: String) extends ControllerLoader with Initializable{

  @FXML private[controller] var detailGridPane : GridPane = null
  @FXML private[controller] var nextButton: Button = null
  @FXML private[controller] var prevButton: Button = null
  @FXML private[controller] var sexComboBox: ComboBox[String] = null
  @FXML private[controller] var ageComboBox: ComboBox[Int] = null
  @FXML private[controller] var yearComboBox: ComboBox[Int] = null
  @FXML private[controller] var monthComboBox: ComboBox[Int] = null
  @FXML private[controller] var firstLanguageComboBox: ComboBox[String] = null
  @FXML private[controller] var languageTableView: TableView[LangRow] = null
  @FXML private[controller] var languageColumn: TableColumn[String, String] = null
  @FXML private[controller] var fluencyColumn: TableColumn[String, String] = null
  @FXML private[controller] var addLanguageButton: Button = null

  val publisher = Actors.create(
    classOf[Subscription], "detailPublisher", null)
  val channel = "/event/detailController"
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

    sexComboBox.getItems().addAll(
      "Female",
      "Male",
      "Other"
    )

    val yearList: ObservableList[Int] = FXCollections.observableList(List(0 to 100:_*).asJava)
    ageComboBox.setItems(yearList)
    yearComboBox.setItems(yearList)
    val monthList: ObservableList[Int] = FXCollections.observableList(List(0 to 12:_*).asJava)
    monthComboBox.setItems(monthList)

    val langList: List[String] =
      Source.fromURL(getClass().getResource("/iso639-1.txt")).getLines().toList

    val fluencyList: List[String] = List(
      "Fluent",
      "Semi-fluent",
      "Basic"
    )

    var list: util.ArrayList[LangRow] = new util.ArrayList[LangRow]()
    var data: ObservableList[LangRow] = FXCollections.observableList(list)

    firstLanguageComboBox.getItems().addAll(FXCollections.observableList(langList.asJava))
    firstLanguageComboBox.setValue("English")

    languageTableView.setEditable(true)
    languageTableView.setItems(data)

    /*languageColumn.setCellValueFactory(new Callback[TableColumn.CellDataFeatures[String,String], ObservableValue[String]]() {
      override def call(cdf: CellDataFeatures[String, String]): ObservableValue[String] = {
        return new ReadOnlyObjectWrapper[String](cdf.getValue())
      }

    })*/
    languageColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(langList.asJava)))
    fluencyColumn.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableList(fluencyList.asJava)))


    addLanguageButton.setOnAction(new EventHandler[ActionEvent]() {
      override def handle(e: ActionEvent) {
        data.add(new LangRow(langList,fluencyList))
      }
    })
  }
}
