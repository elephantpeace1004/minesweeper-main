package kr.co.elephant.game.minesweeper.common;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingConfig {

    public static class Level {
        public int numStars;
        public int bestTime;
    }

    public static boolean isSound;
    public static int NUM_MAPS = 100;
    public static int boardWidthCount = 25;
    public static int boardHeightCount = 4;
    public static Level[] arrLevel;
    private final static Preferences pref = Gdx.app.getPreferences("kr.co.elephant.game.minesweeper");
    public static int[] boardWidth = new int[NUM_MAPS+1];
    public static int[] boardHeight = new int[NUM_MAPS+1];
    public static int[] mines = new int[NUM_MAPS+1];

    static {

        for (int level = 0; level <= NUM_MAPS; level++) {
            // 초보자를 위한 난이도: 레벨 0
            if (level == 0) {
                boardWidth[level] = 10;
                boardHeight[level] = 10;
                mines[level] = 10;
            } else {
                // 레벨에 따라 가로와 세로 크기 설정 (최소 10x10, 최대 30x30)
                int a = 10 + (level - 1) / 2;  // 레벨이 1 증가할 때마다 a 값이 1씩 증가
                boardWidth[level] = Math.min(a, 30);
                boardHeight[level] = Math.min(a, 30);

                // 난이도에 따라 지뢰 수 증가 (10%에서 30%까지)
                double percentageIncrease = 0.1 + 0.2 * ((double) level / NUM_MAPS);  // 레벨이 증가함에 따라 10%에서 30%까지 증가

                int minesCount = (int) (boardWidth[level] * boardHeight[level] * percentageIncrease);

                if(mines[level - 1] >= minesCount){
                    mines[level] =  mines[level - 1] + 1;
                }else{
                    mines[level] = minesCount;
                }
            }

            //Gdx.app.log(CommonConfig.APP_TAG, "setting board --------------" + level + " " + mines[level]);
        }
    }


    public static void load() {
        arrLevel = new Level[NUM_MAPS];
        isSound = pref.getBoolean("isSound", true);

        for (int i = 0; i < NUM_MAPS; i++) {
            arrLevel[i] = new Level();
            arrLevel[i].numStars = pref.getInteger("numStars" + i, 0);
            arrLevel[i].bestTime = pref.getInteger("bestTime" + i, 0);
        }
    }

    public static String getMemberId() {        return pref.getString("memberId","");    }
    public static void setMemberId(String memberId) {
        pref.putString("memberId" , memberId);
        pref.flush();
    }

    public static String getToken() {        return pref.getString("token","");    }
    public static void setToken(String token) {
        pref.putString("token" , token);
        pref.flush();
    }

    public static void isSoundSave(boolean is) {
        pref.putBoolean("isSound", is);
        pref.flush();
    }
    public static boolean getIsSound(){
        return pref.getBoolean("isSound", true);
    }

    public static int getLevel(){
        int result = pref.getInteger("level",0);
        return result;
    }

    public static void setLevel(int level) {
        pref.putInteger("level" , level);
        pref.flush();
    }

    public static void levelCompeted(int level, int time) {
        arrLevel[level].numStars = 1;
        arrLevel[level].bestTime = time;
        pref.putInteger("numStars" + level, arrLevel[level].numStars);
        pref.putInteger("bestTime" + level, arrLevel[level].bestTime);
        pref.flush();
    }

    /*
    public static void load() {
        arrLevel = new Level[NUM_MAPS];

        animationWalkIsON = pref.getBoolean("animationWalkIsON", false);

        for (int i = 0; i < NUM_MAPS; i++) {
            arrLevel[i] = new Level();
            arrLevel[i].numStars = pref.getInteger("numStars" + i, 0);
            arrLevel[i].bestMoves = pref.getInteger("bestMoves" + i, 0);
            arrLevel[i].bestTime = pref.getInteger("bestTime" + i, 0);
        }

    }

    public static void save() {

        pref.putBoolean("animationWalkIsON", animationWalkIsON);
        pref.flush();

    }

    public static void levelCompeted(int level, int moves, int time) {

        arrLevel[level].numStars = 1;
        arrLevel[level].bestMoves = moves;
        arrLevel[level].bestTime = time;

        pref.putInteger("numStars" + level, arrLevel[level].numStars);
        pref.putInteger("bestMoves" + level, arrLevel[level].bestMoves);
        pref.putInteger("bestTime" + level, arrLevel[level].bestTime);

        pref.flush();
    }
     */
}
