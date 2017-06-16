package com.example.robertherber.politicalpunchout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public class Button extends GameObject {
    public Matrix btn_matrix = new Matrix();

    public RectF btn_rect;

    float width;
    float height;
    Bitmap bg;

    public Button(float width, float height, Bitmap bg)
    {
        this.width = width;
        this.height = height;
        this.bg = bg;

        btn_rect = new RectF(0, 0, width, height);
    }

    public void setPosition(float x, float y)
    {
        btn_matrix.setTranslate(x, y);
        btn_matrix.mapRect(btn_rect);
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bg, btn_matrix, null);
    }
}
