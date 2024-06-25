package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ship(val size: Int) {
	private lateinit var cells: Array<Cell>
	private lateinit var texture: Texture // Одна текстура для всех кораблей размера size
	var isHit: Boolean = false // Флаг попадания по кораблю
	private lateinit var hitTexture: Texture // Текстура попадания

	init {
		// Загрузка текстур
		texture = Texture(Gdx.files.internal("ship_$size.png"))
		hitTexture = Texture(Gdx.files.internal("ship_hit_$size.png")) // Загрузка текстуры попадания
	}

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
			// Используем текстуру попадания, если корабль попал
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