package net.ontariotechu.travellingsalesman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;

import androidx.core.content.res.TypedArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private static int screenX, screenY, nodes;
    public static float screenRatioX, screenRatioY;
    private boolean isPlaying = true;
    private Thread thread;
    private Graph graph;
    private int initial;
    private Random random = new Random();
    private Paint paint;
    //private int initial, end, current, nextNode;
    private float bestDistance = Float.MAX_VALUE;
    private int[] path, bestPath = null;
    private boolean finished = false, firstIteration = true;
    private Button ffBtn;
    Bitmap background, btnImg;
    int speed = 333;


    public GameView(GameActivity activity, int screenX, int screenY, int numNodes) {
        super(activity);

        this.screenX = screenX;
        this.screenY = screenY;
        this.nodes = numNodes;

        paint = new Paint();

        path = new int[numNodes];

        //adjust size of sprites based on screen size using an arbitrary size as a baseline
        screenRatioX = screenX / 1920f;
        screenRatioY = screenY / 1080f ;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.map);
        //scale image to fullscreen
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

        //create fast forward button
        btnImg = BitmapFactory.decodeResource(getResources(), R.drawable.fastfwd);
        ffBtn = new Button(btnImg.getWidth(), btnImg.getHeight(), btnImg, 0.25f);
        ffBtn.x = screenX - ffBtn.getBitmap().getWidth() -30;
        ffBtn.y = 30;

        graph = new Graph(nodes, getResources());

        for(int i = 0; i < nodes; i++){
            //pick random spot that is 30 * screenRatio away from edges
            int x = random.nextInt(screenX - 200) + 100; //number from 0 - screenX
            int y = random.nextInt(screenY- 200)+ 100;
            graph.addCity(x, y, getResources());
            path[i] = i;
        }

        graph.calculateMatrix();

        //node furthest left is initial and furthest right is end
        //setInitial();
        initial = path[0];

    }

    @Override
    public void run() {

        while(isPlaying){
            update();
            draw();
            calculateDistanceOfPath();
            sleep();
        }
    }

    public void update(){

        if(!firstIteration) {
            /***Find all permutations algorithm***/

            //find largest i so p[i] < p[i+1]
            //if no such x, we are finished

            System.out.println(path[0]);
            if(path[0] != initial){
                finished = true;
                return;
            }
            int largestI = -1;
            for (int i = 0; i < path.length - 1; i++) {
                if (path[i] < path[i + 1]) {
                    largestI = i;
                }
            }
            /*
            if (largestI == -1) {
                finished = true;
                return;
            }*/

            //find largest j so p[i] < p[j]
            int largestJ = -1;
            for (int j = 0; j < path.length; j++) {
                if (path[largestI] < path[j]) {
                    largestJ = j;
                }
            }

            //swap p[i] and p[j]
            int temp = path[largestI];
            path[largestI] = path[largestJ];
            path[largestJ] = temp;

            //reverse p[i+1 .. n]
            int[] startArray = Arrays.copyOfRange(path, 0, largestI + 1);
            int[] endArray = Arrays.copyOfRange(path, largestI + 1, path.length);

            endArray = reverse(endArray);

            //add endArray to path
            path = Arrays.copyOf(startArray, startArray.length + endArray.length);
            System.arraycopy(endArray, 0, path, startArray.length, endArray.length);



        }
        else{
            firstIteration = false;
        }
        /*
        for (int i = 0; i < graph.cities.length; i++) {

            if(!graph.cities[i].checked){ //check all unvisited nodes
                //System.out.println(graph.distances[current][i]);
                if(graph.distances[current][i] < currDistance){

                    currDistance = graph.distances[current][i];
                    nextNode = i;
                    graph.cities[i].checked = true;

                    return; //we only want to visit one node per frame
                }
            }
        }

        //if we reach here, all nodes have been visited
        path.add(graph.cities[nextNode]);
        graph.cities[current].visited = true;
        current = nextNode;
        currDistance = Float.MAX_VALUE;

        if(current == end){
            finished = true;
            System.out.println("finished");
        }*/
    }

    public void draw(){
        if(getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas(); //returns canvas being displayed on screen

            //paint.setColor(Color.BLACK);
            //paint.setStyle(Paint.Style.FILL);

            //draw background
            //canvas.drawRect(0, 0, screenX, screenY, paint);
            canvas.drawBitmap(background, 0, 0, paint);
            canvas.drawBitmap(ffBtn.getBitmap(), ffBtn.x, ffBtn.y, paint);


            //paint.setColor(Color.RED);
            //paint.setStyle(Paint.Style.FILL);

            for (City city : graph.cities) {
                //canvas.drawCircle(city.xPos, city.yPos, 20f, paint);
                canvas.drawBitmap(city.img, city.xPos - city.width/2, city.yPos - city.width/2, paint);
            }
            City[] cities = graph.cities;

            if(!finished){
                //draw path that is being checked
                paint.setColor(Color.GREEN);
                paint.setAlpha(150);
                paint.setStrokeWidth(3);
                paint.setPathEffect(null);

                for(int i = 0; i < path.length-1; i++){
                    //draw current path
                    canvas.drawLine(cities[path[i]].xPos, cities[path[i]].yPos, cities[path[i+1]].xPos, cities[path[i+1]].yPos, paint);
                }
            }

            if(bestPath != null) {
                //draw best path
                paint.setStrokeWidth(15);
                paint.setColor(Color.RED);
                paint.setAlpha(255);
                paint.setPathEffect(new DashPathEffect(new float[]{30, 20}, 0));
                for (int i = 0; i < bestPath.length - 1; i++) {

                    canvas.drawLine(cities[bestPath[i]].xPos, cities[bestPath[i]].yPos, cities[bestPath[i + 1]].xPos, cities[bestPath[i + 1]].yPos, paint);
                }
            }
            getHolder().unlockCanvasAndPost(canvas); //display canvas

            if(finished){
                isPlaying = false;
            }

        }
    }

    public void sleep(){

        try {
            Thread.sleep(speed);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setInitial(){
        int furthestLeft = Integer.MAX_VALUE;

        for (int i = 0; i < graph.cities.length; i++) {
            if(graph.cities[i].xPos < furthestLeft) {
                furthestLeft = graph.cities[i].xPos;
                //initial = i;
            }
        }

        /*
        current = initial;
        path.add(graph.cities[current]);
        //graph.cities[current].visited = true;*/
    }

    public int[] reverse(int[] data){
        for(int i = 0; i < data.length / 2; i++)
        {
            int temp = data[i];
            data[i] = data[data.length - i - 1];
            data[data.length - i - 1] = temp;
        }

        return data;
    }

    public void calculateDistanceOfPath(){
        float distance = 0;
        for(int i = 0; i < path.length-1; i++){
            distance += graph.distances[path[i]][path[i+1]];
        }

        if(distance < bestDistance){
            bestDistance = distance;
            bestPath = Arrays.copyOf(path, path.length);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        if (ffBtn.getRect().contains(x, y) && event.getAction() == MotionEvent.ACTION_UP) //action up makes button less sensitive
        {
           if(speed == 333){
               speed = 0;
           }
           else if(speed == 0){
               speed = 1000;
           }
           else if(speed == 1000){
               speed = 333;
           }
        }

        return true;
    }

}


