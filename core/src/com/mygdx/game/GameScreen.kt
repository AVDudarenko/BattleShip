package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlin.random.Random

class GameScreen : Stage(ScreenViewport()), InputProcessor {
	private val batch = SpriteBatch()
	private val font = BitmapFont()
	private val skin = Skin(Gdx.files.internal("uiskin.json"))
	private val autoButton: TextButton

	private val gridSize = 10
	private val cellSize = 40f // Фиксированный размер клетки в пикселях
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

	private lateinit var shaderBackground: ShaderProgram
	private lateinit var shaderMask: ShaderProgram
	private lateinit var textureBackground: Texture
	private lateinit var textureMask: Texture
	private lateinit var textureShip: Texture

	init {

		// Инициализация шейдеров
//		shaderBackground = ShaderProgram(
//			Gdx.files.internal("shaders/background.vert"),
//			Gdx.files.internal("shaders/background.frag")
//		)
//		shaderMask = ShaderProgram(
//			Gdx.files.internal("shaders/mask.vert"),
//			Gdx.files.internal("shaders/mask.frag")
//		)
//		if (!shaderBackground.isCompiled || !shaderMask.isCompiled) {
//			Gdx.app.error(
//				"Shader",
//				"Shader compilation failed:\n${shaderBackground.log}\n${shaderMask.log}"
//			)
//			Gdx.app.exit()
//		}

		// Загрузка текстур
		textureBackground = Texture(Gdx.files.internal("background.png"))
		textureMask = Texture(Gdx.files.internal("mask.png"))
		textureShip = Texture(Gdx.files.internal("ship.png"))

		// Создание кнопки Auto
		Gdx.input.inputProcessor = this // Устанавливаем GameScreen как InputProcessor
		autoButton = TextButton("Auto", skin)
		autoButton.setSize(200f, 100f)
		autoButton.setPosition(screenWidth - autoButton.width - 10f, 100f)
		autoButton.addListener(object : ClickListener() {
			override fun clicked(event: InputEvent?, x: Float, y: Float) {
				Gdx.app.log("GameScreen", "Auto button clicked at x=$x, y=$y")
				placeShipsRandomly()
			}
		})
		addActor(autoButton)
	}

	fun placeShipsRandomly() {
		playerShips.clear()
		playerField.flatten().forEach { it.isOccupied = false }

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

	fun canPlaceShip(x: Int, y: Int, size: Int, horizontal: Boolean): Boolean {
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

	fun isValidPosition(x: Int, y: Int): Boolean {
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
			return true // Возвращаем true, чтобы событие не продолжалось дальше
		}

		// Проверяем клик по ячейке игрового поля
		val cell = playerField.flatten().firstOrNull { it.contains(screenX, screenY) }
		cell?.let {
			it.isHit = true
			return true // Возвращаем true, чтобы событие не продолжалось дальше
		}

		// Если ни по кнопке, ни по ячейке не кликнули, возвращаем результат суперкласса
		return super.touchDown(screenX, screenY, pointer, button)
	}


	fun draw(batch: SpriteBatch) {
		for (i in 0 until gridSize) {
			for (j in 0 until gridSize) {
				playerField[i][j].draw(batch)
			}
		}
		for (ship in playerShips) {
			ship.draw(batch)
		}

		// Draw coordinates
		for (i in 0 until gridSize) {
			font.draw(
				batch,
				"${i + 1}",
				fieldOffsetX - cellSize / 2,
				fieldOffsetY + (i + 0.5f) * cellSize
			)
			font.draw(
				batch,
				('A' + i).toString(),
				fieldOffsetX + (i + 0.5f) * cellSize,
				fieldOffsetY - cellSize / 2
			)
		}
	}

	fun render() {

//		batch.shader = shaderBackground
//		batch.begin()
//		batch.draw(textureBackground, fieldOffsetX, fieldOffsetY, fieldSize, fieldSize)
//		batch.end()

//		batch.shader = shaderMask
//		shaderMask.setUniformi("u_texture", 0)
//		shaderMask.setUniformi("u_maskTexture", 1)
//		textureShip.bind(0)
//		textureMask.bind(1)

		Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

		batch.begin()
		draw(batch)
		drawShips()
		batch.end()

		act()
		draw()
	}

	private fun drawShips() {
		for (ship in playerShips) {
			ship.draw(batch)
		}
	}

	override fun dispose() {
		batch.dispose()
		font.dispose()
		skin.dispose()
		shaderBackground.dispose()
		shaderMask.dispose()
		textureBackground.dispose()
		textureMask.dispose()
		playerField.flatten().forEach {
			it.dispose()
		}
		playerShips.forEach {
			it.dispose()
		}
	}
}