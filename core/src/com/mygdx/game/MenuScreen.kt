package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport

class MainMenuScreen(private val game: BattleShipGame) : Stage(ScreenViewport()), Screen {
    private val batch = SpriteBatch()
    private val font = BitmapFont()
    private val skin = Skin(Gdx.files.internal("uiskin.json"))
    private val startButton: TextButton

    init {
        Gdx.input.inputProcessor = this // Устанавливаем MainMenuScreen как InputProcessor
        startButton = TextButton("Start", skin)
        startButton.setSize(200f, 100f)
        startButton.setPosition((Gdx.graphics.width - startButton.width) / 2, (Gdx.graphics.height - startButton.height) / 2)
        startButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                game.setScreen(GameScreen())
            }
        })
        addActor(startButton)
    }

    override fun show() {}

    override fun render(delta: Float) {
        act(delta)
        draw()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        batch.dispose()
        font.dispose()
        skin.dispose()
    }
}