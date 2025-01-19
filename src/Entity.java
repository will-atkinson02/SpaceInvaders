import java.awt.*;
import java.util.ArrayList;
//import java.util.Random;

public class Entity {
    int x;
    int y;
    int width;
    int height;
    ArrayList<Image> imgArray;
    boolean alive = true;
    boolean used = false;

    Entity(int x, int y, int width, int height, ArrayList<Image> imgArray) {
        this.x = x;
        this.y = y; 
        this.width = width;
        this.height = height;
        this.imgArray = imgArray;
    }
}
