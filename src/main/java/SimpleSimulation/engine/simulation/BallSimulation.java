package SimpleSimulation.engine.simulation;

public class BallSimulation {
    private float g = -1;

    private int particleCount;
    private int particleRadius;

    private Particle[] particles;
    private  float[] particlePositions;
    public BallSimulation(int particleCount,int particleRadius, int spawnX, int spawnY){
        this.particleCount = particleCount;
        this.particleRadius = particleRadius;

        this.particles = createParticleArray(particleCount, spawnX,spawnY);
        this.particlePositions = new float[particleCount*2];
    }

    private static Particle[] createParticleArray(int particleCount,int spawnX, int spawnY){
        Particle[] particles = new Particle[particleCount];
        for(int i = 0; i < particleCount; i++){
            particles[i] = new Particle((float)Math.random()*spawnX,(float)Math.random()*spawnY,0,0);
        }
        return particles;
    }

    public float[] update(double dt,int substeps){
        double subDT = dt/substeps;
        for(int step = 0; step < substeps; step++){
            int index  = 0;
            for(int i = 0; i < particleCount; i++){
                particles[i].vY += g * subDT;
                particles[i].y += particles[i].vY * subDT;
                particlePositions[index] = particles[i].x;
                particlePositions[index+1] = particles[i].y;
                index += 2;
            }
        }
        return particlePositions;
    }
}
