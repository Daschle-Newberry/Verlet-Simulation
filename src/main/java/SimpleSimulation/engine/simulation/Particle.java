package SimpleSimulation.engine.simulation;

import SimpleSimulation.util.Vector2F;
import SimpleSimulation.util.VectorMath;


public class Particle {
    private float dampening = 1f;
    private float[] currentPosition;
    private float[] lastPosition;
    private float[] acceleration;
    private float[] color;
    private float radius;

    public Particle(float x, float y, float vX, float vY,float radius,float r, float g, float b){

        this.currentPosition = new float[] {x,y};
        this.lastPosition = new float[] {x-vX,y-vY};
        this.acceleration = new float[] {0,0};
        this.color =  new float[]{r,g,b};
        this.radius = radius;
    }

    public void move(double dt){
        float[] velocity = new float[]{currentPosition[0] - lastPosition[0],currentPosition[1] - lastPosition[1]};
        lastPosition[0] = currentPosition[0];
        lastPosition[1] = currentPosition[1];


        float x  = currentPosition[0] + velocity[0] + acceleration[0] * ((float) dt * (float)dt);
        float y  = currentPosition[1] + velocity[1] + acceleration[1] * ((float) dt * (float)dt);

        currentPosition[0] = x;
        currentPosition[1] = y;

        acceleration[0] = 0;
        acceleration[1] = 0;
    }

    public void accel(float[] acc){
        acceleration[0] += acc[0];
        acceleration[1] += acc[1];
    }

    public float x(){return this.currentPosition[0];}
    public float y(){return this.currentPosition[1];}

    public void setX(float x){
        this.currentPosition[0] = x;
    }
    public void setY(float y){
        this.currentPosition[1] = y;
    }

    public float radius(){return this.radius;}
    public float r(){return this.color[0];}
    public float g(){return this.color[1];}
    public float b(){return this.color[2];}


}
