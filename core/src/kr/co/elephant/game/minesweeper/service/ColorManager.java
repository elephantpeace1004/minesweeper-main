package kr.co.elephant.game.minesweeper.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class ColorManager {

    public static Color hexToColor(String hexColor) {
        int r = Integer.parseInt(hexColor.substring(0, 2), 16);
        int g = Integer.parseInt(hexColor.substring(2, 4), 16);
        int b = Integer.parseInt(hexColor.substring(4, 6), 16);

        float rFloat = r / 255f;
        float gFloat = g / 255f;
        float bFloat = b / 255f;

        return new Color(rFloat, gFloat, bFloat, 1);
    }

    public static void setClearColorFromHex(String hexColor) {
        // 16진수 RGB 값을 10진수로 변환
        int r = Integer.parseInt(hexColor.substring(0, 2), 16);
        int g = Integer.parseInt(hexColor.substring(2, 4), 16);
        int b = Integer.parseInt(hexColor.substring(4, 6), 16);

        // 0에서 1 사이의 부동 소수점 값으로 변환
        float rFloat = r / 255f;
        float gFloat = g / 255f;
        float bFloat = b / 255f;

        // OpenGL의 glClearColor 함수 호출
        Gdx.gl.glClearColor(rFloat, gFloat, bFloat, 1);
    }

}
