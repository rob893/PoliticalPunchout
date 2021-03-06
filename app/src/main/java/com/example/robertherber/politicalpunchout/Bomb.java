package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import java.util.Random;

/**
 * Created by Robert Herber on 6/16/2017.
 */

public class Bomb extends GameObject {
    private Bitmap image;
    int speed;
    private Random rand = new Random();

    public Bomb(Bitmap res, int x, int y, int w, int h, int playerLevel){
        this.x = x;
        this.y = y;
        width = w;
        height = h;
        speed = playerLevel + rand.nextInt(4);
        if(speed > 10){  //cap speed
            speed = 10;
        }

        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){
       y += speed;
    }

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(image, x, y, null);
        } catch (Exception e){}
    }
}
