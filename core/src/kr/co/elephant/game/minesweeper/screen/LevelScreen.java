package kr.co.elephant.game.minesweeper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import kr.co.elephant.game.minesweeper.play.MineSweeperPlay;
import kr.co.elephant.game.minesweeper.service.AbstractScreen;
import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.common.CommonConfig;
import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.service.ButtonManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public class LevelScreen extends AbstractScreen  {


    int levelNum = 1;
    public LevelScreen(final GameMain game) {
        super(game);
        setupUi();
    }

    @Override
    public void render(float delta) {
        // 매 프레임마다 호출되는 메서드
        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.app.log("AbstractScreen", "render  1111");
        // Stage의 렌더링
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    private void setupUi(){
        int numRows = SettingConfig.boardWidthCount;
        int numCols = SettingConfig.boardHeightCount;
        float columnWidth = 191; // 화면 가로 크기를 열의 수로 나눈 값
        float rowHeight = 191; // 각 행의 높이
        Table table = new Table();
        //table.setFillParent(true); // 화면 크기에 맞추도록 설정
        table.setSize(columnWidth * numCols, rowHeight * numRows);
        //table.debug();
        table.row(); table.add().height(50); table.row();


        levelNum = numRows * numCols;
        final int myLevel = SettingConfig.getLevel() + 1;
        Gdx.app.log("Button Clicked", "myLevel " + myLevel);
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                final int currentLevel = levelNum;


                TextButton button = null;
                Gdx.app.log("Button Clicked", "myLevel " + myLevel + " levelNum " + levelNum);
                if(levelNum < myLevel){

                    button = ButtonManager.createTextButton(levelNum + "", bitmapFont, ImageManager.BLOCK_SQUARE_GREEN,ImageManager.BLOCK_SQUARE_GREEN,"black");
                    button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Gdx.app.log(CommonConfig.APP_TAG, "------------ currentLevel --------------" + currentLevel);
                            game.setScreen(new MineSweeperPlay(game, SettingConfig.boardWidth[currentLevel - 1], SettingConfig.boardHeight[currentLevel - 1], SettingConfig.mines[currentLevel - 1], currentLevel - 1));
                        }
                    });
                }else{
                    if(currentLevel == myLevel){
                        button = ButtonManager.createTextButton(levelNum + "", bitmapFont, ImageManager.BLOCK_SQUARE_PLAY,ImageManager.BLOCK_SQUARE_PLAY,"black");
                    }else{
                        button = ButtonManager.createTextButton(levelNum + "", bitmapFont, ImageManager.BLOCK_SQUARE_GREY,ImageManager.BLOCK_SQUARE_GREY,"black");
                    }

                    button.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            Gdx.app.log(CommonConfig.APP_TAG, "------------ currentLevel --------------" + currentLevel);
                            Gdx.app.log(CommonConfig.APP_TAG, "------------ levelNum --------------" + levelNum);
                            Gdx.app.log(CommonConfig.APP_TAG, "------------ myLevel --------------" + myLevel);
                            if(currentLevel == myLevel){
                                game.setScreen(new MineSweeperPlay(game, SettingConfig.boardWidth[currentLevel - 1], SettingConfig.boardHeight[currentLevel - 1], SettingConfig.mines[currentLevel - 1], currentLevel - 1));
                            }else{
                                Gdx.app.log(CommonConfig.APP_TAG, "------------ cant play --------------" + currentLevel);
                            }
                        }
                    });
                }

                // 테이블에 각 버튼을 추가하고 크기를 설정합니다.
                table.add(button).width(columnWidth).height(rowHeight).pad(5);
                levelNum--;
                // 각 행의 마지막 열에 도달하면 다음 행으로 이동합니다.
                if (col == numCols - 1) {
                    table.row();
                }
            }
        }
        table.row(); table.add().height(50); table.row();

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 160);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // 수직 스크롤만 허용

        // ScrollPane의 크기를 고려하여 화면 하단에 위치시키기
        float scrollPaneX = (Gdx.graphics.getWidth() - scrollPane.getWidth()) / 2f ;
        float scrollPaneY = 160;
        scrollPane.setPosition(scrollPaneX, scrollPaneY);

        stage.addActor(scrollPane);
        // ScrollPane를 제일 아래로 스크롤
        //scrollPane.scrollTo(0, 0, 0, 0);

        int saveLevel = SettingConfig.getLevel();
        int totalH = (int)rowHeight * numRows;
        int targetRow = numRows - (saveLevel) / numCols; // levelNum에 해당하는 행 계산
        float targetY = totalH -  (rowHeight * targetRow); // 해당 행까지의 높이 계산
        scrollPane.scrollTo(0, targetY, 0, 0, true, true);


        TextButton exitButton = ButtonManager.createTextButton("MENU", bitmapFont, ImageManager.YELLOW_BUTTON_04,ImageManager.YELLOW_BUTTON_05,"white");
        exitButton.getLabel().setFontScale(2.0f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Clicked", "Exit Button Clicked");
                game.setScreen(new MainMenuScreen(game));

            }
        });
        Table bottomTable = new Table();
        bottomTable.bottom();
        bottomTable.setFillParent(true);
        bottomTable.add(exitButton).width(Gdx.graphics.getWidth() - 80).height(120).align(Align.center).padBottom(20).padRight(40).padLeft(40);
        stage.addActor(bottomTable);
    }


    @Override
    public void dispose() {

    }
}
