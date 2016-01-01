package com.github.yoitsumi.konstruktor

/**
 * Created by Kamil on 24.12.2015.
 */
sealed abstract class Direction(val xd: Int, yd: Int, zd: Int) {
  def translate(p: Point): Point = new Point(p.x + xd, p.y + yd, p.z + zd)
}

case object Top extends Direction(0, 1, 0)
case object Bottom extends Direction(0, -1, 0)
case object Left extends Direction(-1, 0, 0)
case object Right extends Direction(1, 0, 0)
case object Front extends Direction(0, 0, 1)
case object Back extends Direction(0, 0, -1)