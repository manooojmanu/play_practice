package com.manoj.controllers

import com.manoj.models.NewEmployee
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject
import com.manoj.controllers.utils.RequestBodyExtractor._
import com.manoj.service.EmployeeService
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmployeeController @Inject() (cc: ControllerComponents, employeeService: EmployeeService)
    extends AbstractController(cc) {

  def create: Action[AnyContent] = {
    Action.async { implicit request =>
      val newEmployee = extract[NewEmployee]
      Future(employeeService.save(newEmployee)).map(result => Ok(Json.toJson(result)))
    }
  }
}
