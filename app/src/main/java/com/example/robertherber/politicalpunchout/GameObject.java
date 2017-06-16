package com.example.robertherber.politicalpunchout;

import android.graphics.Rect;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public abstract class GameObject {
    protected int x;
    protected int y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public void setWidth(int w){
        this.width = w;
    }

    public void setHeight(int h){
        this.height = h;
    }

    public int getX(){
        return x;
    }

    public int getY(){return y;}

    public int getHeight(){ return height; }

    public int getWidth(){ return width; }

    public int getDx(){ return dx; }

    public int getDy(){ return dy; }

    public Rect getRectangle(){
        return new Rect(x, y, x+width, y+height);
    }
}
