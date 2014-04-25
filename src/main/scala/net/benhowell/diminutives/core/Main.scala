package net.benhowell.diminutives.core

/**
 * Created by Ben Howell [ben@benhowell.net] on 02-Mar-2014.
 */

import javafx.application.{Platform, Application}
import javafx.stage.{Screen, Stage}
import javafx.scene.{Group, Scene}
import scala.collection.mutable
import java.io.{File, FileOutputStream, PrintStream}


object Main {
  def main(args: Array[String]) {
    Application.launch(classOf[Main], args: _*)
  }
}

class Main extends Application {
  val config = Configuration.loadConfig()
  var examples = mutable.DoubleLinkedList[Map[String,String]]() ++
    Trial.loadExampleRun(config, "diminutives.examples")

  println("examples: " + examples)

  var trials = mutable.DoubleLinkedList[Map[String,String]]() ++
    Trial.createRandomTrialRun(config, "diminutives.blocks")

  println("trials: " + trials)

  var stage: Stage = _
  var displayManager: DisplayManager = _


  val onEvent = (topic: String, payload: Any) => payload match {
    case "next" => next()
    case "prev" => prev()
  }

  def next() = trials.next.elem match {
    case null => println("no next!")
    case _ => trials = trials.next
      Platform.runLater(new Runnable() {
        def run() {
          displayManager.loadScreen(trials.elem)
        }
      })
  }

  def prev() = trials.prev match {
    case null => println("no prev!")
    case _ => trials = trials.prev
      Platform.runLater(new Runnable() {
        def run() {
          displayManager.loadScreen(trials.elem)
        }
      })
  }


    /*if (payload == "next") {
      // FIXME - package up result and stick in vector for later, or...
      // FIXME - package up result and send to wherever we collect the results


      if (trials.next.elem == null) {
        println("no next!")
        // FIXME - package up result and stick in vector, then submit vector to wherever
        //do a submit or ask them if they want to end the trial and submit or go back and review
      }
      else {
        trials = trials.next
        Platform.runLater(new Runnable() {
          def run() {
            displayManager.loadScreen(trials.elem)
          }
        })
      }
    }*/





    /*else if (payload == "prev") {
      if (trials.prev == null) {
        println("no prev!")
        //go back to previous screen run... e.g. personal info form
      }
      else {
        trials = trials.prev
        Platform.runLater(new Runnable() {
          def run() {
            displayManager.loadScreen(trials.elem)
          }
        })
      }
    }
    else
      println("msg: " + topic + " matched nothing")

  }*/





  // setup subscriptions
  val ps = new PrintStream(new FileOutputStream(new File("output.txt")))
  EventStream.subscribe(Logger.onEvent(ps), "loggersubscriber")
  EventStream.subscribe(onEvent, "mainsubscriber")

  override def start(primaryStage: Stage) {
    val title = Configuration.getConfigString(config, "diminutives.experiment")
    displayManager = new DisplayManager("TrialGridPane.fxml")
    //displayManager.loadScreen(trials.head)
    displayManager.loadScreen(examples.head)
    init(primaryStage, title)
    primaryStage.show()
  }

  override def stop(){
    super.stop()
    Logger.stop(ps)
  }


  def init(primaryStage: Stage, title: String) {
    stage = primaryStage
    val root = new Group()
    root.getChildren.addAll(displayManager)
    primaryStage.setTitle(title)
    primaryStage.centerOnScreen()
    primaryStage.setScene(new Scene(root, 800, 600))
    primaryStage.sizeToScene()
    //setMaximised(primaryStage)
  }





  def setMaximised(stage: Stage){
    val bounds = Screen.getPrimary().getVisualBounds()
    stage.setX(bounds.getMinX())
    stage.setY(bounds.getMinY())
    stage.setWidth(bounds.getWidth())
    stage.setHeight(bounds.getHeight())

  }
}
