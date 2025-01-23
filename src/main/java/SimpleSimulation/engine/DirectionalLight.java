package SimpleSimulation.engine;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class DirectionalLight {
    private Matrix4f viewMatrix = new Matrix4f();
    private Vector3f position;
    private Vector3f lightFront;

    private Vector3f lightUp = new Vector3f(0.0f,1.0f,0.0f);

    public DirectionalLight(Vector3f position, Vector3f direction){
        this.position = position;
        this.lightFront = direction;
    }
    public Matrix4f getViewMatrix(){
        this.viewMatrix.identity();
        this.viewMatrix = viewMatrix.lookAt(this.position,
                new Vector3f(this.position.x - this.lightFront.x,this.position.y - this.lightFront.y,this.position.z - this.lightFront.z),
                this.lightUp);
        return this.viewMatrix;

    }

    public void setLightPos(Vector3f newPos){this.position = newPos;}

    public void moveToPlayer(Vector3f playerPosition){
       this.position = new Vector3f(playerPosition.x,playerPosition.y + (float)(Math.cos(1)*500),playerPosition.z + (float)(Math.sin(1)*500));

        Vector3f lightDirection = new Vector3f();
        position.sub(playerPosition,lightDirection);
        setLightFront(lightDirection);

    }
    public void setLightFront(Vector3f newFront){
        this.lightFront = newFront;
        this.lightFront.normalize();}
    public Vector3f getPosition(){
        return this.position;
    }
    public Vector3f getLightFront(){return this.lightFront;}

}


