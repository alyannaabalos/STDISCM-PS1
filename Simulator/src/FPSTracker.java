public class FPSTracker {
    private long lastTimeCheck; // Store the last time we checked the FPS
    private int frameCount; // Frames counted in the current second
    private int fps; 

    public FPSTracker() {
        lastTimeCheck = System.currentTimeMillis();
        frameCount = 0;
        fps = 0;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastTimeCheck;

        frameCount++;

        if (elapsedTime >= 1000) { // If a second or more has passed
            fps = frameCount;
            frameCount = 0; // Reset frame count for the next second
            lastTimeCheck = currentTime;
        }
    }

    public int getFPS() {
        return fps;
    }
}
