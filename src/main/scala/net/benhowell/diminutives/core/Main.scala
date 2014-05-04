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

/**
 * Created by Ben Howell [ben@benhowell.net] on 02-Mar-2014.
 */

import javafx.application.Application
import javafx.stage.{Screen, Stage}
import javafx.scene.{Group, Scene}
import scala.collection.mutable
import akka.actor.ActorRef
import net.benhowell.diminutives.controller.{ExampleGridPaneController, TrialGridPaneController}


object Main {
  def main(args: Array[String]) {
    Application.launch(classOf[Main], args: _*)
  }
}

class Main extends Application {
  val config = Configuration.loadConfig()

  var stage: Stage = _

  // init controllers
  val trialGridPaneController = new TrialGridPaneController
  val trialGridPaneLoader = trialGridPaneController.controllerLoader("TrialGridPane.fxml")

  val exampleGridPaneController = new ExampleGridPaneController
  val exampleGridPaneLoader = exampleGridPaneController.controllerLoader("TrialGridPane.fxml")

  // init data sets
  var intro = mutable.DoubleLinkedList[Map[String,String]]() ++
    Intro.load(config, "diminutives.intro")
  println("intro: " + intro)

  var examples = mutable.DoubleLinkedList[Map[String,String]]() ++
    Trial.loadExampleRun(config, "diminutives.examples")
  println("examples: " + examples)

  var trials = mutable.DoubleLinkedList[Map[String,String]]() ++
    Trial.createRandomTrialRun(config, "diminutives.blocks")
  println("trials: " + trials)


  // set up subscribers
  val introSubscriber = Actors.create(
    classOf[Subscription], "introSubscriber",
    (payload: Any, receiver: Any, sender: ActorRef) => payload match {
      case "next" =>
        intro.next.elem match {
          case null => println("no next!")
          case _ => intro = intro.next
        }
        //introGridPaneController.update(intro.elem, introGridPaneLoader)

      case "prev" =>
        intro.prev match {
          case null => println("no prev!")
          case _ => intro = intro.prev
        }
        //introGridPaneController.update(intro.elem, introGridPaneLoader)
    }
  )

  val exampleSubscriber = Actors.create(
    classOf[Subscription], "exampleSubscriber",
    (payload: Any, receiver: Any, sender: ActorRef) => payload match {
      case "next" =>
        examples.next.elem match {
          case null => trialGridPaneController.update(trials.head, trialGridPaneLoader)
          case _ =>
            examples = examples.next
            exampleGridPaneController.update(examples.elem, exampleGridPaneLoader)
        }
      case "prev" =>
        examples.prev match {
          case null => println("no prev!")
          case _ =>
            examples = examples.prev
            exampleGridPaneController.update(examples.elem, exampleGridPaneLoader)
        }
    }
  )

  val trialSubscriber = Actors.create(
    classOf[Subscription], "trialSubscriber",
    (payload: Any, receiver: Any, sender: ActorRef) => payload match {
      case "next" =>
        trials.next.elem match {
          case null => println("no next!")
          case _ =>
            trials = trials.next
            trialGridPaneController.update(trials.elem, trialGridPaneLoader)
        }
      case "prev" =>
        trials.prev match {
          case null => println("no prev!")
          case _ =>
            trials = trials.prev
            trialGridPaneController.update(trials.elem, trialGridPaneLoader)
        }
    }
  )

  // set up subscriptions
  SCEventBus.subscribe(introSubscriber, "/event/introGridPane")
  SCEventBus.subscribe(exampleSubscriber, "/event/exampleGridPaneController")
  SCEventBus.subscribe(trialSubscriber, "/event/trialGridPaneController")



  override def start(primaryStage: Stage) {
    val title = Configuration.getConfigString(config, "diminutives.experiment")
    exampleGridPaneController.load(examples.head, exampleGridPaneLoader)
    init(primaryStage, title)
    primaryStage.show()
  }

  override def stop(){
    super.stop()
    //Logger.stop(ps)
  }


  def init(primaryStage: Stage, title: String) {
    stage = primaryStage
    val root = new Group()
    root.getChildren.addAll(Display)
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
