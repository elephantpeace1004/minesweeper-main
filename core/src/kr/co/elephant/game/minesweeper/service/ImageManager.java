package kr.co.elephant.game.minesweeper.service;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class ImageManager {

    private static final String ATLAS_PATH = "resource/atlas/";
    private static final String SKIN_PATH = "resource/skin/";
    private static final String IMG_PATH = "resource/img/";

    private static final String IMG_FIGMA_PATH = "resource/figma/";
    private static final String BG_PATH = "resource/bg/";
    private static final String KENNEY_PATH = "resource/kenney_ui_pack/";


    public static final String lineGrey = IMG_FIGMA_PATH + "lineGrey.png";
    public static final String F7EEDB = IMG_FIGMA_PATH + "F7EEDB.png";
    public static final String A6A480 = IMG_FIGMA_PATH + "A6A480.png";
    public static final String help001 = IMG_FIGMA_PATH + "help001.png";



    public static final String YELLOW_BUTTON_04 = IMG_FIGMA_PATH + "menu_button_click2.png";
    public static final String YELLOW_BUTTON_05 = IMG_FIGMA_PATH + "menu_button2.png";
    public static final String BG_WINDOW_ROUND = IMG_FIGMA_PATH + "menu_window2.png";


    // 보드셀 기본회색 이미지로 가져올때 사용해보기
    public static final String CELL_GREY = IMG_FIGMA_PATH + "cell_grey.png";

    public static final String ATLAS_CELL = ATLAS_PATH + "cells/pack.atlas";
    public static final String ATLAS_UI = ATLAS_PATH + "ui/pack.atlas";
    public static final String SKIN_UI = SKIN_PATH + "uiskin.json";



    // public static final String FLAG_RED = IMG_PATH + "flagRed.png";
    public static final String FLAG_RED = IMG_FIGMA_PATH + "menu_boom.png";
    //public static final String HOURGLASS = IMG_PATH + "hourglass.png";
    public static final String HOURGLASS = IMG_FIGMA_PATH + "menu_time.png";


    // 게임 성공 실패시
    public static final String ARROW_LEFT = IMG_PATH + "arrowLeft.png";
    public static final String ARROW_RIGHT = IMG_PATH + "arrowRight.png";
    public static final String RETURN = IMG_PATH + "return.png";
    public static final String MENU_LIST = IMG_PATH + "menuList.png";
    public static final String WON = IMG_PATH + "WON!.png";
    public static final String LOST = IMG_PATH + "YOU LOST!.png";



    public static final String MUSIC_ON = IMG_PATH + "musicOn.png";
    public static final String MUSIC_OFF = IMG_PATH + "musicOff.png";
    public static final String BLOCK_SQUARE_GREEN = IMG_PATH + "block_square.png";
    public static final String BLOCK_SQUARE_PLAY = IMG_PATH + "block_square_play.png";
    public static final String BLOCK_SQUARE_GREY = IMG_PATH + "block_square_grey.png";

    public static final String BOOM = BG_PATH + "boom.png";
    public static final String MINESWEEPER = BG_PATH + "Minesweeper.png";
//    public static final String BG_WINDOW_ROUND = BG_PATH + "windowround.png";


    public static final String BLUE_BUTTON_10 = KENNEY_PATH + "blue_button10.png";
    public static final String BLUE_BUTTON_11 = KENNEY_PATH + "blue_button11.png";
    public static final String GREY_BUTTON_10 = KENNEY_PATH + "grey_button10.png";
    public static final String GREY_BUTTON_11 = KENNEY_PATH + "grey_button11.png";
//    public static final String YELLOW_BUTTON_04 = KENNEY_PATH + "yellow_button04.png";
//    public static final String YELLOW_BUTTON_05 = KENNEY_PATH + "yellow_button05.png";
}
