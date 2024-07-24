package kr.co.elephant.game.minesweeper.play;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import kr.co.elephant.game.minesweeper.GameMain;
import kr.co.elephant.game.minesweeper.common.CommonConfig;
import kr.co.elephant.game.minesweeper.component.Cell;
import kr.co.elephant.game.minesweeper.play.game.UiRenderer;
import kr.co.elephant.game.minesweeper.play.ui.PopupWindow;
import kr.co.elephant.game.minesweeper.screen.MainMenuScreen;
import kr.co.elephant.game.minesweeper.service.ColorManager;
import kr.co.elephant.game.minesweeper.service.FontManager;
import kr.co.elephant.game.minesweeper.service.ImageManager;
import kr.co.elephant.game.minesweeper.service.SoundManager;


public class MineSweeperPlay extends Play implements Screen {

    public  SpriteBatch batch;
    public  ShapeRenderer shapeRenderer;
    public  float gameCameraTargetZoom;
    public  boolean panningCamera;
    public  float defaultZoom;
    public  Rectangle boardWorldRectangle, zoomRectangle;
    public  Vector2 pressingCell, chordingCell;
    public  float gameTime;
    public  GlyphLayout minesLayout, timeLayout, levelLayout;
    public  Vector2 minesDisplayPosition, timeDisplayPosition, levelDisplayPosition;

    public  ShapeRenderer shapeRendererWhite;
    public TextureRegion flagRedIconTextureRegion, hourGlassTextureRegion;
    public Image  menuButton;
    public TextureRegion  menuRegion;
    public Vector3 gameCameraTargetPosition;
    public OrthographicCamera fixedCamera;
    public Rectangle  soundButtonBounds, menuSmallButtonBounds, replayButtonBounds, menuButtonBounds , rightButtonBounds , leftButtonBounds ;
    public OrthographicCamera gameCamera;
    private GameInputProcessor gameInputProcessor;
    private SoundManager soundManager;
    private GameResult gameResult;

    public TextButton replayButton, menusButton, rightButton, leftButton;

    public TextButton.TextButtonStyle bludButtonStyle, greyButtonStyle;

    int initNum = 0;
    long touchStartTime;
    boolean initTouchDown = false;
    boolean actionBoom = false;
    boolean stateSendScore = false;

    private PopupWindow popupWindow;

    public MineSweeperPlay(GameMain game, int boardWidth, int boardHeight, int mines, int nowLevel) {
        gameInputProcessor = new GameInputProcessor(this);
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameInputProcessor);
       // inputMultiplexer.addProcessor(new GestureDetector(0.2f, 0.2f, 0.2f, 0.15f,new GameGestureProcessor(this)));
        Gdx.input.setInputProcessor(inputMultiplexer);
        gameResult = new GameResult(this);
        soundManager = SoundManager.getInstance();
        this.nowLevel = nowLevel;
        init(game,boardWidth,boardHeight,mines);

    }


    private void init(GameMain game, int boardWidth, int boardHeight, int mines){
        this.game = game;
        batch = new SpriteBatch();
        initAsset();
        initBoard(boardWidth, boardHeight, mines);
        initStyle();
        initUI();
        initValue();
        initCamera();

        popupWindow = new PopupWindow(skin, "Pause Menu", game.assets.get(FontManager.PEACE_FONT, BitmapFont.class));
        popupWindow.show();
        popupWindow.hide();
    }

    public void openPopupWindow(){
        Gdx.app.log(CommonConfig.APP_TAG, "---- openPopupWindow show");
        popupWindow.show();
    }

    @Override
    public void render(float delta) {

        Gdx.gl20.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        inRenderLongClickEvent();

        if (gameState == GameState.PLAYING) {
            gameTime += delta;
        }

        if(initNum < 100) interpolateCamera(delta);
        initNum++;
        gameCamera.update();
        fixedCamera.update();

        // ------ gameCamera 배치(batch)에 카메라의 투영 행렬(projection matrix)을 설정합니다.
        batch.setProjectionMatrix(gameCamera.combined);
        batch.begin();
        for (int y = 0; y < boardHeight; y++) {
            for (int x = 0; x < boardWidth; x++) {
                batch.draw(board[x][y].texture, x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
        batch.end();
        // ------ fixedCamera
        renderSetTopMenu();
        if (gameState == GameState.WON || gameState == GameState.LOST) {
            gameResult.show();
            if(!stateSendScore) {
                gameResult.sendTimeApi();
                stateSendScore = true;
            }
        }else{
            stateSendScore = false;
        }

        // 팝업윈도우
        if (popupWindow.isVisible()) {
            popupWindow.show();
        }else{
            popupWindow.hide();
        }
        popupWindow.render(delta);


    }


    private void initCamera(){
        // [게임화면 720 * 1280] 카메라 및 도형 렌더링에 사용되는 변수 초기화
        gameCamera = this.game.gameCamera; // 게임의 주 카메라
        fixedCamera = this.game.fixedCamera; // 고정 카메라 : 메뉴등
        gameCameraTargetPosition = new Vector3(); // 카메라의 목표 위치

        // ShapeRenderer는 도형을 그리는 데 사용되는 렌더러이고, Rectangle은 특정 위치와 크기를 가지는 직사각형 영역을 나타내는 클래스입니다.
        shapeRenderer = this.game.shapeRenderer; // 도형 렌더러
        shapeRendererWhite = new ShapeRenderer(); // 흰색 도형 렌더러

        // [보드사각화면 400 * 400 (CELL 10*10)] 게임 보드의 세계적인 크기를 나타내는 직사각형 초기화 : cellSize = 40
        boardWorldRectangle = new Rectangle(0, 0, boardWidth * cellSize, boardHeight * cellSize);
        // [줌사각화면 400 * 400 (CELL 10*10)] 화면에 보이는 게임 보드의 영역을 나타내는 직사각형 초기화
        zoomRectangle = new Rectangle(0, 0, boardWidth * cellSize, boardHeight * cellSize);

        // [줌사각화면 400 * 711 (CELL 10*10)] 디바이스 해상도에 맞춰 직사각형을 9:16 비율로 설정
        if (zoomRectangle.getAspectRatio() >= (9F / 16F)) {
            zoomRectangle.height = (16F / 9F) * zoomRectangle.width;
        } else {
            zoomRectangle.width = (9F / 16F) * zoomRectangle.height;
        }

        // [-100.0 / -255.55557] 줌직사각형을 보드 중심에 맞춤
        zoomRectangle.x = -((zoomRectangle.width - (boardWidth * cellSize / 2)) / 2);
        zoomRectangle.y = -((zoomRectangle.height - (boardHeight * cellSize / 2)) / 2);

        // [200 , 200 , 0] 카메라의 초기 위치 설정
        gameCameraTargetPosition.set(boardWidth * cellSize / 2 , boardHeight * cellSize / 2 , 0);
        gameCamera.position.set(gameCameraTargetPosition.cpy()); // 카메라 위치 설정
        gameCameraTargetZoom = zoomRectangle.width / CommonConfig.GAME_WIDTH; // 카메라 줌 설정
        defaultZoom = gameCameraTargetZoom;
        gameCamera.zoom = gameCameraTargetZoom; // 카메라 줌 적용 gameCameraTargetZoom vs 1f 주면 확대하면서 시작한다.


        //gameCamera.position.set(0,0,0);

        Gdx.app.log(CommonConfig.APP_TAG, "---- boardWidth * cellSize / boardHeight * cellSize " + boardWidth * cellSize + " / " + boardHeight * cellSize);
        Gdx.app.log(CommonConfig.APP_TAG, "---- zoomRectangle.width/height " + zoomRectangle.width + " / " + zoomRectangle.height);
        Gdx.app.log(CommonConfig.APP_TAG, "---- zoomRectangle.x/y " + zoomRectangle.x + " / " + zoomRectangle.y);
        Gdx.app.log(CommonConfig.APP_TAG, "---- gameCameraTargetPosition x,y,z  " + boardWidth * cellSize / 2 + " / " + boardHeight * cellSize / 2);
        Gdx.app.log(CommonConfig.APP_TAG, "---- gameCamera.viewportWidth " + gameCamera.viewportWidth);
        Gdx.app.log(CommonConfig.APP_TAG, "---- gameCamera.viewportHeight " + gameCamera.viewportHeight);
        Gdx.app.log(CommonConfig.APP_TAG, "---- zoomRectangle.width " + zoomRectangle.width);
        Gdx.app.log(CommonConfig.APP_TAG, "---- zoomRectangle.height " + zoomRectangle.height);
        Gdx.app.log(CommonConfig.APP_TAG, "---- CommonConfig.GAME_WIDTH " + CommonConfig.GAME_WIDTH);
        Gdx.app.log(CommonConfig.APP_TAG, "---- gameCameraTargetZoom " + gameCameraTargetZoom);
        Gdx.app.log(CommonConfig.APP_TAG, "---- gameCameraTargetPosition.x " + gameCameraTargetPosition.x);
        Gdx.app.log(CommonConfig.APP_TAG, "---- gameCameraTargetPosition.y " + gameCameraTargetPosition.y);
    }

    // 게임시작할때마다 보드판 이동
    private void interpolateCamera(float delta) {
        gameCamera.position.x += (gameCameraTargetPosition.x - gameCamera.position.x) * 20 * delta;
        gameCamera.position.y += (gameCameraTargetPosition.y - gameCamera.position.y) * 20 * delta;
        // gameCamera.zoom 값이 목표로 하는 gameCameraTargetZoom 값으로 부드럽게 이동하게 됩니다.(1.0 에서 0.5로)
        gameCamera.zoom += (gameCameraTargetZoom - gameCamera.zoom) * 20 * delta;
        Gdx.app.log(CommonConfig.APP_TAG, "x " + gameCamera.position.x + "y " + gameCamera.position.y + "zoom " + gameCamera.zoom);
    }

    // 롱클릭시 깃발표시
    private void inRenderLongClickEvent(){
        // 롱클릭때문에 이고생을 시작
        if(gameInputProcessor.isTouchDown && !initTouchDown){
            initTouchDown = true;
            touchStartTime = Gdx.input.getCurrentEventTime();
        }else if(gameInputProcessor.isTouchDown && initTouchDown  && !actionBoom &&  !gameInputProcessor.isDragging) {
            long elapsedTime = Gdx.input.getCurrentEventTime() - touchStartTime;
            if (elapsedTime >= 150000000) { // 1.5초 이상이면 롱클릭으로 처리
                // 롱클릭 처리하는 코드 작성
                Gdx.app.log(CommonConfig.APP_TAG, "------------ 롱클릭 처리하는 코드 작성 --------------elapsedTime " + elapsedTime);
                if ((gameInputProcessor.cellX >= 0 && gameInputProcessor.cellX < boardWidth) && (gameInputProcessor.cellY >= 0 && gameInputProcessor.cellY < boardHeight)) {
                    // Pressing cell logic
                    if (pressingCell != null) {
                        if (gameInputProcessor.cellX == pressingCell.x && gameInputProcessor.cellY == pressingCell.y && !panningCamera) {
                            if (gameState == MineSweeperPlay.GameState.PLAYING || gameState == MineSweeperPlay.GameState.NOT_STARTED) {
                                toggleFlagCell(gameInputProcessor.cellX, gameInputProcessor.cellY);
                                Gdx.input.vibrate(100);
                            }
                        }
                    }
                }
                initTouchDown = false;
                actionBoom = true;
            }
        }else if(!gameInputProcessor.isTouchDown){
            initTouchDown = false;
            actionBoom = false;
        }
        // 롱클릭때문에 이고생을 끝내다.
    }



    private void initValue(){
        gameState = GameState.NOT_STARTED;
        gameTime = 0;
    }
   private void initBoard(int boardWidth, int boardHeight, int mines){
       // 지뢰찾기보드셋팅
       this.boardWidth = boardWidth;
       this.boardHeight = boardHeight;
       this.mines = mines;

       board = new Cell[this.boardWidth][this.boardHeight];
       createCells(this.boardWidth, this.boardHeight);
       pressingCell = null;
       chordingCell = null;
       cellsFlagged = 0;
       cellsOpened = 0;

   }
    private void initStyle(){

        bludButtonStyle = new TextButton.TextButtonStyle();
        bludButtonStyle.font = game.assets.get(FontManager.PEACE_FONT, BitmapFont.class);
        bludButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.BLUE_BUTTON_10)));
        bludButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.BLUE_BUTTON_11)));

        greyButtonStyle = new TextButton.TextButtonStyle();
        greyButtonStyle.font = game.assets.get(FontManager.PEACE_FONT, BitmapFont.class);
        greyButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.GREY_BUTTON_10)));
        greyButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(ImageManager.GREY_BUTTON_11)));
    }

    private void initUI(){
        // 상단 사운드
        soundManager.setBGM();

        minesLayout = new GlyphLayout();
        timeLayout = new GlyphLayout();
        levelLayout = new GlyphLayout();
        minesDisplayPosition = new Vector2(58, CommonConfig.GAME_HEIGHT - 24);
        timeDisplayPosition = new Vector2(CommonConfig.GAME_WIDTH - 24, CommonConfig.GAME_HEIGHT - 24);
        levelDisplayPosition= new Vector2(CommonConfig.GAME_WIDTH / 2, CommonConfig.GAME_HEIGHT - 24);

        flagRedIconTextureRegion = new TextureRegion(new Texture(ImageManager.FLAG_RED));// 상단 플레그 아이콘
        hourGlassTextureRegion = new TextureRegion(new Texture(ImageManager.HOURGLASS)); // 상단 시계 아이콘
        menuRegion = new TextureRegion(new Texture(ImageManager.MENU_LIST));  // 상단 메뉴 아이콘
        menuButton = new Image(menuRegion);
        menuButton.setPosition(650, CommonConfig.GAME_HEIGHT - 60);
        menuButton.setSize(50, 50); // 아이콘 크기 조절

        // 게임결과화면 버튼
        leftButton = createTextButton(bludButtonStyle, ImageManager.ARROW_LEFT,-165);
        replayButton = createTextButton(bludButtonStyle, ImageManager.RETURN,-55);
        menusButton = createTextButton(bludButtonStyle, ImageManager.MENU_LIST,55);
        rightButton = createTextButton(bludButtonStyle, ImageManager.ARROW_RIGHT, 165);

    }

    private TextButton createTextButton(TextButton.TextButtonStyle style, String imagePath, int addMargin) {
        TextButton button = new TextButton("", style);
        Image iconImage = new Image(new Texture(Gdx.files.internal(imagePath)));
        button.add(iconImage).pad(10);
        button.getLabel().setFontScale(2.0f);
        button.setWidth(100);
        button.setHeight(100);
        float rightButtonX = CommonConfig.GAME_WIDTH / 2 - button.getWidth() / 2 + addMargin;
        float rightButtonY = CommonConfig.GAME_HEIGHT / 2 - button.getHeight() / 2;
        button.setPosition(rightButtonX,rightButtonY);
        return button;
    }


    private void renderSetTopMenu(){

        // Draw rectangles behind the mines counter and timer
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(fixedCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // A6A480 ADAB9E

        float r = 0xA6 / 255f; // 빨간색 채널
        float g = 0xA4 / 255f; // 녹색 채널
        float b = 0x80 / 255f; // 파란색 채널
        // 바탕화면 배경색 지정
        shapeRenderer.setColor(r, g, b, 1);
        //shapeRenderer.setColor(0.2f, 0.2f, 0.5f, 0.75f);
        shapeRenderer.rect(0, CommonConfig.GAME_HEIGHT,CommonConfig.GAME_WIDTH, -70);
        shapeRenderer.end();

        Gdx.gl20.glDisable(GL20.GL_BLEND);
        batch.setProjectionMatrix(fixedCamera.combined);
        batch.begin();
        font.setColor(Color.WHITE);
        // 깃발(지뢰)아이콘
        batch.draw(flagRedIconTextureRegion, 24, CommonConfig.GAME_HEIGHT - 48 , 26f, 26f);
        // 지뢰남은갯수
        font.setColor(Color.BLACK);
        minesLayout.setText(font, ((mines - cellsFlagged) < 100 ? "0" : "") + ((mines - cellsFlagged) < 10 ? "0" : "") + (mines - cellsFlagged));
        font.draw(batch, minesLayout,minesDisplayPosition.x, minesDisplayPosition.y);
        // 시계아이콘
        batch.draw(hourGlassTextureRegion, 130, CommonConfig.GAME_HEIGHT - 48 , 26f, 26f);
        // 플레이시간
        font.setColor(Color.BLACK);
        timeLayout.setText(font, (int) gameTime / 60 + ":" + ((int) gameTime % 60 < 10 ? "0" : "") + (int) gameTime % 60);
        font.draw(batch, timeLayout,160, timeDisplayPosition.y);
        // 레벨표시
        font.setColor(ColorManager.hexToColor("534B38")); // DA3D34
        levelLayout.setText(font,"LEVEL  " + (nowLevel + 1) + " (" + boardWidth +"*"+ boardHeight + ")" );
        font.draw(batch, levelLayout,270, levelDisplayPosition.y);
        // 사운드아이콘
        Image isSoundButton = soundManager.setSoundButton();
        isSoundButton.setPosition(590, CommonConfig.GAME_HEIGHT - 58);
        isSoundButton.setSize(50, 50);
        isSoundButton.draw(batch,1);
        soundButtonBounds = new Rectangle(isSoundButton.getX(), isSoundButton.getY(), isSoundButton.getImageWidth(), isSoundButton.getImageHeight());
        // 메뉴아이콘
        menuButton.draw(batch,1);
        menuSmallButtonBounds = new Rectangle(menuButton.getX(), menuButton.getY(), menuButton.getImageWidth(), menuButton.getImageHeight());
        batch.end();
    }

    public  void resetGame() {
        createCells(boardWidth, boardHeight);
        cellsFlagged = 0;
        cellsOpened = 0;
        gameState = GameState.NOT_STARTED;
        gameTime = 0;
        pressingCell = null;
        chordingCell = null;
        soundManager.setBGM();

        initializeCamera();
    }
    private void initializeCamera() {
        gameCameraTargetPosition.set(boardWidth * cellSize / 2 , boardHeight * cellSize / 2 , 0);
        gameCamera.position.set(gameCameraTargetPosition.cpy());
        //gameCamera.zoom = 1.0f;
        gameCamera.zoom = gameCameraTargetZoom;
        gameCamera.update();
    }
    public  void returnToMenu() {
        dispose();
        game.setScreen(new MainMenuScreen(game));
    }
    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }
    @Override
    public void pause() {
        gameStateBeforePause = gameState;
        gameState = GameState.PAUSED;
        soundManager.stopBGM();
    }

    @Override
    public void resume() {
        gameState = gameStateBeforePause;
        soundManager.setBGM();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        soundManager.stopBGM();
    }

}

