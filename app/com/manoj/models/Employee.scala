package com.manoj.models

import play.api.libs.json.{ Json, OFormat }

case class Employee(id: Long, firsName: String, lastName: String, departmentId: Int)

object Employee {
  implicit val empFormat: OFormat[Employee] = Json.format[Employee]
}
