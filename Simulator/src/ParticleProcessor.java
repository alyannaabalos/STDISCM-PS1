import java.util.*;

public class ParticleProcessor implements Runnable {
    private ParticleController particleController;
    private WallController wallController;
    private int canvasWidth, canvasHeight;
    private long lastUpdateTime = System.currentTimeMillis(); // Initialize with the current time
    private long lastProcessingTime = 0;

    public ParticleProcessor(int canvasWidth, int canvasHeight, WallController wallController) {
        this.particleController = new ParticleController();
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.wallController = wallController;
    }

    public ParticleProcessor(int canvasWidth, int canvasHeight, WallController wallController, List<Particle> particles) {
        this.particleController = new ParticleController(particles);
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.wallController = wallController;
    }

    @Override
    public void run() {
        // Calculate deltaTime in seconds
        long currentTime = System.currentTimeMillis();
        double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // Convert milliseconds to seconds
        lastUpdateTime = currentTime; // Update lastUpdateTime for the next cycle

        List<Wall> walls = wallController.getWall(); // Assuming this method retrieves the current list of walls
        particleController.updateParticles(deltaTime, canvasWidth, canvasHeight, walls);

        // Optionally, calculate and store the processing time for this update cycle
        long endTime = System.currentTimeMillis();
        lastProcessingTime = endTime - currentTime; // This is the time taken for this cycle in milliseconds
    }

    public void addParticle(Particle particle) {
        particleController.addParticle(particle);
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public long getLastProcessingTime() {
        return lastProcessingTime;
    }
}
