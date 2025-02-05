package SimpleSimulation.engine.simulation;

import java.util.ArrayList;

public class BoundingBox {
    private static ArrayList<BoundingBox> boundingBoxes = new ArrayList<>();
    private float top,bottom,left,right;

    public BoundingBox(float size){
        this.left = -.5f * size;
        this.right = .5f * size;

        this.bottom = -.5f * size;
        this.top = .5f * size;

        boundingBoxes.add(this);

    }

    public float getTop(){return top;}
    public float getBottom(){return bottom;}
    public float getLeft(){return left;}
    public float getRight(){return right;}
    public static ArrayList<BoundingBox> getBoundingBoxes(){return boundingBoxes;}
}
