package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Ship(val size: Int) {
	val cells = mutableListOf<Cell>()
	var horizontal = true
	private val texture: Texture = Texture(Gdx.files.internal("ship_$size.png"))

	fun placeShip(startCell: Cell, horizontal: Boolean) {
		this.horizontal = horizontal
		for (i in 0 until size) {
			val cell = if (horizontal) {
				Cell(startCell.x + i * startCell.size, startCell.y, startCell.size)
			} else {
				Cell(startCell.x, startCell.y + i * startCell.size, startCell.size)
			}
			cell.isOccupied = true
			cells.add(cell)
		}
	}

	fun draw(batch: SpriteBatch) {
		for (cell in cells) {
			val x = cell.x
			val y = cell.y
			if (horizontal) {
				batch.draw(texture, x, y, cell.size * size.toFloat(), cell.size.toFloat())
			} else {
				batch.draw(texture, x, y, cell.size.toFloat(), cell.size * size.toFloat())
			}
		}
	}

	fun dispose() {
		texture.dispose()
	}
}