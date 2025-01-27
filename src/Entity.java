import java.awt.*;
import java.util.ArrayList;

public class Entity {
    int x;
    int y;
    int width;
    int height;
    ArrayList<Image> imgArray;
    double paddingRatio;
    int points;
    boolean alive = true;
    boolean used = false;

    Entity(int x, int y, int width, int height, ArrayList<Image> imgArray, double paddingRatio, int points) {
        this.x = x;
        this.y = y; 
        this.width = width;
        this.height = height;
        this.imgArray = imgArray;
        this.paddingRatio = paddingRatio;
        this.points = points;
    }

    public double getPaddingRatio() {
        return paddingRatio;
    }

    public static boolean detectCollision(Entity a, Entity b) {
        int innerBx = b.x + (int)(b.width*b.paddingRatio/2);
        int innerBWidth = (int)(b.width*(1 - b.paddingRatio));
    
        return a.x < innerBx + innerBWidth &&
               a.x + a.width > innerBx &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }
}