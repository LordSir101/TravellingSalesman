package net.ontariotechu.travellingsalesman;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make app fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point); //point contains size of screen

        //initialize view
        gameView = new GameView(this, point.x, point.y);

        //display view
        setContentView(gameView);
    }


    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}
