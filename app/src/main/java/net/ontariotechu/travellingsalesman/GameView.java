package net.ontariotechu.travellingsalesman;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private static int screenX, screenY, nodes;
    public static float screenRatioX, screenRatioY;
    private boolean isPlaying = true;
    private Thread thread;
    private Graph graph;
    private Random random = new Random();
    private Paint paint;

    public GameView(GameActivity activity, int screenX, int screenY, int numNodes) {
        super(activity);

        this.screenX = screenX;
        this.screenY = screenY;
        this.nodes = numNodes;

        paint = new Paint();

        //adjust size of sprites based on screen size using an arbitrary size as a baseline
        screenRatioX = screenX / 1920f;
        screenRatioY = screenY / 1080f ;

        graph = new Graph(nodes);

        for(int i = 0; i < nodes; i++){
            int x = random.nextInt(screenX); //number from 0 - screenX
            int y = random.nextInt(screenY);
            graph.addCity(x, y);
        }

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
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas(); //returns canvas being displayed on screen

            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            //draw background
            canvas.drawRect(0, 0, screenX, screenY, paint);


            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);

            for (City city : graph.cities) {
                canvas.drawCircle(city.xPos, city.yPos, 20f, paint);
            }

            getHolder().unlockCanvasAndPost(canvas); //display canvas
        }
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


