package kr.co.elephant.game.minesweeper.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.service.AbstractScreen;
import kr.co.elephant.game.minesweeper.service.FontManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public class Asset {

    public GameMain game;
    public TextureAtlas uiTexturesAtals;
    public TextureAtlas cellTexturesAtals;
    public BitmapFont font;
    public Skin skin;

    protected void initAsset(){
        // Get assets from game AssetManager.
        cellTexturesAtals = game.assets.get(ImageManager.ATLAS_CELL, TextureAtlas.class);
        uiTexturesAtals = game.assets.get(ImageManager.ATLAS_UI, TextureAtlas.class);
        font = game.assets.get(FontManager.PEACE_FONT, BitmapFont.class);
        skin = new Skin(Gdx.files.internal(ImageManager.SKIN_UI));
    }


}
