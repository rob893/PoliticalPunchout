package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public class Player extends GameObject{
    private Bitmap spritesheetIdle;
    private Bitmap spritesheetRun;
    private int score;
    private int hitPoints = 5;
    private boolean playing;
    private boolean walk;
    private boolean wLeft;
    private boolean wRight;
    private Animation Idleanimation = new Animation();
    private Animation runRightAnimation = new Animation();
    private Animation runLeftAnimation = new Animation();
    private long startTime;
    private int row;

    public Player(Bitmap resI, Bitmap resR, int w, int h, int numFrames1, int numFrames2){
        x = 100;
        y = GamePanel.HEIGHT - 250;
        dx = 10;
        score = 0;
        height = h;
        width = w;

        Bitmap[] imageIdle = new Bitmap[numFrames1];
        Bitmap[] runRight = new Bitmap[numFrames2];
        Bitmap[] runLeft = new Bitmap[numFrames2];
        spritesheetIdle = resI;
        spritesheetRun = resR;

        for(int i = 0; i < imageIdle.length; i++){
            row = 1;
            imageIdle[i] = Bitmap.createBitmap(spritesheetIdle, i*width, row*height, width, height); //second row of sheet for side idle
        }
        for(int i = 0; i < runRight.length; i++){
            row = 1;
            runRight[i] = Bitmap.createBitmap(spritesheetRun, i*width, row*height, width, height); //second row of sheet for side idle
        }
        for(int i = 0; i < runLeft.length; i++){
            row = 3;
            runLeft[i] = Bitmap.createBitmap(spritesheetRun, i*width, row*height, width, height); //second row of sheet for side idle
        }


        Idleanimation.setFrames(imageIdle);
        Idleanimation.setDelay(50);

        runRightAnimation.setFrames(runRight);
        runRightAnimation.setDelay(35);

        runLeftAnimation.setFrames(runLeft);
        runLeftAnimation.setDelay(35);

        startTime = System.nanoTime();
    }

    public void update(){
        long elapsed = (System.nanoTime() - startTime) / 1000000;
        if(elapsed > 100){
            startTime = System.nanoTime();
        }
        if(x > GamePanel.WIDTH-150){
            x = GamePanel.WIDTH-150;
        }
        if(x <-50){
            x= -50;
        }
        if(!walk) {
           Idleanimation.update();
        }
        if(walk && wRight){
            runRightAnimation.update();
            x += dx;
        }

        if(walk && wLeft){
            runLeftAnimation.update();
            x -= dx;
        }
    }

    public void draw(Canvas canvas){
        if(!walk) {
            canvas.drawBitmap(Idleanimation.getImage(), x, y, null);
        }
        if(walk && wRight){
            canvas.drawBitmap(runRightAnimation.getImage(), x, y, null);
        }

        if(walk && wLeft){
            canvas.drawBitmap(runLeftAnimation.getImage(), x, y, null);
        }
    }

    public void setWalk(boolean b){
        walk = b;
    }

    public void setwLeft(boolean b) { wLeft = b;}

    public void setwRight(boolean b){ wRight = b; }

    public void setHitPoints(int x){ hitPoints = x; }

    public void setPlaying(boolean b){ playing = b; }

    public void setScore(int x) { score = x; }

    public boolean isPlaying(){ return playing; }

    public int getHitPoints() {return hitPoints; }

    public int getScore(){ return score; }
}
