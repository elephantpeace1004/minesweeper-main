package kr.co.elephant.game.minesweeper.play;

import com.badlogic.gdx.Gdx;

import kr.co.elephant.game.minesweeper.common.CommonConfig;
import kr.co.elephant.game.minesweeper.common.SettingConfig;

public class Play extends Board {

    public enum GameState {        NOT_STARTED, PLAYING, PAUSED, WON, LOST    }
    public GameState gameState, gameStateBeforePause;
    public  int cellsFlagged, cellsOpened, mines, nowLevel;


    public void nextGame(){
        //resetGame();
        Gdx.app.log(CommonConfig.APP_TAG, "nextGame >>>>");
        int level = nowLevel + 1;
        Gdx.app.log(CommonConfig.APP_TAG, "nextGame() level >>>>" + level);
        Gdx.app.log(CommonConfig.APP_TAG, "SettingConfig.boardWidth[level] >>>>" + SettingConfig.boardWidth[level]);
        game.setScreen(new MineSweeperPlay(game, SettingConfig.boardWidth[level], SettingConfig.boardHeight[level], SettingConfig.mines[level], level));
    }
    public void beforeGame(){
        //resetGame();
        Gdx.app.log(CommonConfig.APP_TAG, "nextGame >>>>");
        int level = nowLevel - 1;
        Gdx.app.log(CommonConfig.APP_TAG, "nextGame() level >>>>" + level);
        Gdx.app.log(CommonConfig.APP_TAG, "SettingConfig.boardWidth[level] >>>>" + SettingConfig.boardWidth[level]);
        game.setScreen(new MineSweeperPlay(game, SettingConfig.boardWidth[level], SettingConfig.boardHeight[level], SettingConfig.mines[level], level));
    }

    protected void winGame() {
        gameState = GameState.WON;
        int level = SettingConfig.getLevel();
        Gdx.app.log(CommonConfig.APP_TAG, "winGame() >>>>" + level);
        SettingConfig.setLevel(level + 1);
    }
    public void loseGame() {
        gameState = GameState.LOST;
        // Show all mines on the board
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                if (board[x][y].isMine && !board[x][y].flagged) {
                    board[x][y].texture = cellTexturesAtals.findRegion("cell_mine");
                }
                if (!board[x][y].isMine && board[x][y].flagged) {
                    board[x][y].texture = cellTexturesAtals.findRegion("cell_flag_wrong");
                }
            }
        }
    }



   // CellControl
    public void openCell(int x, int y) {
        if (!board[x][y].opened && !board[x][y].flagged) {
            board[x][y].opened = true;
            if (!board[x][y].isMine) {
                if (board[x][y].surroundingMines > 0) {
                    board[x][y].texture = cellTexturesAtals.findRegion("cell" + board[x][y].surroundingMines);
                } else {
                    // There are no surrounding mines
                    board[x][y].texture = cellTexturesAtals.findRegion("cell_empty");
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            if (x + dx >= 0 && y + dy >= 0 &&
                                    x + dx < boardWidth && y + dy < boardHeight) {
                                openCell(x + dx, y + dy);
                            }
                        }
                    }
                }
                cellsOpened++;
                if (cellsOpened == boardWidth * boardHeight - mines) {
                    winGame();
                }
            } else {
                board[x][y].texture = cellTexturesAtals.findRegion("cell_mine");
                loseGame();
            }
        }
    }

    public void toggleFlagCell(int x, int y) {
        if (!board[x][y].opened) {
            if (!board[x][y].flagged) {
                board[x][y].flagged = true;
                board[x][y].texture = cellTexturesAtals.findRegion("cell_flag_up");
                cellsFlagged++;
            } else {
                board[x][y].flagged = false;
                board[x][y].texture = cellTexturesAtals.findRegion("cell_normal_up");
                cellsFlagged--;
            }
        }
    }

    public void chordCell(int cellX, int cellY) {
        int surroundingFlags = 0;
        int surroundingMines = board[cellX][cellY].surroundingMines;
        for (int dy = -1; dy < 2; dy++) {
            for (int dx = -1; dx < 2; dx++) {
                if (!(dx == 0 && dy == 0)) { // Don't check the chording cell
                    // Make sure the cell we are checking is on the board
                    if (cellX + dx >= 0 && cellY + dy >= 0 &&
                            cellX + dx < boardWidth && cellY + dy < boardHeight) {
                        if (board[cellX + dx][cellY + dy].flagged) {
                            surroundingFlags++;
                        }
                    }
                }
            }
        }

        // If there are the right amount of flags, open the surrounding cells
        if (surroundingFlags == surroundingMines) {
            for (int dy = -1; dy < 2; dy++) {
                for (int dx = -1; dx < 2; dx++) {
                    if (!(dx == 0 && dy == 0)) { // Don't check the chording cell
                        // Make sure the cell we are checking is on the board
                        if (cellX + dx >= 0 && cellY + dy >= 0 &&
                                cellX + dx < boardWidth && cellY + dy < boardHeight) {
                            if (!board[cellX + dx][cellY + dy].flagged) {
                                openCell(cellX + dx, cellY + dy);
                            }
                        }
                    }
                }
            }
        }
    }

}
