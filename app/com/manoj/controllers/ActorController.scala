package com.manoj.controllers

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.manoj.actors.DivisionActor.Divide
import com.manoj.actors.SimpleActor.{ InitializeComments, ListComments }
import com.manoj.models.Comment
import com.manoj.repo.CommentsRepository
import play.api.libs.json.{ JsValue, Json, OWrites }
import play.api.mvc.{ AbstractController, Action, AnyContent, ControllerComponents }
import java.util.UUID

import com.typesafe.scalalogging.LazyLogging
import javax.inject.{ Inject, Named }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.implicitConversions

class ActorController @Inject() (
  cc: ControllerComponents,
  @Named("division_actor") divisionActor: ActorRef,
  @Named("simple_actor") simpleActor: ActorRef,
  commentsRepository: CommentsRepository
) extends AbstractController(cc)
    with LazyLogging {

  def divide1(value: String): Action[AnyContent] = Action { implicit request =>
    Json.parse(value).as[Divide]
    divisionActor ! value
    Ok("Successful")

  }

  def divide: Action[AnyContent] = Action { implicit request =>
    val dividend = request.getQueryString("dividend").map(_.toInt).get
    val divisor = request.getQueryString("divisor").map(_.toInt).get

    divisionActor ! Divide(dividend, divisor)
    Ok("Successful")

  }
  implicit val timeOut = new Timeout(30 seconds)

  def initialize() = Action.async { implicit request =>
    (simpleActor ? InitializeComments)
      .mapTo[Int]
      .map(result => Ok(Json.parse(s"""
                                      |{
                                      |"comments_saved": $result
                                      |}
                                      |""".stripMargin)))
  }

  def comments() = Action.async { implicit request =>
    print("-")
    commentsRepository.list().map { comments =>
      val response = Json.obj(("size", Json.toJson(comments.length)), ("comment", Json.toJson(comments)))
      Ok(response)
    }
  }

  def actorComments() = Action.async { implicit request =>
    val comments = (simpleActor ? ListComments).mapTo[Vector[Comment]]
    comments.map { comments =>
      val response = Json.obj(("size", Json.toJson(comments.length)), ("comment", Json.toJson(comments)))
      Ok(response)
    }
  }

  implicit def toJson[T: OWrites](t: T): JsValue = {
    Json.toJson(t)
  }

  def byUUID(uuid: String) = Action.async { implicit request =>
    commentsRepository.byUUID(UUID.fromString(uuid)).map { comment =>
      Ok(Json.toJson(comment))
    }
  }

  def dummy() = Action {
    Ok("[]")
  }

}
