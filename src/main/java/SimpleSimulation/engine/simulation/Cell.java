package SimpleSimulation.engine.simulation;
import java.util.ArrayList;

public class Cell {
    private ArrayList<Particle> particles = new ArrayList<>();
    public void add(Particle particle){particles.add(particle);}
    public ArrayList<Particle> get(){return this.particles;}

    public void clear(){particles.clear();}
}
