package kr.co.elephant.game.minesweeper.play.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import kr.co.elephant.game.minesweeper.service.ImageManager;

public class GameRenderer {

    private final SpriteBatch spriteBatch;
    private final Texture texture;
    private final Sprite sprite;

    public GameRenderer() {
        spriteBatch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal(ImageManager.BOOM));
        sprite = new Sprite(texture);
        sprite.setPosition(200, 200);
    }

    public void render(float delta) {
        spriteBatch.begin();
        sprite.draw(spriteBatch);
        spriteBatch.end();
    }

    public void resize(int width, int height) {
    }

    public void dispose() {
        spriteBatch.dispose();
        texture.dispose();
    }
}
