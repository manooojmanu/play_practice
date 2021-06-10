package com.manoj.models

import play.api.libs.json.{ Json, OFormat }

import java.util.UUID

case class Comment(uuid: UUID, postId: Int, name: String, email: String, body: String)

object Comment {
  implicit val commentFormat: OFormat[Comment] = Json.format[Comment]
}
