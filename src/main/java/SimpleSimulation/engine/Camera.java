package SimpleSimulation.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    private int type;
    private Matrix4f projectionMatrix,viewMatrix,globalScale;

    public Camera(){
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.globalScale = new Matrix4f().scale(.5f);
        adjustProjection();

    }
    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.perspective(45,(float)Window.get().width/Window.get().height,0.1f,10f);
    }

    public Matrix4f getViewMatrix(){
        this.viewMatrix.identity();
        this.viewMatrix.translation(new Vector3f(0.0f,0.0f,-1.0f));
        this.viewMatrix.mul(globalScale,this.viewMatrix);
        return this.viewMatrix;

    }
    public Matrix4f getProjectionMatrix(){
        return this.projectionMatrix;
    }

}
