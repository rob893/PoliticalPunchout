package com.example.robertherber.politicalpunchout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Robert Herber on 6/15/2017.
 */

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 942;  //856
    public static final int HEIGHT = 528;  //480
    boolean fired = false;
    private Background bg;
    private Bitmap[] backgrounds;
    private Bitmap scaledBG;
    private Player player;
    private Button button1;
    private Button button2;
    private HealthPack healthPack;
    private Bitmap scaledButton;
    private Bitmap scaledBomb;
    private Bitmap scaledHealth;
    private ArrayList<Missile> missiles;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bomb> bombs;
    private long healthStartTime;
    private long enemyStartTime;
    private long bombStartTime;
    private long trumpSoundStartTime;
    private Random rand;
    private Explosion explosion;
    private boolean exStart;
    private boolean areHealthpack;
    private SoundPool soundPool;
    private int[] soundIds;
    private int[] trumpSoundIds;
    private MainThread thread;

    public GamePanel(Context context){
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter < 1000){
            counter++;
            try{
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        rand = new Random();
        backgrounds = new Bitmap[7];
        backgrounds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1);
        backgrounds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        backgrounds[2] = BitmapFactory.decodeResource(getResources(), R.drawable.bg1);
        backgrounds[3] = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        backgrounds[4] = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
        backgrounds[5] = BitmapFactory.decodeResource(getResources(), R.drawable.bg4);
        backgrounds[6] = BitmapFactory.decodeResource(getResources(), R.drawable.bg5);
        scaledBG = Bitmap.createScaledBitmap(backgrounds[rand.nextInt(backgrounds.length)], WIDTH, HEIGHT, true);
        bg = new Background(scaledBG);
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.trump_iddle), BitmapFactory.decodeResource(getResources(), R.drawable.trump_run), 256, 256, 10, 6);
        scaledButton = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.derp), 100, 100, true);
        scaledBomb = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bombs), 35, 35, true);
        scaledHealth = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.heart), 45, 45, true);
        areHealthpack = false;
        button1 = new Button(100, 100, scaledButton);
        button1.setPosition(0, 0);
        button2 = new Button(100, 100, scaledButton);
        button2.setPosition(WIDTH - 100, 0);
        missiles = new ArrayList<>();
        enemies = new ArrayList<>();
        bombs = new ArrayList<>();
        //sounds
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundIds = new int[2];
        trumpSoundIds = new int[5];
        soundIds[0] = soundPool.load(getContext(), R.raw.boom, 1);
        soundIds[1] = soundPool.load(getContext(), R.raw.pew, 1);
        //trumpSoundIds[0] = soundPool.load(getContext(), R.raw.nope, 1);
        trumpSoundIds[0] = soundPool.load(getContext(), R.raw.trump1, 1);
        trumpSoundIds[1] = soundPool.load(getContext(), R.raw.trump2, 1);
        trumpSoundIds[2] = soundPool.load(getContext(), R.raw.trump3, 1);
        trumpSoundIds[3] = soundPool.load(getContext(), R.raw.trump4, 1);
        trumpSoundIds[4] = soundPool.load(getContext(), R.raw.trump5, 1);
        trumpSoundStartTime = System.nanoTime();
        enemyStartTime = System.nanoTime();
        bombStartTime = System.nanoTime();
        healthStartTime = System.nanoTime();
        player.setPlaying(true);
        player.setwRight(true);
        player.setwLeft(false);
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        if(button1.btn_rect.contains(event.getRawX()/(getWidth()/ (WIDTH*1.f)), event.getRawY()/(getHeight()/ (HEIGHT*1.f))-75) && !fired && player.getAmmo() > 0) { //if button1  contains the touch, fire a missile once
            fired = true;
            player.setwLeft(true);
            player.setwRight(false);
            player.setAmmo(player.getAmmo() - 1);
            missiles.add(new Missile(BitmapFactory.decodeResource(getResources(), R.drawable.missile), player.getX()+35, player.getY()+40, 45, 15, 10, false, 13));
            soundPool.play(soundIds[1], 1, 1, 1, 0, 1);
            return true;
        }
        if(button2.btn_rect.contains(event.getRawX()/(getWidth()/ (WIDTH*1.f)), event.getRawY()/(getHeight()/ (HEIGHT*1.f))-75) && !fired && player.getAmmo() > 0) { //if button2 contains the touch, fire a missile once
            fired = true;
            player.setwRight(true);
            player.setwLeft(false);
            player.setAmmo(player.getAmmo() - 1);
            missiles.add(new Missile(rotateImage(BitmapFactory.decodeResource(getResources(), R.drawable.missile), 180), player.getX()+200, player.getY()+40, 45, 15, 10, true, 13));
            soundPool.play(soundIds[1], 1, 1, 1, 0, 1);
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            if((int)event.getRawX()/(getWidth()/ (WIDTH*1.f))-100<=player.getX()){
                player.setwRight(false);
                player.setwLeft(true);
            }
            if((int)event.getRawX()/(getWidth()/ (WIDTH*1.f))-100 > player.getX()){
                player.setwLeft(false);
                player.setwRight(true);
            }

            player.setWalk(true);
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP){
            player.setWalk(false);
            fired = false;
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update(){
        if(player.isPlaying()){
            //update player
            player.update();
            if(exStart) {
                explosion.update();
            }

            if(player.getScore() % 500 == 0 && player.getScore() != 0){
                player.setScore(player.getScore() + 100);
                player.setLevel(player.getLevel() + 1);
                player.setAmmo(25 - (player.getLevel()));
            }

            //update missiles
            //missileStartTime = System.nanoTime();
            for(int i = 0; i<missiles.size(); i++){
                missiles.get(i).update();
                if((missiles.get(i).getX() < -100) || (missiles.get(i).getX() > WIDTH + 50)){
                    missiles.remove(i);
                    break;
                }
            }

            //update bombs
            long bombElapsed = (System.nanoTime() - bombStartTime) / 1000000;
            if(bombElapsed > 8100-(player.getLevel()*100)){
                bombs.add(new Bomb(scaledBomb, player.getX(), 0, 35, 35, player.getLevel()));
                bombStartTime = System.nanoTime();
            }
            for(int i = 0; i<bombs.size(); i++){
                bombs.get(i).update();
                if(bombs.get(i).getY() >= HEIGHT -20){
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), bombs.get(i).getX(), bombs.get(i).getY() - 30, 100, 100, 25);
                    exStart = true;
                    soundPool.play(soundIds[0], (float)0.5, (float)0.5, 1, 0, 1);
                    bombs.remove(i);
                    break;
                }
                if(collision(player, bombs.get(i))){
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), bombs.get(i).getX(), bombs.get(i).getY() - 30, 100, 100, 25);
                    exStart = true;
                    soundPool.play(soundIds[0], (float)0.5, (float)0.5, 1, 0, 1);
                    player.setHitPoints(player.getHitPoints() - 1);
                    if (player.getHitPoints() == 0) {
                        player.setPlaying(false);
                    }
                    bombs.remove(i);
                    break;
                }

                for (int j = 0; j < missiles.size(); j++) {
                    if (collision(missiles.get(j), bombs.get(i))) {
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), bombs.get(i).getX(), bombs.get(i).getY() - 30, 100, 100, 25);
                        exStart = true;
                        soundPool.play(soundIds[0], (float)0.5, (float)0.5, 1, 0, 1);
                        //soundPool.play(soundIds[2], (float)0.5, (float)0.5, 1, 0, 1);
                        player.setScore(player.getScore() + 25);
                        missiles.remove(j);
                        bombs.remove(i);
                        break;
                    }
                }
            }

            //update enemies
            long enemyElapsed = (System.nanoTime() - enemyStartTime) / 1000000;
            if(enemyElapsed > 2100-(player.getLevel()*100)){
                enemies.add(new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, player.getY() + 45, player.getLevel(), 3));
                enemyStartTime = System.nanoTime();
            }
            for(int i = 0; i<enemies.size(); i++) {
                enemies.get(i).update();

                if ((enemies.get(i).getX() < -100) || (enemies.get(i).getX() > WIDTH + 50)) {
                    enemies.remove(i);
                    break;
                }
                if (collision(enemies.get(i), player)) {
                    explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), enemies.get(i).getX(), enemies.get(i).getY() - 30, 100, 100, 25);
                    exStart = true;
                    soundPool.play(soundIds[0], (float)0.5, (float)0.5, 1, 0, 1);
                    player.setHitPoints(player.getHitPoints() - 1);
                    if (player.getHitPoints() == 0) {
                        player.setPlaying(false);
                    }
                    enemies.remove(i);
                    break;
                }

                for (int j = 0; j < missiles.size(); j++) {
                    if (collision(missiles.get(j), enemies.get(i))) {
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), enemies.get(i).getX(), enemies.get(i).getY() - 30, 100, 100, 25);
                        exStart = true;
                        soundPool.play(soundIds[0], (float)0.5, (float)0.5, 1, 0, 1);
                        //soundPool.play(soundIds[2], 1, 1, 1, 0, 1);
                        player.setScore(player.getScore() + 25);
                        missiles.remove(j);
                        enemies.remove(i);
                        break;
                    }
                }

                /*for(int j = 0; j<bombs.size(); j++){
                    if(collision(bombs.get(j), enemies.get(i))){
                        explosion = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion), enemies.get(i).getX(), enemies.get(i).getY() - 30, 100, 100, 25);
                        exStart = true;
                        player.setScore(player.getScore() + 25);
                        bombs.remove(j);
                        enemies.remove(i);
                        break;
                    }
                }*/
            }

            //update health packs. Only 1 can be active.
            long healthElapsed = (System.nanoTime() - healthStartTime) / 1000000;
            if(healthElapsed > 15000){
                healthPack = new HealthPack(scaledHealth, 45, 45);
                areHealthpack = true;
                healthStartTime = System.nanoTime();
            }
            if(areHealthpack) {
                if (collision(player, healthPack)) {
                    areHealthpack = false;
                    healthPack = null;
                    if(player.getHitPoints() < 7) { //cap player hp at 7
                        player.setHitPoints(player.getHitPoints() + 1);
                    }
                }
            }

            long trumpSoundElapsed = (System.nanoTime() - trumpSoundStartTime) / 1000000;
            if(trumpSoundElapsed > 5000){
                soundPool.play(trumpSoundIds[rand.nextInt(trumpSoundIds.length)], 1, 1, 1, 0, 1);
                trumpSoundStartTime = System.nanoTime();
            }
        }
        else{
            newGame();
        }
    }



    public static Bitmap rotateImage(Bitmap src, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        src = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return src;
    }

    public static Bitmap flip(Bitmap d)
    {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap dst = Bitmap.createBitmap(d, 0, 0, d.getWidth(), d.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }

    public boolean collision(GameObject a, GameObject b){
        if(Rect.intersects(a.getRectangle(), b.getRectangle())){
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas){
        final float scaleFactorX = getWidth() / (WIDTH*1.f);
        final float scaleFactorY = getHeight() / (HEIGHT*1.f);

        if(canvas != null){
            super.draw(canvas);
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);

            //draw stuff
            bg.draw(canvas);
            player.draw(canvas);
            button1.draw(canvas);
            button2.draw(canvas);
            if(areHealthpack) {
                healthPack.draw(canvas);
            }

            for(Missile m: missiles){
                m.draw(canvas);
            }

            for(Enemy e: enemies){
                e.draw(canvas);
            }

            for(Bomb b: bombs){
                b.draw(canvas);
            }
            if(exStart) {
                explosion.draw(canvas);
            }
            drawText(canvas);
            canvas.restoreToCount(savedState);
        }
    }

    public void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("SCORE: " + (player.getScore()), 10, HEIGHT - 10, paint);
        canvas.drawText("LEVEL: " + (player.getLevel()), WIDTH/2- 215, HEIGHT-10, paint);
        canvas.drawText("AMMO: " + (player.getAmmo()), WIDTH/2, HEIGHT - 10, paint);
        canvas.drawText("HEALTH: " + (player.getHitPoints()), WIDTH - 215, HEIGHT - 10, paint);
    }

    public void newGame(){
        areHealthpack = false;
        healthPack = null;
        missiles.clear();
        enemies.clear();
        bombs.clear();
        bombStartTime = System.nanoTime();
        enemyStartTime = System.nanoTime();
        healthStartTime = System.nanoTime();
        player.setScore(0);
        player.setHitPoints(5);
        player.setX(100);
        player.setAmmo(25);
        player.setLevel(1);
        player.setPlaying(true);
        player.setwRight(true);
        player.setwLeft(false);
        scaledBG = Bitmap.createScaledBitmap(backgrounds[rand.nextInt(backgrounds.length)], WIDTH, HEIGHT, true);
        bg = new Background(scaledBG);

    }
}
