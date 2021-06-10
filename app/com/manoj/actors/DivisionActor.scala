package com.manoj.actors

import akka.actor.SupervisorStrategy.{ Restart, Resume, Stop }
import akka.actor.{ Actor, OneForOneStrategy, SupervisorStrategy, Timers }
import com.manoj.actors.DivisionActor.Divide
import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.Json

object DivisionActor {
  case class Divide(dividend: Int, divisor: Int)

  object Divide {
    implicit val divideFormat = Json.format[Divide]
  }
}

class DivisionActor extends Actor with Timers with LazyLogging {

  override lazy val supervisorStrategy: SupervisorStrategy = OneForOneStrategy() {
    case _ => Stop
  }
  var count = 0

  override def receive: Receive = {
    case divide @ Divide(dividend, divisor) =>
      logger.info(s"Inside DivisionActor and dividing $dividend by $divisor")
      divideF(divide)
      count += 1
      logger.info(s"count value -------------------------------- $count")
  }

  private def divideF(divide: Divide) = {
    divide.dividend / divide.divisor
  }

  override def preStart(): Unit = {
    logger.info(s"Starting actor with name ${self.path} and supervisionStrategy: $supervisorStrategy")
    super.preStart()
  }

  override def postStop(): Unit = {
    logger.info(s"Stopping actor xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  $self")
    super.postStop()
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {

    logger.info(s"Restarting Actor --------------------->>>>>>>> $self")
    super.preRestart(reason, message)
  }

}
