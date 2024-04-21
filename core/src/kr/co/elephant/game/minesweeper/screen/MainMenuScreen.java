package kr.co.elephant.game.minesweeper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import kr.co.elephant.game.minesweeper.network.HttpApi;
import kr.co.elephant.game.minesweeper.play.MineSweeperPlay;
import kr.co.elephant.game.minesweeper.service.AbstractScreen;
import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.service.ButtonManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class MainMenuScreen extends AbstractScreen {

    private static final float BUTTON_WIDTH = 550f;
    private static final float BUTTON_HEIGHT = 120f;

    public MainMenuScreen(final GameMain game) {
        super(game);
        createUI();
    }

    private void createUI() {
        // 메뉴 테이블을 생성합니다.
        Table table = createMenuTable();

        // 배경 이미지를 추가합니다.
        Texture backgroundImage = new Texture(Gdx.files.internal(ImageManager.BOOM));
        Image background = new Image(backgroundImage);
        background.setPosition((Gdx.graphics.getWidth() - background.getWidth()) / 2, 300);

        Texture backgroundImage2 = new Texture(Gdx.files.internal(ImageManager.MINESWEEPER));
        Image background2 = new Image(backgroundImage2);
        background2.setPosition((Gdx.graphics.getWidth() - 300), 50);

        // 배경과 메뉴를 스테이지에 추가합니다.
        stage.addActor(background);
        stage.addActor(background2);
        stage.addActor(table);


        String memberId = SettingConfig.getMemberId();
        Gdx.app.log("sendRegisterRequest", "memberId " + memberId);
        if("".equals(memberId)){
            HttpApi.postMemberRegister();
        }else{
            HttpApi.postLogin(memberId);
        }

    }


    // 메뉴 테이블을 생성합니다.
    private Table createMenuTable() {
        Table table = new Table();
        table.setSize(640, 650);
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        // 메뉴 테이블의 배경을 설정합니다.
        Drawable tableBackground = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.BG_WINDOW_ROUND)));
        table.setBackground(tableBackground);

        // 플레이 버튼을 생성합니다.
        TextButton playButton = ButtonManager.createTextButton("PLAY", bitmapFont, ImageManager.YELLOW_BUTTON_05,ImageManager.YELLOW_BUTTON_04,"black");
        playButton.getLabel().setFontScale(2.0f);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                    int level = SettingConfig.getLevel();
                    game.setScreen(new MineSweeperPlay(game, SettingConfig.boardWidth[level], SettingConfig.boardHeight[level], SettingConfig.mines[level], level));
            }
        });
        // 레벨 버튼을 생성합니다.
        TextButton optionsButton = ButtonManager.createTextButton("LEVEL", bitmapFont, ImageManager.YELLOW_BUTTON_05,ImageManager.YELLOW_BUTTON_04,"black");
        optionsButton.getLabel().setFontScale(2.0f);
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelScreen(game));
            }
        });
        // 랭킹 버튼을 생성합니다.
        TextButton exitButton = ButtonManager.createTextButton("RANKING", bitmapFont, ImageManager.YELLOW_BUTTON_05,ImageManager.YELLOW_BUTTON_04,"black");
        exitButton.getLabel().setFontScale(2.0f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RankingScreen(game));
            }
        });
        // 도움말 버튼을 생성합니다.
        TextButton helpButton = ButtonManager.createTextButton("HELP", bitmapFont, ImageManager.YELLOW_BUTTON_05,ImageManager.YELLOW_BUTTON_04,"black");
        helpButton.getLabel().setFontScale(2.0f);
        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HelpScreen(game));
            }
        });

        // 버튼들을 테이블에 추가합니다.
        table.add(playButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).align(Align.left).pad(10);
        table.row();
        table.add(optionsButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).align(Align.left).pad(10);
        table.row();
        table.add(exitButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).align(Align.left).pad(10);
        table.row();
        table.add(helpButton).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).align(Align.left).pad(10);

        // 메뉴 테이블의 초기 위치 및 애니메이션을 설정합니다.
        addMenuTableAnimation(table);
        return table;
    }



    // 메뉴 테이블의 초기 위치 및 애니메이션을 설정합니다.
    private void addMenuTableAnimation(Table table) {
        float duration = 0.5f;
        table.setPosition((stage.getViewport().getWorldWidth() - table.getWidth()) / 2, -table.getHeight());
        table.addAction(Actions.sequence(
                Actions.moveTo(
                        (stage.getViewport().getWorldWidth() - table.getWidth()) / 2,
                        (stage.getViewport().getWorldHeight() - table.getHeight()) / 2 + 200,
                        duration,
                        Interpolation.swingOut
                )
        ));
    }

    @Override
    public void render(float delta) {
        // 화면을 렌더링합니다.
        float r = 0x3C / 255f; // 빨간색 채널
        float g = 0x3B / 255f; // 녹색 채널
        float b = 0x3F / 255f; // 파란색 채널
        // 바탕화면 배경색 지정
        Gdx.gl.glClearColor(r, g, b, 1);
        //Gdx.gl.glClearColor(0.247f, 0.486f, 0.714f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

}
