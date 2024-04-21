package kr.co.elephant.game.minesweeper.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import kr.co.elephant.game.minesweeper.R;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // XML 레이아웃에서 TextView 요소를 가져와서 텍스트를 변경합니다.
        TextView textView = findViewById(R.id.textView);
        textView.setText("This is My Activity");
    }
}