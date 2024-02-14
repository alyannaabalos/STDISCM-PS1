import java.awt.*;
import java.util.*;
import java.util.List;

public class WallController {
    private final List<Wall> walls = new ArrayList<>(); 

    void addWall(Wall newWall) {
        this.walls.add(newWall);
    }

    void addWall(int x1, int y1, int x2, int y2) {
        this.walls.add(new Wall(x1, y1, x2, y2));
    }

    List<Wall> getWall(){
        return this.walls;
    }

    public void drawWalls(Graphics g, int canvasHeight) {
        for (Wall wall : walls) {
            wall.draw(g, canvasHeight);
        }
    }
}