package SimpleSimulation.util;

public class Vector2F {
    private float x;
    private float y;
    public Vector2F(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float x(){return this.x;}
    public float y(){return this.y;}



    public void add(Vector2F b) {
        this.x += b.x();
        this.y += b.y();

    }


    public void sub(Vector2F b) {
        this.x -= b.x();
        this.y -= b.y();
    }

    public Vector2F addCopy(Vector2F b) {

        return new Vector2F(this.x + b.x(),this.y + b.y());

    }

    public Vector2F subCopy(Vector2F b) {

        return new Vector2F(this.x - b.x(),this.y - b.y());

    }



    public void mul(float b) {
        this.x *= b;
        this.y *= b;

    }

    public void mul(Vector2F b) {
        this.x *= b.x();
        this.y *= b.y();
    }

    public void normalize() {
        float magnitude = (float)Math.sqrt(Math.pow(this.x,2) + Math.pow(this.y,2));
        this.x /= magnitude;
        this.y /= magnitude;
    }
    public void set(Vector2F b) {
        this.x = b.x();
        this.y = b.y();
    }

}
