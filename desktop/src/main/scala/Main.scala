package com.github.yoitsumi.konstruktor

import com.badlogic.gdx.backends.lwjgl._

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "Konstruktor"
    cfg.height = 480
    cfg.width = 800
    cfg.forceExit = false
    new LwjglApplication(new Konstruktor, cfg)
}
