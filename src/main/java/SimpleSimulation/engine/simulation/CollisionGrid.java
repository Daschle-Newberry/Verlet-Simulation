package SimpleSimulation.engine.simulation;

import java.util.ArrayList;
import java.util.Arrays;

public class CollisionGrid {
    private Cell[][] grid;
    private int cellCount;
    private float cellSize;
    private float size;
    public CollisionGrid(float size,int cellCount){

        this.grid = new Cell[cellCount+2][cellCount+2];
        for(int x = 0; x < cellCount+2; x++){
            for(int y = 0; y < cellCount+2; y++){
                grid[x][y] = new Cell();
            }
        }
        this.cellCount = cellCount;
        this.cellSize = size/cellCount;
        this.size = size;


    }
    public void resetGrid(){
        for(int x = 0; x < cellCount + 2; x++){
            for(int y = 0; y < cellCount + 2; y++){
                grid[x][y].clear();
            }
        }
    }
    public void addToGrid(Particle particle){
        int x = (int)Math.floor((particle.x() + (.5 * size))/cellSize) + 1;
        int y = (int)Math.floor((particle.y() + (.5 * size))/cellSize) + 1;

        grid[x][y].add(particle);

    }

    public Cell[][] getGrid() {return this.grid;}

}
