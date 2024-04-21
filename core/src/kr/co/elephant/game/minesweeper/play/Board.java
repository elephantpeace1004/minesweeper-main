package kr.co.elephant.game.minesweeper.play;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import kr.co.elephant.game.minesweeper.component.Cell;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public class Board extends Asset {

    public  Cell[][] board;
    public  int cellSize = 40;
    public  int boardHeight;
    public  int boardWidth;

    protected void createCells(int boardWidth, int boardHeight) {
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                board[x][y] = new Cell(cellTexturesAtals.findRegion("cell_normal_up"));
            }
        }
    }

    public void generateMines(int amount, int initialX, int initialY) {
        for (int m = 0; m < amount; ) {
            int randX = MathUtils.random(boardWidth - 1);
            int randY = MathUtils.random(boardHeight - 1);
            if (!board[randX][randY].isMine && !(
                    (randX >= initialX - 1 && randX <= initialX + 1) &&
                            (randY >= initialY - 1 && randY <= initialY + 1))) {
                // Set as a mine as long as it isn't already a mine and it is
                // not in a 3x3 space around the cell the user clicked.
                board[randX][randY].isMine = true;
                m++;
            }
        }
    }

    public void generateCellLabels() {
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (!board[x][y].isMine) {
                    int surroundingMines = 0;
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (x + dx >= 0 && y + dy >= 0 &&
                                    x + dx < boardWidth && y + dy < boardHeight) {
                                if (board[x + dx][y + dy].isMine) {
                                    surroundingMines++;
                                }
                            }
                        }
                    }
                    board[x][y].surroundingMines = surroundingMines;
                }
            }
        }
    }


}
