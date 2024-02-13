import java.util.concurrent.ConcurrentHashMap;

public class ParticleProcessor implements Runnable {
    private ConcurrentHashMap<Long, Particle> particleMap;
    private WallController wallController;
    private int canvasWidth, canvasHeight;
    private long lastProcessingTime = 0;

    // Constructor now accepts a ConcurrentHashMap for particles and the WallController
    public ParticleProcessor(int canvasWidth, int canvasHeight, WallController wallController, ConcurrentHashMap<Long, Particle> particleMap) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.wallController = wallController;
        this.particleMap = particleMap;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();

        // Process each particle for updates and collision checks
        particleMap.forEach((id, particle) -> {
            particle.update(canvasWidth, canvasHeight); // Update particle position based on its velocity
            // Assuming the Wall class and its collision method are correctly implemented
            particle.checkWallCollisions(wallController.getWall()); // Check and handle collisions with walls
        });

        long endTime = System.currentTimeMillis();
        lastProcessingTime = endTime - startTime;
    }

    public long getLastProcessingTime() {
        return lastProcessingTime;
    }
}
