package com.ssblur.scriptor.error

class MissingRequiredElementException(element: String?, message: String?): RuntimeException(
  String.format(
    "Missing required element %s: %s",
    element,
    message
  )
) {
}
