package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Robert Herber on 6/21/2017.
 */

public class HealthPack extends GameObject {
    private Bitmap image;

    public HealthPack(Bitmap res, int x, int w, int h){
        this.x = x;
        this.y = GamePanel.HEIGHT - 250;
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
