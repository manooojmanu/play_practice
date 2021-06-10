package com.manoj.models

import play.api.libs.json.{ Json, OFormat }

case class NewEmployee(first_name: String, last_name: Option[String], department_id: Int)

object NewEmployee {
  implicit val newEmployeeFormat: OFormat[NewEmployee] = Json.format[NewEmployee]
}
