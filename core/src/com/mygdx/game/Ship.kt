package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ship(val size: Int) {
	private lateinit var cells: Array<Cell>

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
			// Выбираем текстуру в зависимости от типа корабля
			val texture = Texture(Gdx.files.internal("ship_$size.png"))
			batch.draw(texture, cell.x, cell.y, cell.size.toFloat(), cell.size.toFloat())
		}
	}

	fun dispose() {
		cells.forEach {
			it.dispose()
		}
	}
}