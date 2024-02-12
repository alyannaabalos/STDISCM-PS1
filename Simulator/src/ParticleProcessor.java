import java.util.*;

public class ParticleProcessor implements Runnable {
    private ParticleController particleController;
    private WallController wallController;
    private int canvasWidth, canvasHeight;

    public ParticleProcessor(int canvasWidth, int canvasHeight, WallController wallController) {
        this.particleController = new ParticleController();
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.wallController = wallController;
    }

    @Override
    public void run() {
        // Check if particleController is null
        if (particleController == null) {
            System.err.println("Error: particleController is null");
            return;
        }

        // Check if canvasWidth and canvasHeight are valid
        if (canvasWidth <= 0 || canvasHeight <= 0) {
            System.err.println("Error: Invalid canvas dimensions");
            return;
        }

        // Check if wallController is null
        if (wallController == null) {
            System.err.println("Error: wallController is null");
            return;
        }

        // Check if wallController.getWall() returns null
        List<Wall> walls = wallController.getWall();
        if (walls == null) {
            System.err.println("Error: wallController.getWall() returned null");
            return;
        }

        // Call particleController.updateParticles() with valid parameters
        particleController.updateParticles(canvasWidth, canvasHeight, walls);
    }


    public void addParticle(Particle particle) {
        particleController.addParticle(particle);
    }

    public ParticleController getParticleController() {
        return particleController;
    }
}
