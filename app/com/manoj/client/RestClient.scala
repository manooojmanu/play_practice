package com.manoj.client

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.json.{ Json, OFormat }
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class CommentRequest(id: Int, postId: Int, name: String, email: String, body: String)

object CommentRequest {
  implicit lazy val commentFormat: OFormat[CommentRequest] = Json.format[CommentRequest]
}

class RestClient @Inject() (ws: WSClient) extends play.api.libs.ws.WSBodyReadables with LazyLogging {

  def getComments(): Future[List[CommentRequest]] = {
//    logger.info(s"Fetching Comments------------------------------- ")

    ws.url("https://jsonplaceholder.typicode.com/comments").get().map { s =>
      s.json.as[List[CommentRequest]]
    }
  }
}
