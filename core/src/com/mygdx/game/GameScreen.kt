package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlin.random.Random

class GameScreen : Stage(ScreenViewport()), Screen,
	InputProcessor {
	private val batch = SpriteBatch()
	private val font = BitmapFont()
	private val skin = Skin(Gdx.files.internal("uiskin.json"))
	private val autoButton: TextButton

	private val gridSize = 10
	private val cellSize = 40f
	private val fieldSize = cellSize * gridSize
	private val screenWidth = Gdx.graphics.width.toFloat()
	private val screenHeight = Gdx.graphics.height.toFloat()
	private val fieldOffsetX = (screenWidth - fieldSize) / 2
	private val fieldOffsetY = (screenHeight - fieldSize) / 2
	private val playerField = Array(gridSize) { x ->
		Array(gridSize) { y ->
			Cell(
				fieldOffsetX + x * cellSize,
				fieldOffsetY + y * cellSize,
				cellSize.toInt()
			)
		}
	}
	private val playerShips = mutableListOf<Ship>()
	private var hitTexture: Texture = Texture(Gdx.files.internal("cell_miss.png"))
	private var cellTexture: Texture = Texture(Gdx.files.internal("cell_empty.png"))

	init {
		Gdx.input.inputProcessor = this
		autoButton = TextButton("Auto", skin)
		autoButton.setSize(200f, 100f)
		autoButton.setPosition(screenWidth - autoButton.width - 10f, 100f)
		autoButton.addListener(object : ClickListener() {
			override fun clicked(event: InputEvent?, x: Float, y: Float) {
				Gdx.app.log("GameScreen", "Auto button clicked at x=$x, y=$y")
				placeShipsRandomly()
				draw()
			}
		})
		placeShipsRandomly()
		addActor(autoButton)
	}

	fun placeShipsRandomly() {
		playerShips.clear()
		playerField.forEach { row ->
			row.forEach { cell ->
				cell.isOccupied = false
				cell.isHit = false
			}
		}

		val sizes = arrayOf(1, 1, 1, 1, 2, 2, 2, 3, 3, 4)
		val random = Random
		for (size in sizes) {
			var placed = false
			while (!placed) {
				val startX = random.nextInt(gridSize)
				val startY = random.nextInt(gridSize)
				val horizontal = random.nextBoolean()
				if (canPlaceShip(startX, startY, size, horizontal)) {
					val ship = Ship(size)
					ship.placeShip(playerField, startX, startY, horizontal)
					playerShips.add(ship)
					placed = true
				}
			}
		}
	}

	private fun canPlaceShip(x: Int, y: Int, size: Int, horizontal: Boolean): Boolean {
		if (horizontal) {
			if (x + size > gridSize) return false
			for (i in 0 until size) {
				if (!isValidPosition(x + i, y)) return false
			}
		} else {
			if (y + size > gridSize) return false
			for (i in 0 until size) {
				if (!isValidPosition(x, y + i)) return false
			}
		}
		return true
	}

	private fun isValidPosition(x: Int, y: Int): Boolean {
		if (x < 0 || x >= gridSize || y < 0 || y >= gridSize) return false
		if (playerField[x][y].isOccupied) return false
		for (dx in -1..1) {
			for (dy in -1..1) {
				val nx = x + dx
				val ny = y + dy
				if (nx in 0 until gridSize && ny in 0 until gridSize && playerField[nx][ny].isOccupied) {
					return false
				}
			}
		}
		return true
	}

	override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
		// Преобразуем координаты экрана в координаты Stage
		val stageCoords = screenToStageCoordinates(Vector2(screenX.toFloat(), screenY.toFloat()))

		// Проверяем клик по кнопке
		val targetActor = hit(stageCoords.x, stageCoords.y, true)
		if (targetActor is TextButton && targetActor == autoButton) {
			// Клик по кнопке Auto
			Gdx.app.log("GameScreen", "Auto button clicked")
			placeShipsRandomly()
			draw()
			return true // Возвращаем true, чтобы событие не продолжалось дальше
		}

		// Проверяем клик по кораблям
		for (ship in playerShips) {
			if (ship.hitCheck(screenX, screenY)) {
				// Обработка попадания по кораблю
				ship.isHit = true // Устанавливаем флаг попадания
				// Дополнительные действия, если нужно
				return true // Возвращаем true, чтобы событие не продолжалось дальше
			}
		}

		// Проверяем клик по ячейке игрового поля
		val cell = playerField.flatten().firstOrNull { it.contains(screenX, screenY) }
		cell?.let {
			it.isHit = true
			return true // Возвращаем true, чтобы событие не продолжалось дальше
		}

		// Если ни по кнопке, ни по кораблю, ни по ячейке не кликнули, возвращаем результат суперкласса
		return super.touchDown(screenX, screenY, pointer, button)
	}

	private fun drawCoordinates() {
		for (i in 0 until gridSize) {
			val numberText = (i + 1).toString()
			font.draw(
				batch,
				numberText,
				fieldOffsetX - cellSize / 2,
				fieldOffsetY + (i + 0.5f) * cellSize + font.capHeight / 2
			)
		}

		for (i in 0 until gridSize) {
			val letterText = ('A' + i).toString()
			font.draw(
				batch,
				letterText,
				fieldOffsetX + (i + 0.5f) * cellSize - font.capHeight / 2,
				fieldOffsetY - cellSize / 2
			)
		}
	}

	override fun show() {}

	override fun render(delta: Float) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		act(delta)
		draw()
	}

	override fun resize(width: Int, height: Int) {}

	override fun pause() {}

	override fun resume() {}

	override fun hide() {}


	override fun draw() {
		batch.begin()

		// Отрисовка игрового поля
		playerField.flatten().forEach { cell ->
			if (cell.isHit) {
				batch.draw(hitTexture, cell.x, cell.y, cell.size.toFloat(), cell.size.toFloat())
			} else {
				batch.draw(cellTexture, cell.x, cell.y, cell.size.toFloat(), cell.size.toFloat())
			}
		}

		// Отрисовка кораблей
		playerShips.forEach { ship ->
			ship.draw(batch)
		}

		drawCoordinates()

		batch.end()

		// Вызываем метод draw() суперкласса для отрисовки дочерних элементов Stage
		super.draw()
	}

	override fun dispose() {
		super.dispose()
		batch.dispose()
		font.dispose()
		skin.dispose()
		hitTexture.dispose()
		cellTexture.dispose()
		playerField.flatten().forEach {
			it.dispose()
		}
		playerShips.forEach {
			it.dispose()
		}
	}
}