package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Cell(val x: Float, val y: Float, val size: Int) {
	var isOccupied = false
	var isHit = false
	private val textureEmpty = Texture(Gdx.files.internal("cell_empty.png"))
	private val textureHit = Texture(Gdx.files.internal("cell_hit.png"))
	private val textureMiss = Texture(Gdx.files.internal("cell_miss.png"))

	fun draw(batch: SpriteBatch) {
		val texture = when {
			isHit && isOccupied -> textureHit
			isHit -> textureMiss
			else -> textureEmpty
		}
		batch.draw(texture, x, y, size.toFloat(), size.toFloat())
	}

	fun contains(screenX: Int, screenY: Int): Boolean {
		val relativeX = screenX - x
		val relativeY = screenY - y
		return (0 until size).contains<Any>(element = relativeX) && (0 until size).contains<Any>(
			element = relativeY
		)
	}

	fun dispose() {
		textureEmpty.dispose()
		textureHit.dispose()
		textureMiss.dispose()
	}
}