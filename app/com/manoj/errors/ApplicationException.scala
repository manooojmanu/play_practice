package com.manoj.errors

class ApplicationException(message: String) extends Exception(message) {
  def this() = {
    this("")
  }
}

class InValidJsonException(message: String) extends ApplicationException(message)
