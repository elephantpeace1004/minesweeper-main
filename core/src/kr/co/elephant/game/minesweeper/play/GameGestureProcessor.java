package kr.co.elephant.game.minesweeper.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import kr.co.elephant.game.minesweeper.common.CommonConfig;
import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.service.SoundManager;

public class GameGestureProcessor implements GestureDetector.GestureListener {

    private boolean isDragging = false;
    private Vector3 touchPos,  screenTouchDownPos;
    private long touchStartTime;
    private Vector2 touchStart1, touchStart2;   // 화면클릭좌표(x,y)
    private float initialDistance;  // 화면클릭좌표간 거리

    private MineSweeperPlay mineSweeperPlay;
    private SoundManager soundManager;



    public GameGestureProcessor(){}

    public GameGestureProcessor(MineSweeperPlay mineSweeperPlay) {
        this.mineSweeperPlay = mineSweeperPlay;
        soundManager = SoundManager.getInstance();
        touchStart1 = new Vector2();
        touchStart2 = new Vector2();
        touchPos = new Vector3();
        screenTouchDownPos = new Vector3();
    }

    // 화면에 손가락이 닿았을 때 호출됨
    @Override
    public boolean touchDown(float screenX, float screenY, int pointer, int button) {
        Gdx.app.log(CommonConfig.APP_TAG, "^^^^^ GameGestureProcessor touchDown");

        return false;
    }

    // 화면을 터치한 후 짧게 눌렀다 뗄 때 호출됨
    @Override
    public boolean tap(float screenX, float screenY, int count, int button) {
        Gdx.app.log(CommonConfig.APP_TAG, "^^^^^ GameGestureProcessor tap");
        return false;
    }

    // 화면을 길게 눌렀을 때 호출됨
    @Override
    public boolean longPress(float screenX, float screenY) {
        Gdx.app.log(CommonConfig.APP_TAG, "^^^^^ GameGestureProcessor longPress");
        return false;
    }

    // 화면을 터치하고 빠르게 드래그할 때 호출됨
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        //Gdx.app.log(CommonConfig.APP_TAG, "GameGestureProcessor fling");

        return false;
    }

    // 화면을 드래그할 때 호출됨
    @Override
    public boolean pan(float screenX, float screenY, float deltaX, float deltaY) {
        //Gdx.app.log(CommonConfig.APP_TAG, "GameGestureProcessor pan");

        return false;
    }

    // 화면을 드래그한 후 손가락을 뗄 때 호출됨
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        //Gdx.app.log(CommonConfig.APP_TAG, "GameGestureProcessor panStop");
        return false;
    }

    // 화면을 줌(확대 또는 축소)할 때 호출됨
    @Override
    public boolean zoom(float originalDistance, float currentDistance) {
        //Gdx.app.log(CommonConfig.APP_TAG, "GameGestureProcessor zoom");
        return false;
    }

    // 핀치 동작을 수행할 때 호출됨
    @Override
    public boolean pinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer) {
        //Gdx.app.log(CommonConfig.APP_TAG, "GameGestureProcessor pinch");
        return false;
    }

    // 핀치 동작을 멈출 때 호출됨
    @Override
    public void pinchStop() {
       // Gdx.app.log(CommonConfig.APP_TAG, "GameGestureProcessor pinchStop");
    }
}

