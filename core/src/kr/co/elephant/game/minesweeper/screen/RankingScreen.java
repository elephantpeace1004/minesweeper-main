package kr.co.elephant.game.minesweeper.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.google.gson.Gson;

import java.util.List;

import kr.co.elephant.game.minesweeper.dto.RankDto;
import kr.co.elephant.game.minesweeper.dto.ResponseDto;
import kr.co.elephant.game.minesweeper.network.HttpApi;
import kr.co.elephant.game.minesweeper.network.HttpRequestManager;
import kr.co.elephant.game.minesweeper.service.AbstractScreen;
import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.service.ButtonManager;
import kr.co.elephant.game.minesweeper.service.FontManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public  class RankingScreen extends AbstractScreen {


    private Table table;
    private Table topTable;
    private Table bottomTable;
    private TextButton closeButton;
    // TODO checkbox to enable question marks

    public RankingScreen(final GameMain game) {
        super(game);
        setupUi();

    }

    /**
     * Setup the UI elements.
     */
    private void setupUi() {

        Table table = new Table();
        table.setSize(Gdx.graphics.getWidth(), 140);
        table.setPosition(0, Gdx.graphics.getHeight() - 140);
        // 메뉴 테이블의 배경을 설정합니다.
        Drawable tableBackground = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.A6A480)));
        table.setBackground(tableBackground);
        stage.addActor(table);



        BitmapFont bitmapFont = game.assets.get(FontManager.PEACE_FONT, BitmapFont.class); //new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = bitmapFont;
        labelStyle.fontColor = Color.BLACK; // 원하는 색상으로 변경 가능
        Label label = new Label("Ranking", labelStyle);
        float fontSize = 44f;
        label.setFontScale(fontSize / bitmapFont.getCapHeight());


        topTable = new Table();
        topTable.top();
        topTable.setFillParent(true);
        topTable.add(label).expandX().height(120).align(Align.center).padTop(20);
        stage.addActor(topTable);


        TextButton exitButton = ButtonManager.createTextButton("MENU", bitmapFont, ImageManager.YELLOW_BUTTON_04,ImageManager.YELLOW_BUTTON_05,"white");
        exitButton.getLabel().setFontScale(2.0f);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("Button Clicked", "Exit Button Clicked");
                game.setScreen(new MainMenuScreen(game));

            }
        });
//        bottomTable = new Table();
//        bottomTable.bottom();
//        bottomTable.setFillParent(true);
//        bottomTable.add(exitButton).width(550).height(120).align(Align.center).padBottom(20);
//        stage.addActor(bottomTable);

        Table bottomTable = new Table();
        bottomTable.bottom();
        bottomTable.setFillParent(true);
        bottomTable.add(exitButton).width(Gdx.graphics.getWidth() - 80).height(120).align(Align.center).padBottom(20).padRight(40).padLeft(40);
        stage.addActor(bottomTable);

        HttpRequestManager.sendHttpGetRequest(HttpApi.MEMBER_RANK_URL, new HttpRequestManager.HttpRequestListener() {
            @Override
            public void onSuccess(ResponseDto responseDto) {
                // 요청이 성공적으로 전송되었을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "getRank succ" + responseDto.getData());

                Gson gson = new Gson();
                String jsonString = gson.toJson(responseDto.getData());
                RankDto rankDto = gson.fromJson(jsonString, RankDto.class);
                afterApi(rankDto);
            }
            @Override
            public void onFailure(int statusCode, String responseBody) {
                // 요청이 실패했을 때 처리할 작업
                Gdx.app.log("sendRegisterRequest", "getRank fail" + responseBody);
            }
        });


    }

    private void  afterApi(RankDto rankDto){

        Texture separatorTexture = new Texture(Gdx.files.internal(ImageManager.A6A480));
        TextureRegionDrawable separatorDrawable = new TextureRegionDrawable(new TextureRegion(separatorTexture));

        Label.LabelStyle labelStyle = new Label.LabelStyle(bitmapFont, skin.getColor("black"));
        table = new Table(skin);
        table.top();
        float screenWidth = Gdx.graphics.getWidth();
        table.setWidth(screenWidth);

        Label Label01 = new Label(  "NO" , labelStyle);
        Label Label02 = new Label(  "Nat." , labelStyle);
        Label Label03 = new Label(  "Level" , labelStyle);
        Label Label04 = new Label(  "Time" , labelStyle);
        Label Label05 = new Label(  "Date" , labelStyle);
        table.add(Label01).height(40).left().expandX();
        table.add(Label02).height(40).left().expandX();
        table.add(Label03).height(40).left().expandX();
        table.add(Label04).height(40).left().expandX();
        table.add(Label05).height(40).right().expandX();
        table.row();

        // 라인
        table.add(new Image(separatorDrawable)).colspan(5).height(2).expandX().fillX().padTop(20).padBottom(20).row();



        int i = 1;
        for(RankDto.Content item : rankDto.getContent()){
            Label noLabel = new Label(  "" + i++ , labelStyle);
            Label nameLabel = new Label(item.getMemberId(), labelStyle);
            Label locationLabel = new Label(  item.getMemberLocation() , labelStyle);
            Label levelLabel = new Label(  item.getMemberLevel() , labelStyle);
            Label scoreLabel = new Label( item.getMemberTime(), labelStyle);
            Label dateLabel = new Label(item.getUpdateDate().substring(0,10), labelStyle);

            // 두 번째 행: 네 개의 열로 분할
            table.add(noLabel).height(40).left().expandX();
            table.add(locationLabel).height(40).left().expandX();
            table.add(levelLabel).height(40).left().expandX();
            table.add(scoreLabel).height(40).left().expandX();
            table.add(dateLabel).height(40).right().expandX();
            table.row();

            table.add().left().expandX();
            table.add(nameLabel).colspan(4).expandX().height(40).left().row();

            table.add(new Image(separatorDrawable)).colspan(5).height(2).expandX().fillX().padTop(20).padBottom(20).row();

        }
        table.pad(20,40,10,40);

        ScrollPane scrollPane = new ScrollPane(table);
        scrollPane.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() - 320);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false); // 수직 스크롤만 허용
        // ScrollPane의 크기를 고려하여 화면 하단에 위치시키기
        float scrollPaneX = (Gdx.graphics.getWidth() - scrollPane.getWidth()) / 2f ;
        float scrollPaneY = 160;
        scrollPane.setPosition(scrollPaneX, scrollPaneY);

        stage.addActor(scrollPane);


    }

}
