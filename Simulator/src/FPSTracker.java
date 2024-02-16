public class FPSTracker {
    private long lastTimeCheck; 
    private int frameCount; 
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

        if (elapsedTime >= 1000) {
            fps = frameCount;
            frameCount = 0; 
            lastTimeCheck = currentTime;
        }
    }

    public int getFPS() {
        return fps;
    }
}
