package com.github.yoitsumi.konstruktor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.{GL20, Mesh}
import com.badlogic.gdx.graphics.g3d.{Material, Model}
import com.badlogic.gdx.graphics.g3d.model.data.{ModelMesh, ModelData}
import com.badlogic.gdx.graphics.g3d.utils.{MeshPartBuilder, MeshBuilder, ModelBuilder}
import com.badlogic.gdx.math.Vector3
import GdxUtils._

/**
 * Created by Kamil on 24.12.2015.
 */
object ShapeUtils {

  val I = .3f // Inner size
  val T = .5f // Transistor size

  def wire(prox: Proximity, material: Material): Model = {
    import prox._

    val mb = new ModelBuilder
    mb.begin()
    box(mb, "center", material, 0, 0, 0, I, I, I)
    if(top)
      box(mb, "top", material, 0, .25f+I/4, 0, I, .5f-I/2, I)
    if(bottom)
      box(mb, "bottom", material, 0, -(.25f+I/4), 0, I, .5f-I/2, I)
    if(right)
      box(mb, "right", material, .25f+I/4, 0, 0, .5f-I/2, I, I)
    if(left)
      box(mb, "left", material, -(.25f+I/4), 0, 0, .5f-I/2, I, I)
    if(front)
      box(mb, "front", material, 0, 0, .25f+I/4, I, I, .5f-I/2)
    if(back)
      box(mb, "back", material, 0, 0, -(.25f+I/4), I, I, .5f-I/2)
    mb.end()
  }

  def box(mb: ModelBuilder, id: String, material: Material,
          x: Float, y: Float, z: Float,
          w: Float, h: Float, d: Float): Unit = {
    mb.part(id, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, material).
    box(x, y, z, w, h, d)
  }

  def transistor(trans: Transistor, gateMaterial: Material, sourceMaterial: Material, drainMaterial: Material): Model = {
    val mb = new ModelBuilder
    mb.begin()
    box(mb, "center", gateMaterial, 0, 0, 0, T, T, T)
    def connection(dir: Direction, mat: Material) = dir match {
      case Top =>    box(mb, "top", mat, 0, .25f+I/4, 0, I, .5f-I/2, I)
      case Bottom => box(mb, "bottom", mat, 0, -(.25f+I/4), 0, I, .5f-I/2, I)
      case Right =>  box(mb, "right", mat, .25f+I/4, 0, 0, .5f-I/2, I, I)
      case Left =>   box(mb, "left", mat, -(.25f+I/4), 0, 0, .5f-I/2, I, I)
      case Front =>  box(mb, "front", mat, 0, 0, .25f+I/4, I, I, .5f-I/2)
      case Back =>   box(mb, "back", mat, 0, 0, -(.25f+I/4), I, I, .5f-I/2)
    }
    connection(trans.gate, gateMaterial)
    connection(trans.source, sourceMaterial)
    connection(trans.drain, drainMaterial)
    mb.end()
  }

}
