package net.ontariotechu.travellingsalesman;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class City {

    int xPos;
    int yPos;
    int width, height;
    boolean visited;
    Bitmap img;


    public City (int x, int y, Resources res) {
        xPos = x;
        yPos = y;
        visited = false;

        img = BitmapFactory.decodeResource(res, R.drawable.town);
        width = img.getWidth();
        height = img.getHeight();

        width = (int) (width * GameView.screenRatioX * 0.08f);
        height = (int) (height * GameView.screenRatioY * 0.08f);

        img = Bitmap.createScaledBitmap(img, width, height, false);

    }

}