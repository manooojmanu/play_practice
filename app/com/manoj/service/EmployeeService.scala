package com.manoj.service

import com.manoj.models.{ Employee, NewEmployee }
import com.typesafe.scalalogging.LazyLogging

import scala.collection.mutable.ListBuffer

class EmployeeService extends LazyLogging {
  private var list: ListBuffer[Employee] = new ListBuffer[Employee]

  def fromNewEmployee(newEmployee: NewEmployee): Employee = {
    Employee(list.length + 1, newEmployee.first_name, newEmployee.first_name, newEmployee.department_id)
  }

  def save(newEmployee: NewEmployee) = {
    logger.info("Saving Employee: " + NewEmployee)
    val employee = fromNewEmployee(newEmployee)
    list += employee
    employee
  }
}
