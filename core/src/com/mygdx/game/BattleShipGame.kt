package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20

class BattleShipGame : Game() {
	override fun create() {
		setScreen(MainMenuScreen(this))
	}

	override fun dispose() {
		screen.dispose()
	}
}