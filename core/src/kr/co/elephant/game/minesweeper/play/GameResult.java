package kr.co.elephant.game.minesweeper.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.network.HttpApi;
import kr.co.elephant.game.minesweeper.play.MineSweeperPlay;
import kr.co.elephant.game.minesweeper.screen.HelpScreen;
import kr.co.elephant.game.minesweeper.screen.LevelScreen;
import kr.co.elephant.game.minesweeper.screen.RankingScreen;
import kr.co.elephant.game.minesweeper.service.ButtonManager;
import kr.co.elephant.game.minesweeper.service.FontManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;
import kr.co.elephant.game.minesweeper.service.SoundManager;

public class GameResult {
    private SoundManager soundManager;
    private MineSweeperPlay mineSweeperPlay;
    private String gameTime;


    public GameResult(MineSweeperPlay mineSweeperPlay){
        this.soundManager = SoundManager.getInstance();
        this.mineSweeperPlay = mineSweeperPlay;

    }

    public void show(){
        int nowLevel = mineSweeperPlay.nowLevel;
        int saveLevel = SettingConfig.getLevel();
        soundManager.stopBGM();


        // 반투명 배경과 흰가운데 배경
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        mineSweeperPlay.shapeRendererWhite.setProjectionMatrix(mineSweeperPlay.fixedCamera.combined);
        mineSweeperPlay.shapeRendererWhite.begin(ShapeRenderer.ShapeType.Filled);
        mineSweeperPlay.shapeRendererWhite.setColor(1, 1, 1, 0.5f); // 반투명한 흰색
        mineSweeperPlay.shapeRendererWhite.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mineSweeperPlay.shapeRendererWhite.setColor(1, 1, 1, 1.0f);
        mineSweeperPlay.shapeRendererWhite.rect(100, 550, 520, 400);


        float r = 0x3C / 255f; // 빨간색 채널
        float g = 0x3B / 255f; // 녹색 채널
        float b = 0x3F / 255f; // 파란색 채널

        // 테두리 그리기
        mineSweeperPlay.shapeRendererWhite.setColor(r, g, b, 1.0f); // 테두리 색상 설정 (검은색)
        mineSweeperPlay.shapeRendererWhite.rectLine(100, 550, 100, 950, 5); // 왼쪽 테두리
        mineSweeperPlay.shapeRendererWhite.rectLine(620, 550, 620, 950, 5); // 오른쪽 테두리
        mineSweeperPlay.shapeRendererWhite.rectLine(100, 550, 620, 550, 5); // 위쪽 테두리
        mineSweeperPlay.shapeRendererWhite.rectLine(100, 950, 620, 950, 5); // 아래쪽 테두리

        mineSweeperPlay.shapeRendererWhite.end();


        mineSweeperPlay.batch.begin();
        // 버튼 4개 설정
        mineSweeperPlay.leftButton.draw(mineSweeperPlay.batch, 1);
        mineSweeperPlay.leftButtonBounds = new Rectangle(mineSweeperPlay.leftButton.getX(), mineSweeperPlay.leftButton.getY(), mineSweeperPlay.leftButton.getWidth(), mineSweeperPlay.leftButton.getHeight());
        mineSweeperPlay.replayButton.draw(mineSweeperPlay.batch, 1);
        mineSweeperPlay.replayButtonBounds = new Rectangle(mineSweeperPlay.replayButton.getX(), mineSweeperPlay.replayButton.getY(), mineSweeperPlay.replayButton.getWidth(), mineSweeperPlay.replayButton.getHeight());
        mineSweeperPlay.menusButton.draw(mineSweeperPlay.batch, 1);
        mineSweeperPlay.menuButtonBounds = new Rectangle(mineSweeperPlay.menusButton.getX(), mineSweeperPlay.menusButton.getY(), mineSweeperPlay.menusButton.getWidth(), mineSweeperPlay.menusButton.getHeight());
        mineSweeperPlay.rightButton.draw(mineSweeperPlay.batch, 1);
        mineSweeperPlay.rightButtonBounds = new Rectangle(mineSweeperPlay.rightButton.getX(), mineSweeperPlay.rightButton.getY(), mineSweeperPlay.rightButton.getWidth(), mineSweeperPlay.rightButton.getHeight());

        BitmapFont fontTime = mineSweeperPlay.game.assets.get(FontManager.PEACE_FONT, BitmapFont.class);
        fontTime.setColor(Color.BLACK);
        fontTime.getData().setScale(2);
        GlyphLayout layout = new GlyphLayout();
        gameTime = (int) mineSweeperPlay.gameTime / 60 + ":" + ((int) mineSweeperPlay.gameTime % 60 < 10 ? "0" : "") + (int) mineSweeperPlay.gameTime % 60;
        layout.setText(fontTime, "TIME " +   gameTime);
        Vector2 timeDisplayPosition = new Vector2(720 / 2 - layout.width / 2, 1280 - 500);
        fontTime.draw(mineSweeperPlay.batch, layout,timeDisplayPosition.x, timeDisplayPosition.y);
        fontTime.getData().setScale(1);

        // 현재 레벨에 따른 좌우이동 버튼 활성화
        if(nowLevel > 0){
            mineSweeperPlay.leftButton.setStyle(mineSweeperPlay.bludButtonStyle);
        }else{
            mineSweeperPlay.leftButton.setStyle(mineSweeperPlay.greyButtonStyle);
        }
        if(nowLevel < saveLevel){
            mineSweeperPlay.rightButton.setStyle(mineSweeperPlay.bludButtonStyle);
        }else{
            mineSweeperPlay.rightButton.setStyle(mineSweeperPlay.greyButtonStyle);
        }

        // 성공 실패시 상단 텍스트이미지

        if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.WON) {
            Texture texture = new Texture(Gdx.files.internal(ImageManager.WON));
            TextureRegion won = new TextureRegion(texture);
            mineSweeperPlay.batch.draw(won,720 / 2 - 232 / 2,1280 - 440,232,84);

        } else if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.LOST) {
            Texture texture = new Texture(Gdx.files.internal(ImageManager.LOST));
            TextureRegion youlost = new TextureRegion(texture);
            mineSweeperPlay.batch.draw(youlost,720 / 2 - 400 / 2,1280 - 440, 400, 84);
        }
        mineSweeperPlay.batch.end();

    }

    public void sendTimeApi() {
        if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.WON){
            String memberId = SettingConfig.getMemberId();
            HttpApi.postMemberData(memberId, mineSweeperPlay.nowLevel + 1 + "", gameTime);
        }
//        // 이건 지워야함
//        if (mineSweeperPlay.gameState == MineSweeperPlay.GameState.LOST){
//            String memberId = SettingConfig.getMemberId();
//            HttpApi.postMemberData(memberId, mineSweeperPlay.nowLevel + 1 + "", gameTime);
//        }
    }

}
