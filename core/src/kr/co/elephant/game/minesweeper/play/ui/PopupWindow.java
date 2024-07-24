package kr.co.elephant.game.minesweeper.play.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class PopupWindow {
    private Stage stage;
    private Window window;
    private boolean isVisible;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private InputProcessor previousInputProcessor;

    public PopupWindow(Skin skin, String title, BitmapFont font) {
        this.stage = new Stage();
        this.font = font;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();

        window = new Window(title, skin);
        window.setSize(600, 800);
        window.setPosition(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 400);
        window.setVisible(false);

        // Add a close button to the window
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        window.add(closeButton).pad(10);
        stage.addActor(window);

       // Gdx.input.setInputProcessor(stage);
    }

    public void show() {
        isVisible = true;
        window.setVisible(true);

        previousInputProcessor = Gdx.input.getInputProcessor();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        if (previousInputProcessor != null) {
            multiplexer.addProcessor(previousInputProcessor);
        }
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void hide() {
        isVisible = false;
        window.setVisible(false);
        Gdx.input.setInputProcessor(previousInputProcessor);
    }


    public void render(float delta) {
        if (isVisible) {
            stage.act(delta);
            stage.draw();
        }
    }

    public void dispose() {
        stage.dispose();
        batch.dispose();
        shapeRenderer.dispose();
    }

    public boolean isVisible() {
        return isVisible;
    }
}
