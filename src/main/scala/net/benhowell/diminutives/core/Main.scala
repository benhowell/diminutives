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
import net.benhowell.diminutives.controller.{IntroController, ExampleController, TrialController}

object Main {
  def main(args: Array[String]) {
    Application.launch(classOf[Main], args: _*)
  }
}

class Main extends Application {
  val config = Configuration.loadConfig()

  var stage: Stage = _

  // init controllers
  val introController = new IntroController("IntroGridPane.fxml")

  val exampleController = new ExampleController("TrialGridPane.fxml")

  val trialController = new TrialController("TrialGridPane.fxml")

  // init data sets
  var intro = mutable.DoubleLinkedList[Map[String,String]]() ++
    Intro.load(config, "experiment.intro")
  println("intro: " + intro)

  var examples = mutable.DoubleLinkedList[Map[String,String]]() ++
    Trial.loadExampleRun(config, "experiment.examples")
  println("examples: " + examples)

  var trials = mutable.DoubleLinkedList[Map[String,String]]() ++
    Trial.createRandomTrialRun(config, "experiment.blocks")
  println("trials: " + trials)

  // set up subscribers
  val introSubscriber = Actors.create(
    classOf[Subscription], "introSubscriber",
    (payload: Any, receiver: Any, sender: ActorRef) => payload match {
      case "next" =>
        intro.next.elem match {
          case null => exampleController.update(examples.head)
          case _ => intro = intro.next
            introController.update(intro.elem)
        }
      case "prev" =>
        intro.prev match {
          case null => println("no prev!")
          case _ =>
            intro = intro.prev
            introController.update(intro.elem)
        }
    }
  )

  val exampleSubscriber = Actors.create(
    classOf[Subscription], "exampleSubscriber",
    (payload: Any, receiver: Any, sender: ActorRef) => payload match {
      case "next" =>
        examples.next.elem match {
          case null => trialController.update(trials.head)
          case _ =>
            examples = examples.next
            exampleController.update(examples.elem)
        }
      case "prev" =>
        examples.prev match {
          case null => introController.update(intro.elem)
          case _ =>
            examples = examples.prev
            exampleController.update(examples.elem)
        }
    }
  )

  val trialSubscriber = Actors.create(
    classOf[Subscription], "trialSubscriber",
    (payload: Any, receiver: Any, sender: ActorRef) => payload match {
      case "next" =>
        trials.next.elem match {
          case null => println("no next! end the experiment and stuff")
          case _ =>
            trials = trials.next
            trialController.update(trials.elem)
        }
      case "prev" =>
        trials.prev match {
          case null => exampleController.update(examples.elem)
          case _ =>
            trials = trials.prev
            trialController.update(trials.elem)
        }
    }
  )

  // set up subscriptions
  SCEventBus.subscribe(introSubscriber, "/event/introController")
  SCEventBus.subscribe(exampleSubscriber, "/event/exampleController")
  SCEventBus.subscribe(trialSubscriber, "/event/trialController")



  override def start(primaryStage: Stage) {
    introController.load(intro.head)
    init(primaryStage, Configuration.getConfigString(config, "experiment.title"))
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
