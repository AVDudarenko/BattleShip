package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ship(val size: Int) {
	private lateinit var cells: Array<Cell>
	private lateinit var texture: Texture // Одна текстура для всех кораблей размера size

	init {
		// Загрузка текстуры один раз при создании объекта Ship
		texture = Texture(Gdx.files.internal("ship_$size.png"))
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
			batch.draw(texture, cell.x, cell.y, cell.size.toFloat(), cell.size.toFloat())
		}
	}

	fun dispose() {
		texture.dispose() // Освобождаем текстуру
		cells.forEach {
			it.dispose() // Освобождаем текстуры ячеек, если это необходимо
		}
	}
}