package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

class BattleShipGame : ApplicationAdapter() {
	private var gameScreen: GameScreen? = null

	override fun create() {
		gameScreen = GameScreen()
		Gdx.input.inputProcessor = gameScreen // Назначаем GameScreen как InputProcessor
	}

	override fun render() {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		gameScreen!!.act(Gdx.graphics.deltaTime)
		gameScreen!!.draw()

		// Вместо вызова gameScreen.render(), используем gameScreen.act() и gameScreen.draw()
	}

	override fun dispose() {
		gameScreen!!.dispose()
	}
}