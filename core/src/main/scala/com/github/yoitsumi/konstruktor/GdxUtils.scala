package com.github.yoitsumi.konstruktor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Vector3

/**
 * Created by Kamil on 24.12.2015.
 */
object GdxUtils {

  implicit class BatchImplicits[B <: Batch](val batch: B) extends AnyVal {

    def use[R](block: => R): R = {
      batch.begin()
      val res = block
      batch.end()
      res
    }

  }

  implicit class RichVector3(val vec: Vector3) extends AnyVal {
    def copy(x: Float = vec.x, y: Float = vec.y, z: Float = vec.z): Vector3 =
      new Vector3(x, y, z)

    def *(s: Float): Vector3 = vec.cpy scl s

    def +(v: Vector3): Vector3 = vec.cpy add v
  }

}
