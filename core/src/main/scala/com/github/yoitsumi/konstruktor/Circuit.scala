package com.github.yoitsumi.konstruktor

import com.badlogic.gdx.graphics.Color

/**
 * Created by Kamil on 24.12.2015.
 */
class Circuit(val wires: Seq[Wire], val transistors: Seq[Transistor]) {

  lazy val gateConnections: Map[Transistor, Wire] =
    connectionsFor(_.gate)

  lazy val sourceConnections: Map[Transistor, Wire] =
    connectionsFor(_.source)

  lazy val drainConnections: Map[Transistor, Wire] =
    connectionsFor(_.drain)

  lazy val filteredConnections: Seq[(Transistor, Wire)] =
    sourceConnections.toSeq ++ drainConnections.toSeq

  private def connectionsFor(leg: Transistor => Direction): Map[Transistor, Wire] =
    (for {
      wire <- wires
      point <- wire.points
      transistor <- transistors
      if point == leg(transistor).translate(transistor.position)
    } yield transistor -> wire ).toMap

}

case class Wire(points: Seq[Point], material: WireType)

sealed abstract class WireType(val color: Color, val activeColor: Color) {
  def colorForState(state: WireState): Color =
    if(state.powered) activeColor else color
}

case object MetalWire extends WireType(Color.GRAY, Color.LIGHT_GRAY)
case object PWire extends WireType(Color.RED, Color.MAGENTA)
case object NWire extends WireType(Color.BLUE, Color.CYAN)

case class Point(x: Int, y: Int, z: Int)

case class Proximity(p: Point, w: Wire, circuit: Circuit) {
  def top: Boolean = check(Top)
  def bottom: Boolean = check(Bottom)
  def left: Boolean = check(Left)
  def right: Boolean = check(Right)
  def front: Boolean = check(Front)
  def back: Boolean = check(Back)

  def check(d: Direction): Boolean =
    w.points.contains(d.translate(p)) ||
    circuit.transistors.exists(_.position == d.translate(p))
}


case class Transistor(position: Point, gate: Direction, source: Direction, drain: Direction) {

}