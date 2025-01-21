import java.awt.*;
import java.util.ArrayList;

public class Entity {
    int x;
    int y;
    int width;
    int height;
    ArrayList<Image> imgArray;
    double paddingRatio;
    boolean alive = true;
    boolean used = false;

    Entity(int x, int y, int width, int height, ArrayList<Image> imgArray, double paddingRatio) {
        this.x = x;
        this.y = y; 
        this.width = width;
        this.height = height;
        this.imgArray = imgArray;
        this.paddingRatio = paddingRatio;
    }

    public double getPaddingRatio() {
        return paddingRatio;
    }
}