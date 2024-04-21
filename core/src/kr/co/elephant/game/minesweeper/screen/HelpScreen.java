package kr.co.elephant.game.minesweeper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import kr.co.elephant.game.minesweeper.service.AbstractScreen;
import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.service.ImageManager;
import kr.co.elephant.game.minesweeper.service.ButtonManager;

public  class HelpScreen extends AbstractScreen {


    public HelpScreen(final GameMain game) {
        super(game);
        setupUi();
    }

    private void setupUi() {

        Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, skin.getColor("black"));

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        Table table = new Table();
        table.setFillParent(true);


        Texture separatorTexture = new Texture(Gdx.files.internal(ImageManager.help001));
        TextureRegionDrawable separatorDrawable = new TextureRegionDrawable(new TextureRegion(separatorTexture));
        table.add(new Image(separatorDrawable)).width(Gdx.graphics.getWidth() - 80).pad(10).expandX().row();
        table.add(new Image(separatorDrawable)).width(Gdx.graphics.getWidth() - 80).pad(10).expandX().row();
        table.add(new Image(separatorDrawable)).width(Gdx.graphics.getWidth() - 80).pad(10).expandX().row();


        Label title = new Label("Here is the translation of the instructions for playing Minesweeper:", labelStyle);
        title.setWrap(true);
        table.add(title).width(Gdx.graphics.getWidth() - 80).pad(10).expandX().fillX().row();
        table.add().height(50).expandX().fillX().row();

        // 제목과 설명을 담은 배열
        String[] titlesAndDescriptions = {
                "Click :\n\nClick on a cell on the board to reveal its content. If the clicked cell does not contain a mine, it will show a number indicating how many mines are adjacent to it.",
                "Long Click :\n\nIn some games, on mobile devices, you can long-press a cell suspected of containing a mine to place a flag on it.",
                "Long Click on Flag :\n\nRemoves the flag from the cell.",
                "Click on Number :\n\nIn cases where there are no mines nearby, a feature called \"Auto Open\" or \"Auto Discover\" is usually activated. This feature automatically opens all adjacent empty cells when a cell with no adjacent mines is clicked. It allows players to explore the board more efficiently without the need to click on each adjacent empty cell individually, making gameplay more convenient and faster."
        };

        for (int i = 0; i < titlesAndDescriptions.length; i++) {
            Label descriptionLabel = new Label(titlesAndDescriptions[i], labelStyle);
            descriptionLabel.setWrap(true); // 설명이 길 경우 자동으로 줄 바꿈
            table.add(descriptionLabel).width(Gdx.graphics.getWidth() - 80).pad(0).expandX().fillX().row();
            table.add().height(50).expandX().fillX().row();
        }
        table.padTop(30); // 여백 추가

        mainTable.add(table).top().expandX().fillX().row();

        //stage.addActor(mainTable);

        ScrollPane scrollPane = new ScrollPane(mainTable);
        scrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() + 370);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // 수직 스크롤만 허용
        // ScrollPane의 크기를 고려하여 화면 하단에 위치시키기
        float scrollPaneX = (Gdx.graphics.getWidth() - scrollPane.getWidth()) / 2f ;
        float scrollPaneY = 160;
        scrollPane.setPosition(scrollPaneX, scrollPaneY);

        stage.addActor(scrollPane);



        TextButton exitButton = ButtonManager.createTextButton("MENU", bitmapFont, ImageManager.YELLOW_BUTTON_04,ImageManager.YELLOW_BUTTON_04,"white");
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
    public void render(float delta) {
        Gdx.gl20.glClearColor(1, 1, 1, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().apply();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}