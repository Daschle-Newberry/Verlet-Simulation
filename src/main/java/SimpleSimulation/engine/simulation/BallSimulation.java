package SimpleSimulation.engine.simulation;

import java.util.Arrays;

public class BallSimulation {
    private float[] g = new float[]{0,-1};
    private int substeps = 16;

    private Particle[] particles;
    private int particleCount;
    private  float[] particlePositions;
    private int maxParticles;
    private float particleRadius;

    private BoundingBox boundingBox;
    private CollisionGrid particleGrid;


    private int frame;

    public BallSimulation(int maxParticles, float particleRadius,float size){
        this.boundingBox = new BoundingBox(size);
        this.particles = new Particle[]{};
        this.maxParticles = maxParticles;
        this.particleRadius = particleRadius;
        this.particlePositions = new float[maxParticles*5];
        particleGrid = new CollisionGrid(size,Math.round(size/particleRadius));

    }

    private void addParticle(float x, float y, float vX, float vY, float radius){
        particles = Arrays.copyOf(particles,particles.length + 1);
        particles[particles.length - 1] = new Particle(x,y,vX,vY,radius,(float)Math.random(),(float)Math.random(),(float)Math.random());
    }


    private void applyGravity(Particle particle){
        particle.accel(g);
    }

    private void checkBoundingCollisionsSquare(Particle particle) {
        if(particle.x() > boundingBox.getRight()){
            particle.setX(boundingBox.getRight());
        }
        else if(particle.x() < boundingBox.getLeft()){
            particle.setX(boundingBox.getLeft());
        }

        if(particle.y() < boundingBox.getBottom()){
            particle.setY(boundingBox.getBottom());
        }

    }

    private void correctParticleCollisions(Particle particleA, Particle particleB) {
        if (particleA == null || particleB == null || particleA == particleB) {
            return;
        }

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


            particleA.setX(particleA.x() + (collisionVector[0] * (radiusRatioA * overlap)));
            particleA.setY(particleA.y() + (collisionVector[1] * (radiusRatioA * overlap)));

            particleB.setX(particleB.x() - (collisionVector[0] * (radiusRatioB * overlap)));
            particleB.setY(particleB.y() - (collisionVector[1] * (radiusRatioB * overlap)));
        }

    }
    private void checkParticleCollisions(){
        Particle[][] grid = particleGrid.getGrid();
        for(int x = 1; x < grid.length - 1; x++){
            for(int y = 1; y < grid[x].length - 1; y++){
                for(int dx = -1; dx <= 1; dx++){
                    for(int dy = -1; dy <= 1; dy++){
                        correctParticleCollisions(grid[x][y],grid[x+dx][y+dy]);
                    }
                }
            }
        }
    }

    private void updateParticlePositons(double dt){
        int index = 0;
        for(Particle particle : particles){
            applyGravity(particle);
            checkBoundingCollisionsSquare(particle);
            particle.move(dt);
            particlePositions[index] = particle.x();
            particlePositions[index+1] = particle.y();
            particlePositions[index+2] = particle.r();
            particlePositions[index+3] = particle.g();
            particlePositions[index+4] = particle.b();

            index += 5;
        }
    }
    private void addToGrid(){
        for(Particle particle : particles){
            particleGrid.addToGrid(particle);
        }
    }
    public float[] update(double dt){
        frame += 1;
        double subDT = dt/substeps;
        if (frame % 2 == 0 && particleCount < maxParticles){
            addParticle(-.5f,.3f,0.00006f,0.0f,particleRadius);
            particleCount+= 2;
        }
        for(int step = 0; step < substeps; step++){
            addToGrid();
            checkParticleCollisions();
            updateParticlePositons(subDT);
        }

        return particlePositions;
    }
}
