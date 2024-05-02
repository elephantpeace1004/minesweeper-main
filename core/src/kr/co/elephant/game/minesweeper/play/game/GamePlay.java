package kr.co.elephant.game.minesweeper.play.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.play.game.GameRenderer;
import kr.co.elephant.game.minesweeper.play.game.UiRenderer;
import kr.co.elephant.game.minesweeper.service.AbstractScreen;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public class GamePlay  implements Screen {
    private final GameRenderer gameRenderer;
    private final UiRenderer uiRenderer;

    public GamePlay(GameMain game) {
        gameRenderer = new GameRenderer();
        uiRenderer = new UiRenderer();
    }

    @Override
    public void show() {
        uiRenderer.init();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameRenderer.render(delta);
        uiRenderer.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        gameRenderer.resize(width, height);
        uiRenderer.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        gameRenderer.dispose();
        uiRenderer.dispose();
    }
}
