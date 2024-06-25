package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx

class BattleShipGame : ApplicationAdapter() {
	private var gameScreen: GameScreen? = null

	override fun create() {
		gameScreen = GameScreen()
		Gdx.input.inputProcessor = gameScreen // Назначаем GameScreen как InputProcessor
	}

	override fun render() {
		gameScreen!!.render()
	}

	override fun dispose() {
		gameScreen!!.dispose()
	}
}