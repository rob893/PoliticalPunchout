package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public class Missile extends GameObject {

    //private int score;
    private int speed;
    //private Random rand = new Random();
    private Animation animation = new Animation();
    private Bitmap spritesheet;
    private boolean goingRight;

    public Missile(Bitmap res, int x, int y, int w, int h, int s, boolean goingRight, int numFrames){
        super.x = x;
        super.y = y;

        width = w;
        height = h;
        speed = s;
        this.goingRight = goingRight;


        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        if(goingRight){
            for(int i = 0; i<image.length; i++){
                image[i] = Bitmap.createBitmap(spritesheet, 6, i*height+4, width, height);
            }
        }
        if(!goingRight) {
            for (int i = 0; i < image.length; i++) {
                image[i] = Bitmap.createBitmap(spritesheet, 0, i * height, width, height);
            }
        }

        animation.setFrames(image);
        animation.setDelay(100-speed);
    }

    public void update(){
        if(goingRight){
            x+= speed;
        } else {
            x -= speed;
        }
        animation.update();
    }

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(animation.getImage(), x, y, null);
        } catch (Exception e) {
        }
    }

    @Override
    public int getWidth(){
        return width - 10;
    }
}