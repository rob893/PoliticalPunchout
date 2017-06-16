package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public class Enemy extends GameObject {

    private Bitmap spritesheet;
    //private int x;
    //private int y;
    private Animation animation = new Animation();
    private boolean goingRight;

    public Enemy(Bitmap res, int w, int h, int y, int numFrames){
        super.y = y;

        width = w;
        height = h;

        Random rand = new Random();
        goingRight = rand.nextBoolean();

        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        if(goingRight){
            super.x = 0;
            for(int i = 0; i<image.length; i++){
                image[i] =  Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
            }
        }
        if(!goingRight){
            super.x = GamePanel.WIDTH;
            for(int i = 0; i<image.length; i++){
                image[i] =  Bitmap.createBitmap(GamePanel.flip(spritesheet), i*width+5, 0, width, height);
            }
        }

        animation.setFrames(image);
        animation.setDelay(10);
    }

    public void update(){
        animation.update();

        if(goingRight){
            x += 5;
        } else {
            x -= 5;
        }

    }

    public boolean isGoingRight(){
        return goingRight;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
}
