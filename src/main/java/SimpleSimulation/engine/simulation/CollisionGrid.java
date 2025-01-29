package SimpleSimulation.engine.simulation;

import java.util.ArrayList;
import java.util.Arrays;

public class CollisionGrid {
    private Cell[][] grid;
    private int cellCount;
    private float cellSize;
    private float size;
    public CollisionGrid(float size,int cellCount){
        this.grid = new Cell[cellCount][cellCount];
        for(int x = 0; x < cellCount; x++){
            for(int y = 0; y < cellCount; y++){
                grid[x][y] = new Cell();
            }
        }
        this.cellCount = cellCount;
        this.cellSize = size/cellCount;
        this.size = size;


    }
    public void resetGrid(){
        for(int x = 0; x < cellCount; x++){
            for(int y = 0; y < cellCount; y++){
                grid[x][y].clear();
            }
        }
    }
    public void addToGrid(Particle particle){
        int x = (int)Math.floor((particle.x() + (.5 * size))/cellSize);
        int y = (int)Math.floor((particle.y() + (.5 * size))/cellSize);




        System.out.println("Position "+ particle.x() + " " + particle.y());
        System.out.println("Grid " + x + " " + y);
        grid[x][y].add(particle);

    }

    public Cell[][] getGrid() {return grid;}

}
