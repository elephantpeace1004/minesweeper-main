package kr.co.elephant.game.minesweeper.component;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cell {

    public TextureRegion texture;
    public boolean isMine;
    public boolean opened;
    public boolean flagged;
    public int surroundingMines;

    public Cell(TextureRegion texture) {
        this.texture = texture;
    }

}