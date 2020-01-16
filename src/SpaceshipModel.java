public class SpaceshipModel {
    private float height;
    private float turning, falling;
    // lower middle side
    private float x, y;
    private LunarModel lunarModel;


    public SpaceshipModel(LunarModel lm) {
        lunarModel = lm;
        height = 50;
        turning = 0;
        x = lunarModel.getWidth() / 2 + 200;
        y = lunarModel.getHeight() / 1.34f;
        falling = -0.8f;
    }

    public void setHeight(float h) {
        if (height + h > 150) height = 150;
        else height += h;
    }

    public float getHeight() {
        return height;
    }

    public void setTurning(float t) {
        turning += t;
        x += t;
    }

    public void fallDown() {
        this.setHeight(falling);
    }

    public void stop() {
        falling = 0;
    }

    public float getTurning() {
        return turning;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
