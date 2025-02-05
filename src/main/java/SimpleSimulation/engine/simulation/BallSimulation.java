package SimpleSimulation.engine.simulation;

import SimpleSimulation.engine.input.MouseListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BallSimulation {
    private float[] universalG = new float[]{0,-1};
    private int substeps = 8;
    private double fixedDT = .0075;


    private Particle[] particles;
    private int particleCount;
    private  float[] particlePositions;
    private int maxParticles;
    private float particleRadius;

    private GravitySource[] gravitySources;

    private BoundingBox boundingBox;
    private CollisionGrid particleGrid;

    private int frame;


    public BallSimulation(int maxParticles, float particleRadius,float size){
        this.boundingBox = new BoundingBox(size);
        this.particles = new Particle[]{};
        this.maxParticles = maxParticles;
        this.particleRadius = particleRadius;
        this.particlePositions = new float[maxParticles*5];
        this.particleGrid = new CollisionGrid(size + particleRadius, (int)( .5 * Math.ceil(size/particleRadius)));
        this.gravitySources =  new GravitySource[0];

        for(int i = 0; i < maxParticles; i++){
            float x = (float) ((Math.random() * 2) - 1) * (size - 2 * particleRadius);
            float y = (float) ((Math.random() * 2) - 1) * (size - 2 * particleRadius);
            addParticle(x,y,0,0,particleRadius);
            particleCount++;
        }


    }

    private void addParticle(float x, float y, float vX, float vY, float radius){
        if (frame % 20 == 0 && particleCount < maxParticles){
            particles = Arrays.copyOf(particles,particles.length + 1);
            float r  = (float)(Math.tan(frame/100f) + 2)/2f;
            float g  = (float)(Math.cos(frame/100f) + 2)/2f;;
            float b  = (float)(Math.sin(frame/100f) + 2)/2f;

            particles[particles.length - 1] = new Particle(x,y,vX,vY,radius,r,g,b);

            particleCount += 1;
        }
    }

    private void checkBoundingCollisionsSquare(Particle particle) {
        if(particle.x() > boundingBox.getRight() - particleRadius){
            particle.setX(boundingBox.getRight() - particleRadius);
        }
        else if(particle.x() < boundingBox.getLeft() + particleRadius){
            particle.setX(boundingBox.getLeft() + particleRadius);
        }

        if(particle.y() < boundingBox.getBottom() + particleRadius){
            particle.setY(boundingBox.getBottom() + particleRadius);
        }
        if(particle.y() > boundingBox.getTop() - particleRadius){
            particle.setY(boundingBox.getTop() - particleRadius);
        }
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




    private void checkParticleCollisions(){
        Cell[][] grid = particleGrid.getGrid();
        for(int x = 1; x < grid.length - 1; x++){
            for(int y = 1; y < grid[x].length - 1; y++){
                if(grid[x][y].get().isEmpty()){
                    continue;
                }
                Cell cellA  = grid[x][y];
                for(int dx = -1; dx <= 1; dx++){
                    for(int dy = -1; dy <= 1; dy++){
                        if(grid[x + dx][y + dy].get().isEmpty()){
                            continue;
                        }
                        Cell cellB = grid[x + dx][y + dy];
                        checkCells(cellA,cellB);
                    }
                }
            }
        }
    }
    

    private void updateParticlePositons(double dt){
        int index = 0;
        for(Particle particle : particles){
            applyGravity(particle);
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
        particleGrid.resetGrid();
        for(Particle particle : particles){
            checkBoundingCollisionsSquare(particle);
            particleGrid.addToGrid(particle);
        }
    }

    private void addGravitySource(float x, float y){

        gravitySources =  Arrays.copyOf(gravitySources, gravitySources.length + 1);
        gravitySources[gravitySources.length - 1] = new GravitySource(x,y);
    }
    private void applyGravity(Particle particle){
        particle.accel(universalG);
        for(GravitySource source : gravitySources){
            float directionX = source.x() - particle.x();
            float directionY = source.y() - particle.y();

            float distance = (float)Math.sqrt((directionX * directionX)+(directionY*directionY));
            float force = source.force()/distance;
            float[] accel= new float[]{(directionX/distance) * force,(directionY/distance) * force};
            particle.accel(accel);
        }

    }

    private void processUserInput(){
        if(MouseListener.get().mouseButtonDown(0)){
            addGravitySource(MouseListener.getXWorld(),MouseListener.getYWorld());
        }
        if(MouseListener.get().mouseButtonDown(1)){
            gravitySources = new GravitySource[0];
        }
    }
    public float[] update(){
        processUserInput();
        addParticle(0,0.0f,0.0f,0.0f,particleRadius);
        double subDT = fixedDT/substeps;
        for(int step = 0; step < substeps; step++) {
            addToGrid();
            checkParticleCollisions();
            updateParticlePositons(subDT);
        }
        frame += 1;
        return particlePositions;
    }
}


