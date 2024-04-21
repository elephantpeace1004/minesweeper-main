package kr.co.elephant.game.minesweeper.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import kr.co.elephant.game.minesweeper.common.CommonConfig;
import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.service.SoundManager;

public class GameInputProcessor implements  InputProcessor {
    public boolean isDragging = false;
    public boolean isTouchDown = false;
    public boolean isTouchUp = false;
    private Vector3 touchPos,  screenTouchDownPos;
    private long touchStartTime;
    private Vector2 touchStart1, touchStart2;   // 화면클릭좌표(x,y)
    private float initialDistance;  // 화면클릭좌표간 거리

    private MineSweeperPlay mineSweeperPlay;
    private SoundManager soundManager;


    public int cellX,cellY;

    public GameInputProcessor(MineSweeperPlay mineSweeperPlay) {
        this.mineSweeperPlay = mineSweeperPlay;
        soundManager = SoundManager.getInstance();
        touchStart1 = new Vector2();
        touchStart2 = new Vector2();
        touchPos = new Vector3();
        screenTouchDownPos = new Vector3();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.F2 ||
                (mineSweeperPlay.gameState == MineSweeperPlay.GameState.LOST || mineSweeperPlay.gameState == MineSweeperPlay.GameState.WON)
                        && keycode == Input.Keys.SPACE) {
            mineSweeperPlay.resetGame();
        }
        if (keycode == Input.Keys.ESCAPE) {
            mineSweeperPlay.returnToMenu();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Gdx.app.log(CommonConfig.APP_TAG, "GameInputProcessor touchDown");
        isDragging = false;
        isTouchDown = true;
        isTouchUp = false;
        if (pointer == 0) {
            touchStart1.set(screenX, screenY);
        } else if (pointer == 1) {
            touchStart2.set(screenX, screenY);
            initialDistance = touchStart1.dst(touchStart2);
        }
        Gdx.app.log(CommonConfig.APP_TAG, "------------ touchDown --------------");


        touchStartTime = Gdx.input.getCurrentEventTime();
        touchPos.set(screenX, screenY, 0);
        screenTouchDownPos = touchPos.cpy();
        mineSweeperPlay.gameCamera.unproject(touchPos);

        float gameWidth = CommonConfig.GAME_WIDTH;
        float gameHeight = CommonConfig.GAME_HEIGHT;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float xRatio = screenWidth / gameWidth;
        float yRatio = screenHeight / gameHeight;

        float mouseX = Gdx.input.getX() / xRatio;
        float mouseY = (Gdx.graphics.getHeight() - Gdx.input.getY()) / yRatio;

        // 사운드아이콘 클릭
        if (mineSweeperPlay.soundButtonBounds.contains(mouseX, mouseY)) {
            SettingConfig.isSound = !SettingConfig.isSound;
            SettingConfig.isSoundSave(SettingConfig.isSound);
            soundManager.setSoundButton();
            soundManager.setBGM();
            return true;
        }
        if(mineSweeperPlay.menuSmallButtonBounds.contains(mouseX, mouseY)){
            mineSweeperPlay.loseGame();
            return true;
        }

        // 게임 승패
        int nowLevel = mineSweeperPlay.nowLevel;
        int saveLevel = SettingConfig.getLevel();
        if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.LOST || mineSweeperPlay.gameState == MineSweeperPlay.GameState.WON) {
            if (mineSweeperPlay.replayButtonBounds.contains(mouseX, mouseY)) {
                Gdx.app.log(CommonConfig.APP_TAG, "----------- resetGame");
                mineSweeperPlay.resetGame();
                return true;
            }else if(mineSweeperPlay.menuButtonBounds.contains(mouseX, mouseY)) {
                Gdx.app.log(CommonConfig.APP_TAG, "----------- returnToMenu");
                mineSweeperPlay.returnToMenu();
            }else if(mineSweeperPlay.rightButtonBounds.contains(mouseX, mouseY)){
                Gdx.app.log(CommonConfig.APP_TAG, "----------- rightButtonBounds nextGame ");
               if(nowLevel < saveLevel) mineSweeperPlay.nextGame();
            }else if(mineSweeperPlay.leftButtonBounds.contains(mouseX, mouseY)){
                Gdx.app.log(CommonConfig.APP_TAG, "----------- rightButtonBounds beforeGame ");
                if(nowLevel > 0) mineSweeperPlay.beforeGame();
            } else {
                Gdx.app.log(CommonConfig.APP_TAG, "Button not Clicked!---------");
                return false;
            }
        }
        // 게임중 혹은 게임시작전
        if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.PLAYING || mineSweeperPlay.gameState == MineSweeperPlay.GameState.NOT_STARTED) {
            if (mineSweeperPlay.boardWorldRectangle.contains(touchPos.x, touchPos.y)) {
                int cellX = (int) touchPos.x / mineSweeperPlay.cellSize;
                int cellY = (int) touchPos.y / mineSweeperPlay.cellSize;
                this.cellX = cellX;
                this.cellY = cellY;

                // Make sure that the cell coordinates are on the board
                if ((cellX >= 0 && cellX <= mineSweeperPlay.boardWidth) && (cellY >= 0 && cellY <= mineSweeperPlay.boardHeight)) {
                    if (!mineSweeperPlay.board[cellX][cellY].opened) {
                        // Cell is not yet open
                        mineSweeperPlay.pressingCell = new Vector2(cellX, cellY);
                        if (!mineSweeperPlay.board[cellX][cellY].flagged) {
                            mineSweeperPlay.board[cellX][cellY].texture = mineSweeperPlay.cellTexturesAtals.findRegion("cell_normal_down");
                        } else {
                            // board[cellX][cellY].texture = cellTextures.findRegion("cell_flag_down");
                        }
                    } else {
                        // Cell is already open
                        mineSweeperPlay.chordingCell = new Vector2(cellX, cellY);
                    }
                }
                return true;
            }
        }

        return false;
    }



    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isTouchDown = false;
        isTouchUp = true;

        if(isDragging){
            Gdx.app.log(CommonConfig.APP_TAG, "touchUp return");
            return true;
        }
        Gdx.app.log(CommonConfig.APP_TAG, "touchUp start");
        boolean returnTrue = false;
        touchPos.set(screenX, screenY, 0);
        mineSweeperPlay.gameCamera.unproject(touchPos);
        long elapsedTime = Gdx.input.getCurrentEventTime() - touchStartTime;
        Gdx.app.log(CommonConfig.APP_TAG, "elapsedTime " + elapsedTime);
        if (elapsedTime > 200000000) {
            button = 1; // 롱클릭
        }else{
            button = 0; // 숏클릭
        }
        Gdx.app.log(CommonConfig.APP_TAG, "button " + button);
        if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.PLAYING || mineSweeperPlay.gameState == MineSweeperPlay.GameState.NOT_STARTED) {
            // cellX and cellY represent the boards coordinates of the touched cell
            int cellX = (int) touchPos.x / mineSweeperPlay.cellSize;
            int cellY = (int) touchPos.y / mineSweeperPlay.cellSize;

            // Make sure the cell position is on the board
            if ((cellX >= 0 && cellX < mineSweeperPlay.boardWidth) && (cellY >= 0 && cellY < mineSweeperPlay.boardHeight)) {
                // Pressing cell logic
                if (mineSweeperPlay.pressingCell != null) {
                    if (cellX == mineSweeperPlay.pressingCell.x && cellY == mineSweeperPlay.pressingCell.y && !mineSweeperPlay.panningCamera) {
                        if (button == 0) { // 숏클릭
                            Gdx.app.log(CommonConfig.APP_TAG, "touchUp 숏클릭");
                            if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.NOT_STARTED) {
                                Gdx.app.log(CommonConfig.APP_TAG, "touchUp 숏클릭 NOT_STARTED");
                                mineSweeperPlay.generateMines(mineSweeperPlay.mines, cellX, cellY);
                                mineSweeperPlay.generateCellLabels();
                                mineSweeperPlay.gameState = MineSweeperPlay.GameState.PLAYING;
                            }
                            if (!mineSweeperPlay.board[cellX][cellY].flagged) {
                                Gdx.app.log(CommonConfig.APP_TAG, "touchUp 숏클릭 셀오픈");
                                //soundManager.playCellClick();
                                mineSweeperPlay.openCell(cellX, cellY);
                            } else {
                                Gdx.app.log(CommonConfig.APP_TAG, "touchUp 숏클릭 깃발표시");
                                //soundManager.playCellClick();
                                mineSweeperPlay.board[cellX][cellY].texture = mineSweeperPlay.cellTexturesAtals.findRegion("cell_flag_up");
                            }
                        } else if (button == 1) { // 롱클릭
                            Gdx.app.log(CommonConfig.APP_TAG, "touchUp 롱클릭");
                            if(!isDragging) {
                                //mineSweeperPlay.toggleFlagCell(cellX, cellY);
                               // Gdx.input.vibrate(100);
                            }
                        }
                        returnTrue = true;
                    } else {
                        Gdx.app.log(CommonConfig.APP_TAG, "Dragged off the cell");
                        // Dragged off the cell
                        mineSweeperPlay.board[(int) mineSweeperPlay.pressingCell.x][(int) mineSweeperPlay.pressingCell.y].texture =
                                !mineSweeperPlay.board[(int) mineSweeperPlay.pressingCell.x][(int) mineSweeperPlay.pressingCell.y].flagged ?
                                        mineSweeperPlay.cellTexturesAtals.findRegion("cell_normal_up") :
                                        mineSweeperPlay.cellTexturesAtals.findRegion("cell_flag_up");
                    }
                }
                // Chording cell logic
                if (mineSweeperPlay.chordingCell != null && mineSweeperPlay.gameState == MineSweeperPlay.GameState.PLAYING) {
                    if (cellX == mineSweeperPlay.chordingCell.x && cellY == mineSweeperPlay.chordingCell.y && !mineSweeperPlay.panningCamera) {
                        Gdx.app.log(CommonConfig.APP_TAG, "panningCamera----------chordCell");
                        mineSweeperPlay.chordCell(cellX, cellY);
                    } else {
                        Gdx.app.log(CommonConfig.APP_TAG, "else panningCamera----------");
                        // Dragged off cell
                    }
                }
            }
        }
        mineSweeperPlay.pressingCell = null;
        mineSweeperPlay.chordingCell = null;
        mineSweeperPlay.panningCamera = false;
        return returnTrue;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float distX = Math.abs(screenX - touchStart1.x);
        float distY = Math.abs(screenY - touchStart1.y);
        // 드래그 이동 거리가 일정 값 이상일 때만 드래그로 판단
        if(distX > 10 || distY > 10) {
            isDragging = true;
            if (pointer == 0) {
                // 단일 터치로 이동 구현
                float deltaX = screenX - touchStart1.x;
                float deltaY = screenY - touchStart1.y;
                mineSweeperPlay.gameCamera.position.add(-deltaX * mineSweeperPlay.gameCamera.zoom, deltaY * mineSweeperPlay.gameCamera.zoom, 0);
                touchStart1.set(screenX, screenY);
            } else if (pointer == 1) {
                Vector2 touch2 = new Vector2(screenX, screenY);
                float newDistance = touchStart1.dst(touch2);
                float zoomFactor = initialDistance / newDistance;
                mineSweeperPlay.gameCamera.zoom *= zoomFactor;
                // 카메라 줌 레벨을 제한 (예: 0.1에서 10 사이)
                mineSweeperPlay.gameCamera.zoom = MathUtils.clamp(mineSweeperPlay.gameCamera.zoom, 0.3f, 1.5f);
                initialDistance = newDistance;
            }
//            if (gameCameraTargetZoom < defaultZoom - 0.1f && (Math.abs(screenTouchDownPos.x - screenX) >= 20 ||  Math.abs(screenTouchDownPos.y - screenY) >= 20 || panningCamera)) {
//                panningCamera = true;
//                gameCamera.translate((screenTouchDownPos.x - screenX) / 4f,  -(screenTouchDownPos.y - screenY) / 4f);
//                gameCameraTargetPosition.set(gameCamera.position.cpy());
//                gameCameraTargetPosition.x = MathUtils.clamp(gameCameraTargetPosition.x, boardWorldRectangle.x, boardWorldRectangle.x + boardWorldRectangle.width);
//                gameCameraTargetPosition.y = MathUtils.clamp(gameCameraTargetPosition.y, boardWorldRectangle.y, boardWorldRectangle.y + boardWorldRectangle.height);
//                screenTouchDownPos.set(screenX, screenY, 0);
//                return true;
//            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Gdx.app.log(CommonConfig.APP_TAG, "------------ scrolled --------------" + amountX);
        mineSweeperPlay.gameCameraTargetZoom += amountX / 20F;
        mineSweeperPlay.gameCameraTargetZoom = MathUtils.clamp(mineSweeperPlay.gameCameraTargetZoom, 0.2f, mineSweeperPlay.defaultZoom);
        if (mineSweeperPlay.gameCameraTargetZoom >= mineSweeperPlay.defaultZoom - 0.1f) {
            mineSweeperPlay.gameCameraTargetPosition.set(mineSweeperPlay.boardWidth * mineSweeperPlay.cellSize / 2, mineSweeperPlay.boardHeight * mineSweeperPlay.cellSize / 2, 0);
        }
        return true;
    }
}
