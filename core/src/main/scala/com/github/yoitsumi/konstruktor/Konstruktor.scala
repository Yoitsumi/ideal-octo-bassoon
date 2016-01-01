package com.github.yoitsumi.konstruktor

import com.badlogic.gdx.Game

class Konstruktor extends Game {
  override def create(): Unit = {
    setScreen(new EditScreen)
  }
}
