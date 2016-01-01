package com.github.yoitsumi.konstruktor

/**
 * Created by Kamil on 24.12.2015.
 */
class CircuitState(val wireStates: Map[Wire, WireState], val transistorStates: Map[Transistor, TransistorState]) {

  def next(circuit: Circuit): CircuitState = {
    val newTrans: Map[Transistor, TransistorState] = circuit.gateConnections.mapValues { case wire =>
      TransistorState((wire.material == PWire) != wireStates(wire).powered)
    }

    val connections: Map[Wire, Seq[Wire]] = circuit.filteredConnections.filter {
      case (trans, wire) => transistorStates(trans).conducting
    }.groupBy(_._1 /*transistor*/ ).toSeq.flatMap {
      case (_, Seq((_, a), (_, b))) => Seq(a -> b, b -> a)
    }.groupBy(_._1).mapValues(_.map(_._2))

    def spread(wires: Seq[Wire], acc: Seq[Wire] = Seq()): Seq[Wire] = {
      val spreads = wires.flatMap(connections.getOrElse(_, Seq())).filterNot(acc.contains)
      val newAcc = spreads ++ wires ++ acc
      spreads.foldLeft(newAcc) { (acc, wire) =>
        if(!acc.contains(wire))
          spread(Seq(wire), acc)
        else acc
      }
    }

    val seed = circuit.wires.filter(wireStates(_).source)
    val poweredWires = spread(seed)

    val newWires = circuit.wires.map(wire => wire -> WireState(
      poweredWires.contains(wire),
      wireStates(wire).source
    )).toMap

    new CircuitState(newWires, newTrans)

  }

  override def toString: String = s"CircuitState($wireStates, $transistorStates)"

}

case class WireState(powered: Boolean, source: Boolean)

case class TransistorState(conducting: Boolean)