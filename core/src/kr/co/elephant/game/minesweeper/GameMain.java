package kr.co.elephant.game.minesweeper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import kr.co.elephant.game.minesweeper.common.CommonConfig;
import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.screen.MainMenuScreen;
import kr.co.elephant.game.minesweeper.service.FontManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public class GameMain extends Game  {
    public OrthographicCamera gameCamera, fixedCamera;
    public ScreenViewport viewport;
    public ShapeRenderer shapeRenderer;
    public AssetManager assets;
    private boolean finishedLoadingAssets;

    @Override
    public void create() {
        // 게임이 처음 실행될 때 호출되는 부분
        //SettingConfig.setLevel(56);

        // Create asset manager and load assets.
        assets = new AssetManager();
        loadAssets();
        assets.finishLoading();
        onFinishLoadingAssets();

        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, CommonConfig.GAME_WIDTH, CommonConfig.GAME_HEIGHT);
        fixedCamera = new OrthographicCamera();
        fixedCamera.setToOrtho(false, CommonConfig.GAME_WIDTH, CommonConfig.GAME_HEIGHT);
        viewport = new ScreenViewport(gameCamera);
        shapeRenderer = new ShapeRenderer();

        SettingConfig.load();
        setScreen(new MainMenuScreen(this));
    }
    private void loadAssets() {
        assets.load(ImageManager.ATLAS_CELL, TextureAtlas.class);
        assets.load(ImageManager.ATLAS_UI, TextureAtlas.class);
        assets.load(ImageManager.SKIN_UI, Skin.class);
        assets.load(FontManager.ARIAL_32_FONT, BitmapFont.class);
        assets.load(FontManager.CURRENT_REGULAR_FONT, BitmapFont.class);
        assets.load(FontManager.PEACE_FONT, BitmapFont.class);
    }
    private void onFinishLoadingAssets() {
        finishedLoadingAssets = true;
    }

    @Override
    public void dispose() {
        // 게임이 종료될 때 호출되는 부분
        super.dispose();
    }



}
