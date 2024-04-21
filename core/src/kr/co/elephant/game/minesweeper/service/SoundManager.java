package kr.co.elephant.game.minesweeper.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import kr.co.elephant.game.minesweeper.common.SettingConfig;

public class SoundManager {

    private static final String BGM = "resource/sound/More-Monkey-Island-Band.mp3";
    private static final String CELL_CLICK = "resource/sound/Footstep_Tile_Right.mp3";

    private static SoundManager instance;
    private Music bgMusic, cellClickMusic ;
    private TextureRegion soundOn, soundOff;
    Image isSoundButton;
    public SoundManager() {
        // 배경음악 플레이
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal(BGM));
        bgMusic.setLooping(true);
        cellClickMusic = Gdx.audio.newMusic(Gdx.files.internal(CELL_CLICK));
        soundOn = new TextureRegion(new Texture(ImageManager.MUSIC_ON));
        soundOff = new TextureRegion(new Texture(ImageManager.MUSIC_OFF));
    }
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public Image setSoundButton(){
        if(SettingConfig.getIsSound()){
            isSoundButton = new Image(soundOn);
        }else{
            isSoundButton = new Image(soundOff);
        }
        return  isSoundButton;
    }
    public void setBGM(){
        if(SettingConfig.getIsSound())  bgMusic.play();
        else bgMusic.pause();
    }
    public void palyBGM(){
        bgMusic.play();
    }
    public void stopBGM(){
        bgMusic.pause();
    }
    public void playCellClick(){
        cellClickMusic.play();
    }
    public void stopCellClick(){
        cellClickMusic.pause();
    }


}
