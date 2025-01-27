package SimpleSimulation.engine.simulation;

public class CollisionGrid {
    private Particle[][] grid;
    private int cellCount;
    private float size;
    public CollisionGrid(float size,int cellCount){
        this.grid = new Particle[cellCount][cellCount];
        this.cellCount = cellCount;
        this.size = size;

    }

    public void addToGrid(Particle particle){
        int gridX = (int)( ((particle.x() + (.5f * size))/(size)) * (cellCount-1));
        int gridY = (int)( ((particle.y() + (.5f * size))/(size)) * (cellCount-1));
        grid[gridX][gridY] = particle;

    }

    public Particle[][] getGrid() {return grid;}
}
