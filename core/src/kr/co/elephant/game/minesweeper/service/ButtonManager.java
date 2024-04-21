package kr.co.elephant.game.minesweeper.service;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ButtonManager {

    public static TextButton.TextButtonStyle textButtonStyle;

    public static TextButton createTextButton(String name, BitmapFont bitmapFont,String upImage, String downImage, String fontColor){
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = bitmapFont;
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture(upImage)));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture(downImage)));

        switch (fontColor){
            case  "black" :
                style.fontColor = new Color(0, 0, 0, 1);
                break;
            case  "white" :
            default : style.fontColor = new Color(1, 1, 1, 1);
        }
        return  new TextButton(name, style);
    }

}
