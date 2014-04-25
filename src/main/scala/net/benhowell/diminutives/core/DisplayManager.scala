package net.benhowell.diminutives.core

import javafx.scene.layout.StackPane
import scala.collection.mutable.HashMap
import javafx.scene.{Parent, Node}
import javafx.fxml.FXMLLoader
import java.io.IOException
import java.net.URL
import net.benhowell.diminutives.controller.TrialGridPaneController
import javafx.scene.image.Image


/**
 * Created by Ben Howell [ben@benhowell.net] on 06-Mar-2014.
 */

class DisplayManager(_resource: String) extends StackPane{

  var screens = new HashMap[String, Node]()

  val resource: URL = getClass.getResource("../view/" + _resource)
  if (resource == null) {
    throw new IOException("Cannot load resource: " + resource)
  }

  val loader: FXMLLoader = new FXMLLoader(resource)
  val fxmlLoad = loader.load().asInstanceOf[Parent]
  loader.getController()
  val trialGridPaneController = loader.getController[TrialGridPaneController]

  def loadScreen(trial: Map[String,String]){
    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    val img = new Image(trial("imagePath") + trial("imageName"))
    trialGridPaneController.update(trial("text").replace("${name}", trial("name")), img)
    addScreen(trial("id"), fxmlLoad)
  }

  def addScreen(id: String, screen: Node) {
    //Add the screen to the collection
    screens.put(id, screen)
    setScreen(id, screen)
  }

  def setScreen(id: String, screen: Node): Boolean = {
    if (screens.get(id) != null) {   //screen loaded
      if (!getChildren().isEmpty()) {    //if there is more than one screen
        getChildren().remove(0)         //remove the displayed screen
        getChildren().add(0, screen)     //add the screen
      }
      else {
        getChildren().add(screen)       //no one else been displayed, then just show
      }
      return true
    }
    else {
      println("screen hasn't been loaded!!! \n")
      return false
    }
  }

  def unloadScreen(name: String): Boolean = {
    //This method will remove the screen with the given name from the collection of screens
    if (screens.remove(name) == null) {
      println("Screen didn't exist")
      return false
    }
    else {
      return true
    }
  }

}
