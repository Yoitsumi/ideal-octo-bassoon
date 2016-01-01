package com.github.yoitsumi.konstruktor

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight
import com.badlogic.gdx.graphics.{GL20, PerspectiveCamera, VertexAttribute, Color}
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d._
import com.badlogic.gdx.graphics.g3d.utils.{CameraInputController, ModelBuilder}
import com.badlogic.gdx.math.{Matrix4, Vector3}
import com.badlogic.gdx.{Gdx, Screen}

/**
 * Created by Kamil on 24.12.2015.
 */
class EditScreen extends Screen {

  private val batch = new ModelBatch()

  private var state = new CircuitState(Map(
    Wire(Seq(Point(-1, 0, 0)), PWire) -> WireState(true, true),
    Wire(Seq(Point(1, 0, 0)), PWire) -> WireState(false, false),
    Wire(Seq(Point(0, 1, 0)), NWire) -> WireState(true, true)
  ), Map(
    Transistor(Point(0, 0, 0), Top, Left, Right) -> TransistorState(false)
  ))

  private var circuit = new Circuit(state.wireStates.keys.toSeq, state.transistorStates.keys.toSeq)

//  val cube = {
//    val modelBuilder = new ModelBuilder
////    val model = modelBuilder.createBox(1, 1, 1,
////      new Material(new ColorAttribute(ColorAttribute.Diffuse, Color.RED)),
////      Usage.Position | Usage.Normal)
////    new ModelInstance(model)
//    val model = ShapeUtils.wire(true, false, true, false, true , false,
//                  new Material(new ColorAttribute(ColorAttribute.Diffuse, Color.RED)))
//    new ModelInstance(model)
//  }

  def material(color: Color) =
    new Material(new ColorAttribute(ColorAttribute.Diffuse, color))

  val environment = new Environment
//  environment.set(ColorAttribute.createAmbient(new Color(.8f, .4f, .4f, 1f)))
  environment.add(new DirectionalLight().set(.8f, .8f, .8f, -1f, -.8f, -.2f))
  environment.add(new DirectionalLight().set(1f, 1f, 1f, 1f, .8f, .2f))

  val camera = new PerspectiveCamera(67,
    Gdx.graphics.getWidth, Gdx.graphics.getHeight)
  camera.position.set(10f, 10f, 10f)
  camera.lookAt(0, 0, 0)
  camera.near = 1
  camera.far = 100
  camera.update()

  val camController = new CameraInputController(camera)
  Gdx.input.setInputProcessor(camController)


  override def hide(): Unit = ()

  override def resize(width: Int, height: Int): Unit = ()

  override def dispose(): Unit = ()

  override def pause(): Unit = ()

  override def render(delta: Float): Unit = {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT)

//    cube.transform.rotate(Vector3.Y, Gdx.input.getDeltaX)

    batch.begin(camera)
//    batch.render(cube, environment)

    for(wire <- circuit.wires; point <- wire.points) {
      val color = if(state.wireStates(wire).powered) wire.material.activeColor else wire.material.color
      val model = ShapeUtils.wire(new Proximity(point, wire, circuit), material(color))
      val instance = new ModelInstance(model, new Vector3(point.x, point.y, point.z))
      batch.render(instance, environment)
    }

    for(transistor <- circuit.transistors) {
      val gate = circuit.gateConnections(transistor)
      val source = circuit.sourceConnections(transistor)
      val drain = circuit.drainConnections(transistor)
      val model = ShapeUtils.transistor(transistor,
        material(gate.material.colorForState(state.wireStates(gate))),
        material(source.material.colorForState(state.wireStates(source))),
        material(drain.material.colorForState(state.wireStates(drain))) )
      val instance = new ModelInstance(model, new Vector3(transistor.position.x, transistor.position.y, transistor.position.z))
      batch.render(instance, environment)
    }

    batch.end()



    if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
      println(state)
      state = state.next(circuit)
      println(state)
    }
  }

  override def show(): Unit = ()

  override def resume(): Unit = ()
}
