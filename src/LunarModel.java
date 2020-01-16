import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

//extends for map and noise
public class LunarModel extends PApplet {
    private float flying;
    private float move;
    private int row, col;
    private float[][] terrain;
    private int scl,w,h;
    private int difficulty;
    private int baseStart, baseEnd, baseEndFinish;
    private int baseWidthStart, baseWidthEnd,baseTimeStart;
    private float terrainTime;
    private float culMove;
    private boolean timeFlag;
    private float distanceToBase;
    private float fuel;
    private float xInc,yInc;

    //Observer pattern
    private List<Observer> observers = new ArrayList<Observer>();

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    //type 0=fuel, 1=distance, 3=win, 4=loose
    public void notifyObservers(int type) {
        for (Observer o : this.observers)
            o.update(this, type);
    }


    public LunarModel() {
        scl = 20;
        w = 2000;
        h = 2600;
        flying = 0;
        move = 0.01f;
        difficulty = 1;
        terrainTime = 0;
        xInc=0.07f;
        yInc=0.04f;

        col = w / scl;
        row = h / scl;
        culMove = 0;

        baseStart = 0;
        baseEnd = 0;
        baseEndFinish = 20;
        baseTimeStart = 1000;
        baseWidthStart = (int) random(50, col - 70);
        baseWidthEnd = baseWidthStart + 20;

        distanceToBase = baseTimeStart;

        fuel = 1000;

        terrain = new float[row][col];
    }

    public void fly() {
        flying -= move;
        if (difficulty < 400) difficulty++;

        float yoff = flying;
        for (int y = 0; y < col; y++) {
            float xoff = 0;
            for (int x = 0; x < row; x++) {
                //if it's time for the base
                if (terrainTime >= baseTimeStart) {
                    if (y < baseStart && y > baseEnd && x > baseWidthStart && x < baseWidthEnd)
                        terrain[x][y] = 0;
                    else
                        terrain[x][y] = map(noise(xoff, yoff), 0, 1, -difficulty, difficulty);
                }
                //if it's no base, normal terrain
                else
                    terrain[x][y] = map(noise(xoff, yoff), 0, 1, -difficulty, difficulty);
                xoff += xInc;
            }
            yoff += yInc;
        }

        moveBase();

        terrainTime += move / yInc;
        this.setDistance();
        if (distanceToBase < row - baseEndFinish && distanceToBase > -baseEndFinish) timeFlag = true;
        else timeFlag=false;

        notifyObservers(1);
    }

    private void moveBase() {
        if (terrainTime >= baseTimeStart) {
            //propagate the base according to the distance, basing on the yoff as a metric
            culMove += move;
            if (culMove > 0.04) {
                baseStart++;
                culMove = 0;
                if (baseStart > baseEndFinish)
                    baseEnd++;
            }
            if (culMove < -0.04) {
                baseStart--;
                culMove = 0;
                if (baseStart > baseEndFinish)
                    baseEnd--;
            }
        }
    }

    public void setMove(float s) {
        if (abs(this.move + s) < 0.05)
            this.move += s;
    }

    public float getMove(){
        return this.move;
    }

    public void stop() {
        this.move = 0;
    }

    public float getTerrain(int x, int y) {
        return terrain[x][y];
    }

    public boolean baseTime() {
        return timeFlag;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public int getScale() {
        return scl;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public float getFuel() {
        return fuel;
    }

    public void setFuel(float f) {
        fuel += f;
        notifyObservers(0);
    }

    public float getDistance() {
        return distanceToBase;
    }

    private void setDistance() {
        distanceToBase = (baseTimeStart + row - terrainTime);
        notifyObservers(1);
    }

    public int getBaseWidthStart(){
        return this.baseWidthStart;
    }

    public int getBaseWidthEnd(){
        return this.baseWidthEnd;
    }

    public void setWin(boolean w) {
        if (w)
            notifyObservers(3);
        else
            notifyObservers(4);
    }

}
