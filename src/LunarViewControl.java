import processing.core.PApplet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

public class LunarViewControl extends PApplet implements KeyListener {
    private LunarModel lunarModel;
    private SpaceshipModel spaceshipModel;
    private Vector<Integer> keys;
    private int width, height;
    private boolean collision;
    private boolean baseFlag;
    private boolean update;

    public LunarViewControl(LunarModel m, SpaceshipModel s, int w, int h) {
        lunarModel = m;
        keys = new Vector<Integer>();
        width = w;
        height = h;
        collision = false;
        spaceshipModel = s;
        baseFlag = false;
        update = true;
    }

    @Override
    public void setup() {
        size(displayWidth, displayHeight, P3D);
    }

    @Override
    public void draw() {
        translate(width / 2 - spaceshipModel.getTurning() * 10, height / 2 - 200);
        rotateX(PI / (2.4f + spaceshipModel.getHeight() / 550));
        translate(-lunarModel.getWidth() / 2 - 200, -lunarModel.getHeight() / 2 + 200);

        this.drawTerrain();
        this.drawSpaceship();

        updateMove(this.update);

        if (keys.contains(KeyEvent.VK_ESCAPE))
            System.exit(0);
    }

    private void drawTerrain() {
        background(0);
        //move the terrain forward
        lunarModel.fly();
        //slow down the movement=spaceship
        if (lunarModel.getMove() > 0) lunarModel.setMove(-0.0003f);
        //draw the terrain array
        for (int y = 0; y < lunarModel.getCol() - 1; y++) {
            beginShape(TRIANGLE_STRIP);
            for (int x = 0; x < lunarModel.getRow(); x++) {
                setColor(x, y);
                vertex(x * lunarModel.getScale(), y * lunarModel.getScale(), lunarModel.getTerrain(x, y));
                vertex(x * lunarModel.getScale(), (y + 1) * lunarModel.getScale(), lunarModel.getTerrain(x, y + 1));
                if (checkCollision(x, y)) collision = true;
            }
            endShape();
        }
        collisionTrue();
    }

    private void setColor(int x, int y) {
        //alpha factors for the terrain to disappear on sides and end
        float alphaX, alphaY;
        alphaY = ((float) y / (float) lunarModel.getCol());
        int xx = x;
        if (xx > (float) lunarModel.getRow() / (float) 2) xx = lunarModel.getRow() - x;
        alphaX = ((float) xx / ((float) lunarModel.getRow() / (float) 2));
        stroke(255, 125 * alphaX * alphaY);
        fill(255, 125 * alphaX * alphaY);
        //base color
        if (lunarModel.baseTime()) {
            if (lunarModel.getTerrain(x, y) == 0 && lunarModel.getTerrain(x, y + 1) == 0)
                fill(255, 200);
        }
    }

    private boolean checkCollision(int x, int y) {
        //if spaceship is as high as the part of terrain
        if ((lunarModel.getTerrain(x, y) >= spaceshipModel.getHeight() && spaceshipModel.getHeight() >= lunarModel.getTerrain(x, y + 1))
                || (lunarModel.getTerrain(x, y + 1) >= spaceshipModel.getHeight() && spaceshipModel.getHeight() >= lunarModel.getTerrain(x, y))
                || (lunarModel.baseTime() && spaceshipModel.getHeight() < 1))
        //if the boundaries of a part of terrain intersect with the spaceship
        {
            Rectangle terr = new Rectangle(x * lunarModel.getScale(), y * lunarModel.getScale(),
                                            lunarModel.getScale(), lunarModel.getScale());
            Rectangle sps = new Rectangle((int) (spaceshipModel.getX() - lunarModel.getScale() / 2 + spaceshipModel.getTurning() * 10), (int) (spaceshipModel.getY() - lunarModel.getScale()),
                                            lunarModel.getScale(), lunarModel.getScale());
            //if collision detected
            if (terr.intersects(sps)) {
                //System.out.println("terrain: "+terr.toString() + "\n" +"spaceship: "+ sps.toString() + "\n" + lunarModel.getTerrain(x, y) + " " + lunarModel.getTerrain(x, y + 1) + " " + spaceshipModel.getHeight());
                //check if it's the base
                if (lunarModel.baseTime() && abs(spaceshipModel.getHeight()) < 1
                        && spaceshipModel.getX() < lunarModel.getBaseWidthEnd() * lunarModel.getScale()
                        && spaceshipModel.getX() > lunarModel.getBaseWidthStart() * lunarModel.getScale()) {
                    baseFlag = true;
                }
                return true;
            }
        }
        return false;
    }

    private void collisionTrue() {
        if (collision) {
            lunarModel.stop();
            spaceshipModel.stop();
            this.update = false;
            if (baseFlag)
                lunarModel.setWin(true);
            else
                lunarModel.setWin(false);
        }
    }

    private void drawSpaceship() {
        spaceshipModel.fallDown();
        stroke(255);
        fill(255);

        translate(0, 0, spaceshipModel.getHeight());
        triangle(spaceshipModel.getX() - lunarModel.getScale() / 2 + spaceshipModel.getTurning() * 10, spaceshipModel.getY(), spaceshipModel.getX() + lunarModel.getScale() / 2 + spaceshipModel.getTurning() * 10, spaceshipModel.getY(), spaceshipModel.getX() + spaceshipModel.getTurning() * 10, spaceshipModel.getY() - lunarModel.getScale());
    }


    //controlling the key events
    //moved the changes in the game to separate function for performance reasons
    private void updateMove(boolean update) {
        if (update) {
            if (keys.contains(KeyEvent.VK_UP)) {
                spaceshipModel.setHeight(lunarModel.getMove() * 80);
                if(lunarModel.getFuel()>0)
                {lunarModel.setMove(0.0005f);
                lunarModel.setFuel(-1);}
            }
            if (keys.contains(KeyEvent.VK_RIGHT))
                spaceshipModel.setTurning(1f);
            if (keys.contains(KeyEvent.VK_LEFT))
                spaceshipModel.setTurning(-1f);
            if (keys.contains(KeyEvent.VK_DOWN))
                lunarModel.setMove(-0.0005f);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keys.contains(e.getKeyCode())) keys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.removeElement(e.getKeyCode());
    }

}
