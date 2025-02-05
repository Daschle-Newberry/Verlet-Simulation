package SimpleSimulation.engine.simulation;

public class GravitySource {
    private float x;
    private float y;
    private float force;

    public GravitySource(float x, float y){
        this.x = x;
        this.y = y;
        this.force = .8f;
    }

    public float x(){return this.x;}
    public float y(){return this.y;}
    public float force(){return this.force;}
}
