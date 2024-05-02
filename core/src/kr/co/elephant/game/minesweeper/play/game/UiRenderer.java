package kr.co.elephant.game.minesweeper.play.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import kr.co.elephant.game.minesweeper.common.SettingConfig;
import kr.co.elephant.game.minesweeper.play.MineSweeperPlay;
import kr.co.elephant.game.minesweeper.screen.HelpScreen;
import kr.co.elephant.game.minesweeper.screen.LevelScreen;
import kr.co.elephant.game.minesweeper.screen.RankingScreen;
import kr.co.elephant.game.minesweeper.service.ButtonManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;

public class UiRenderer {
    private final Stage stage;
    private final Table table;
    private final Skin skin;

    public UiRenderer() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        skin  = new Skin(Gdx.files.internal(ImageManager.SKIN_UI));

        table = new Table();
    }

    public void init() {
        //Gdx.input.setInputProcessor(stage);

        table.setSize(640, 650);
        table.setPosition((Gdx.graphics.getWidth() - table.getWidth()) / 2, (Gdx.graphics.getHeight() - table.getHeight()) / 2);
        // 메뉴 테이블의 배경을 설정합니다.
        Drawable tableBackground = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.BG_WINDOW_ROUND)));
        table.setBackground(tableBackground);
        addMenuTableAnimation(table);
        stage.addActor(table);
    }

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

    public void render(float delta) {
//        stage.act(delta);
//        stage.draw();
        stage.act(Math.min(delta, 1 / 30f));
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
