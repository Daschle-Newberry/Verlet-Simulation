package SimpleSimulation.engine.simulation;

public class CollisionWorker implements Runnable {
    private CollisionGrid particleGrid;
    private int start, end;

    public CollisionWorker(CollisionGrid particleGrid, int start, int end){
        this.particleGrid = particleGrid;
        this.start = start;
        this.end = end;
    }

    @Override
    public void run() {
        checkParticleCollisions();
    }
    private void correctParticleCollisions(Particle particleA, Particle particleB) {
        float distX = (particleA.x() - particleB.x()) * (particleA.x() - particleB.x());
        float distY = (particleA.y() - particleB.y()) * (particleA.y() - particleB.y());
        float distance = distX + distY;
        float minDistance = (particleA.radius() + particleB.radius()) * (particleA.radius() + particleB.radius());
        if (distance < minDistance) {
            distance = (float) Math.sqrt(distance);
            float overlap = particleA.radius() + particleB.radius() - distance;
            float[] collisionVector = new float[]{particleA.x() - particleB.x(), particleA.y() - particleB.y()};
            collisionVector = new float[]{collisionVector[0] / distance, collisionVector[1] / distance};


            float radiusRatioA = particleA.radius() / (particleA.radius() + particleB.radius());
            float radiusRatioB = particleB.radius() / (particleA.radius() + particleB.radius());


            particleA.setX(particleA.x() + (collisionVector[0] * (radiusRatioA * overlap * .75f)));
            particleA.setY(particleA.y() + (collisionVector[1] * (radiusRatioA * overlap * .75f)));

            particleB.setX(particleB.x() - (collisionVector[0] * (radiusRatioB * overlap * .75f)));
            particleB.setY(particleB.y() - (collisionVector[1] * (radiusRatioB * overlap * .75f)));
        }
    }

    private void checkCells(Cell cellA, Cell cellB){
        for(Particle particleA : cellA.get()){
            for(Particle particleB : cellB.get()){
                if(particleA != particleB){
                    correctParticleCollisions(particleA,particleB);
                }
            }
        }
    }
    private void checkParticleCollisions() {
        Cell[][] grid = particleGrid.getGrid();
        for (int x = start; x < end; x++) {
            for (int y = 1; y < grid[x].length - 1; y++) {
                if (grid[x][y].get().isEmpty()) {
                    continue;
                }
                Cell cellA = grid[x][y];
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (grid[x + dx][y + dy].get().isEmpty()) {
                            continue;
                        }
                        Cell cellB = grid[x + dx][y + dy];
                        checkCells(cellA, cellB);
                    }
                }
            }
        }
    }
}
