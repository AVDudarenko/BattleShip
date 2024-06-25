package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import kotlin.random.Random

class GameScreen : InputAdapter() {
    private var stage: Stage = Stage(ScreenViewport())
    private var batch: SpriteBatch = SpriteBatch()
    private var font: BitmapFont = BitmapFont()
    private var skin: Skin
    private var autoButton: TextButton

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

    init {
        Gdx.input.inputProcessor = stage

        // Initialize skin for the button
        skin = Skin(Gdx.files.internal("uiskin.json"))

        // Initialize the Auto button
        val buttonStyle = TextButton.TextButtonStyle()
        buttonStyle.font = font
        autoButton = TextButton("Auto", skin)
        autoButton.setSize(200f, 100f)
		autoButton.setPosition(screenWidth - autoButton.width - 10f, 100f)

        autoButton.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                placeShipsRandomly()
            }
        })

        stage.addActor(autoButton)
    }

    fun placeShipsRandomly() {
        playerShips.clear()
        playerField.flatten().forEach { it.isOccupied = false }
		val sizes = arrayOf(1, 1, 1, 1, 2, 2, 2, 3, 3, 4)
        for (size in sizes) {
            var placed = false
            while (!placed) {
                val x = Random.nextInt(gridSize)
                val y = Random.nextInt(gridSize)
                val horizontal = Random.nextBoolean()
                if (canPlaceShip(x, y, size, horizontal)) {
                    val ship = Ship(size)
                    ship.placeShip(playerField, x, y, horizontal)
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
		// Check surrounding cells
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
        val cell = playerField.flatten().firstOrNull { it.contains(screenX, screenY) }
        cell?.let {
            it.isHit = true
        }
        return true
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
        Gdx.gl.glClearColor(0.53f, 0.81f, 0.92f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()
        draw(batch)
        batch.end()
        stage.draw()
    }

    fun dispose() {
        batch.dispose()
        font.dispose()
        skin.dispose()
        stage.dispose()
        playerField.flatten().forEach {
            it.dispose()
        }
        playerShips.forEach {
            it.dispose()
        }
    }
}