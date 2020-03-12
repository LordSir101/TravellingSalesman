package net.ontariotechu.travelingsalesman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.view.SurfaceView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private static int screenX, screenY;
    public static float screenRatioX, screenRatioY;
    private boolean isPlaying = true;
    private Thread thread;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.screenX = screenX;
        this.screenY = screenY;

        //adjust size of sprites based on screen size using an arbitrary size as a baseline
        screenRatioX = screenX / 1920f;
        screenRatioY = screenY / 1080f ;


    }

    @Override
    public void run() {

        while(isPlaying){
            update();
            draw();
            sleep();
        }
    }

    public void update(){

    }

    public void draw(){

    }

    public void sleep(){
        //1000 millis / 17 millis = 60fps
        try {
            Thread.sleep(17);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){

        isPlaying = true;
        thread = new Thread(this);
        thread.start(); //calls run method
    }

    public void pause(){

        try {
            isPlaying = false;
            thread.join();//stops the thread
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


