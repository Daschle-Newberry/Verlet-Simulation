package SimpleSimulation.engine.simulation;

public class BoundingBox {
    private float top,bottom,left,right;

    public BoundingBox(float size){
        this.left = -.5f * size;
        this.right = .5f * size;

        this.bottom = -.5f * size;
        this.top = .5f * size;

    }

    public float getTop(){return top;}
    public float getBottom(){return bottom;}
    public float getLeft(){return left;}
    public float getRight(){return right;}
}
