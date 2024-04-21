package kr.co.elephant.game.minesweeper.service;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import kr.co.elephant.game.minesweeper.GameMain;

/**
 * AbstractScreen은 Screen 인터페이스를 구현하고 있는 추상 클래스입니다.
 * 모든 화면 클래스들이 이 클래스를 상속하여 구현합니다.
 */
public abstract class AbstractScreen implements Screen {

    protected final GameMain game;
    protected Stage stage;
    protected Skin skin;
    protected BitmapFont bitmapFont;
    protected SpriteBatch batch;

    public AbstractScreen(GameMain game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        this.skin = new Skin(Gdx.files.internal(ImageManager.SKIN_UI));
        this.bitmapFont = game.assets.get(FontManager.PEACE_FONT, BitmapFont.class);
        this.batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage); // 입력 이벤트를 Stage로 전달하기 위해 설정
    }

    @Override
    public void show() {
        // 화면이 처음으로 나타날 때 호출되는 메서드
    }

    @Override
    public void render(float delta) {
        // 매 프레임마다 호출되는 메서드
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Stage의 렌더링
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // 화면 크기가 변경될 때 호출되는 메서드
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // 게임이 일시 중지될 때 호출되는 메서드
    }

    @Override
    public void resume() {
        // 게임이 다시 시작될 때 호출되는 메서드
    }

    @Override
    public void hide() {
        // 다른 화면으로 전환될 때 호출되는 메서드
    }

    @Override
    public void dispose() {
        // 화면이 파괴될 때 호출되는 메서드
        stage.dispose();
        skin.dispose();
        bitmapFont.dispose();
        batch.dispose();
        game.dispose();
    }
}
