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
}