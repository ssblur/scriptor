package com.ssblur.scriptor.helpers

import org.joml.Vector2i

object ShapeHelper {
  fun spiral() = sequence {
    var i = 2
    var cap = 2
    var yAxis = false
    var invert = false
    var last = Vector2i(0, 1)
    while(true) {
      yield(last)
      if(yAxis) {
        last = last.add(0, if(invert) 1 else -1)
      } else {
        last = last.add(if(invert) 1 else -1, 0)
      }

      if(i >= cap) {
        i = 0
        if(yAxis) {
          invert = !invert
          cap += 2
        }
        yAxis = !yAxis
      }
      i++
    }
  }

  fun invertedSpiral() = sequence {
    val spiral = spiral().iterator()
    while(true) {
      yield(spiral.next().let { Vector2i(-it.x, -it.y) })
    }
  }
}