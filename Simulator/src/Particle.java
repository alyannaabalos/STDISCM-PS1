import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Point2D;
import java.awt.*;

public class Particle {
    double x, y, vx, vy;

    public Particle(int x, int y, double vx, double vy) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
    }

    void update(int canvasWidth, int canvasHeight) {
        x += vx;
        y += vy;

        if (x <= 5) {
            x = 5;
            vx *= -1;
        } else if (x >= canvasWidth - 5) {
            x = canvasWidth - 5;
            vx *= -1;
        }

        if (y <= 5) {
            y = 5;
            vy *= -1;
        } else if (y >= canvasHeight - 5) {
            y = canvasHeight - 5;
            vy *= -1;
        }
    }

    void checkWallCollisions(List<Wall> walls) {
        for (Wall wall : walls) {
            double toi = calculateTOI(wall);
            if (toi >= 0 && toi <= 1) {
                double pastX = this.x;
                double pastY = this.y;

                // Move to collision point
                x += vx * toi;
                y += vy * toi;

                List<Wall> nearestWalls = getNearestWalls(this, walls);

                if (!nearestWalls.isEmpty() && nearestWalls.size() >= 2) {
                    vx *= -1;
                    vy *= -1;

                } else {
                    Point2D.Double wallVector = new Point2D.Double(wall.x2 - wall.x1, wall.y2 - wall.y1);
                    Point2D.Double normal = new Point2D.Double(-wallVector.y, wallVector.x);
                    double length = Math.sqrt(normal.x * normal.x + normal.y * normal.y);
                    normal.x /= length;
                    normal.y /= length;

                    double dotProduct = vx * normal.x + vy * normal.y;
                    vx -= 2 * dotProduct * normal.x;
                    vy -= 2 * dotProduct * normal.y; 
                    
                }  

                x += vx * (1 - toi);
                y += vy * (1 - toi);
            }
        }
    }
    
    double calculateTOI(Wall wall) {
        double wx = wall.x2 - wall.x1;
        double wy = wall.y2 - wall.y1;

        double nx = -wy;
        double ny = wx;

        double ux = vx;
        double uy = vy;

        double px = x - wall.x1;
        double py = y - wall.y1;

        double numerator = nx * px + ny * py;
        double denominator = nx * ux + ny * uy;

        if (denominator == 0) return -1;

        double t = -numerator / denominator;

        if (t < 0 || t > 1) return -1;

        double collisionX = x + ux * t;
        double collisionY = y + uy * t;
        double wallDot = wx * wx + wy * wy; 

        double collisionDot = (collisionX - wall.x1) * wx + (collisionY - wall.y1) * wy; 

        if (collisionDot < 0 || collisionDot > wallDot) return -1;

        return t;
    }

    List<Wall> getNearestWalls(Particle particle, List<Wall> walls) {
        List<Wall> nearestWalls = new ArrayList<>();
        double velocity = Math.sqrt(particle.vx * particle.vx + particle.vy * particle.vy);
        double angle = Math.atan2(particle.vy, particle.vx);
    
        for (Wall wall : walls) {
            if (Math.abs(wall.x1 - particle.x) <= velocity || Math.abs(wall.x2 - particle.x) <= velocity) {
                if (Math.abs(wall.y1 - particle.y) <= velocity || Math.abs(wall.y2 - particle.y) <= velocity) {
                    nearestWalls.add(wall);
                }
            }
        }
        return nearestWalls;
    }

    void draw(Graphics g, int canvasHeight) {
        int invertedY = canvasHeight - (int)y - 5;
        g.fillOval((int)x - 5, invertedY, 10, 10);
    }
}