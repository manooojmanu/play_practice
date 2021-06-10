package com.manoj.controllers.utils

import com.manoj.errors.InValidJsonException
import com.typesafe.scalalogging.LazyLogging
import play.api.Logging
import play.api.libs.json.{ JsError, JsSuccess, OFormat }
import play.api.mvc.{ AnyContent, Request }

import javax.inject.Inject
import scala.concurrent.{ ExecutionContext, Future }
import scala.reflect.{ ClassTag, _ }

object RequestBodyExtractor extends LazyLogging {

  def extract[T: OFormat: ClassTag](implicit request: Request[AnyContent]): T = {
    request.body.asJson
      .map(_.validate[T] match {
        case JsSuccess(value, path) =>
          println("Json path ----------------------- " + path)
          logger.info("Json path ----------------------- " + path)
          value
        case JsError(errors) =>
          println("-------------------------------- " + errors)
          logger.info("Error while extracting request into " + classTag[T].runtimeClass, errors)
          throw new InValidJsonException("Error while extracting request into: " + classTag[T].runtimeClass)
      })
      .getOrElse {
        logger.error(s"No request body found for the request $request")
        throw new RuntimeException(s"No request body found for the request $request")
      }
  }
}

import play.api.mvc._

class LoggingAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser)
    with Logging {

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    logger.info("Calling action")
    block(request)
  }
}
