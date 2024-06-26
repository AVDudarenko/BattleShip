package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ship(val size: Int) {
	private lateinit var cells: Array<Cell>
	private var texture: Texture =
		Texture(Gdx.files.internal("ship_$size.png"))
	var isHit: Boolean = false
	private var hitTexture: Texture =
		Texture(Gdx.files.internal("ship_hit_$size.png"))

	fun placeShip(field: Array<Array<Cell>>, startX: Int, startY: Int, horizontal: Boolean) {
		cells = Array(size) { i ->
			if (horizontal) {
				field[startX + i][startY]
			} else {
				field[startX][startY + i]
			}
		}
		cells.forEach { it.isOccupied = true }
	}

	fun draw(batch: SpriteBatch) {
		for (cell in cells) {
			// Используем текстуру попадания, если по кораблю попали
			val textureToDraw = if (isHit) hitTexture else texture
			batch.draw(textureToDraw, cell.x, cell.y, cell.size.toFloat(), cell.size.toFloat())
		}
	}

	fun dispose() {
		texture.dispose()
		hitTexture.dispose() // Освобождаем текстуру попадания
		cells.forEach { it.dispose() }
	}

	fun hitCheck(x: Int, y: Int): Boolean {
		return cells.any { it.contains(x, y) }
	}
}