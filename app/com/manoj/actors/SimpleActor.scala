package com.manoj.actors

import akka.actor.{ Actor, Timers }
import akka.util.Timeout
import com.manoj.actors.SimpleActor._
import com.manoj.client.RestClient
import com.manoj.models.Comment
import com.manoj.repo.CommentsRepository
import com.typesafe.scalalogging.LazyLogging

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import akka.pattern.pipe

object SimpleActor {
  case object InitializeComments
  case object ListComments
  case object Timer
  case object StopTimer
}

class SimpleActor @Inject() (restClient: RestClient, commentsRepository: CommentsRepository)
    extends Actor
    with Timers
    with LazyLogging {
  implicit val timeOut = new Timeout(30 seconds)
  var count = 0
  var commentCount = 1
  val timerUUID = UUID.randomUUID()

  override def receive: Receive = {
    case InitializeComments =>
      restClient
        .getComments()
        .flatMap { comments =>
//          logger.info("Comments ------------------- idss " + comments.length)
          commentsRepository.bulkSave(
            comments.map(c => Comment(UUID.randomUUID(), c.postId, c.name, c.email, c.body))
          )
        }
        .pipeTo(sender())
    case ListComments =>
      print(".")
      commentsRepository.list().pipeTo(sender())
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
