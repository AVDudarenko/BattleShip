package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class BattleShipGame extends ApplicationAdapter {
	GameScreen gameScreen;

	@Override
	public void create() {
		gameScreen = new GameScreen();

	}

	@Override
	public void render() {
		gameScreen.render();
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}
}
