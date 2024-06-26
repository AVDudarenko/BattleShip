package com.mygdx.game

import com.badlogic.gdx.Game

class BattleShipGame : Game() {
	override fun create() {
		setScreen(MenuScreen(this))
	}

	override fun dispose() {
		screen.dispose()
	}
}