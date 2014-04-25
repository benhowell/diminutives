package net.benhowell.diminutives.core

import akka.actor.{Actor, Props, ActorSystem}

/**
 * Created by Ben Howell [ben@benhowell.net] on 22-Mar-2014.
 */



sealed class Subscriber(f: (String, Any) => Unit) extends Actor {
  override def receive = { case (topic: String, payload: Any) => f(topic, payload) }
}

object EventStream{

  // ActorSystem is a heavy object: create only one per application
  // http://doc.akka.io/docs/akka/snapshot/scala/actors.html
  val system = ActorSystem("actorsystem")

  def subscribe(f: (String, Any) => Unit, name: String) = {
    val props = Props(classOf[Subscriber], f)
    val subscriber = system.actorOf(props, name = name)
    system.eventStream.subscribe(subscriber, classOf[(String, Any)])
  }

  def publish(topic: String, payload: Any) {
    system.eventStream.publish(topic, payload)
  }
}
