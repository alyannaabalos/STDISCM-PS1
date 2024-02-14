import java.awt.*;

public class Wall {
    int x1, y1, x2, y2;

    public Wall(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    void draw(Graphics g, int canvasHeight) {
        int invertedY1 = canvasHeight - y1;
        int invertedY2 = canvasHeight - y2;
        g.drawLine(x1, invertedY1, x2, invertedY2);
    }
}