package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public class Background extends GameObject {
    private Bitmap image;
    private int x;
    private int y;

    public Background(Bitmap res){
        image = res;
    }

    public void update(){}

    public void draw(Canvas canvas){
        canvas.drawBitmap(image, x, y, null);
    }

}
