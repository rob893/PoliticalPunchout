package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

/**
 * Created by Robert Herber on 6/21/2017.
 */

public class HealthPack extends GameObject {
    private Bitmap image;
    private Random rand = new Random();

    public HealthPack(Bitmap res, int w, int h){
        this.x = 50+ rand.nextInt(GamePanel.WIDTH - 75);
        this.y = GamePanel.HEIGHT - 100;
        width = w;
        height = h;

        image = Bitmap.createBitmap(res, 0, 0, width, height);
    }

    public void update(){}

    public void draw(Canvas canvas){
        try{
            canvas.drawBitmap(image, x, y, null);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
