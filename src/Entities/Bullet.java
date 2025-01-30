package Entities;

import java.awt.Image;
import java.util.ArrayList;

import GameState.GameState;

public class Bullet extends Entity {
    public int velocityY;

    public Bullet(GameState gs, int x, int y, ArrayList<Image> imgArray) {
        super(x, y, gs.tileSize/8, gs.tileSize/2, imgArray);
        this.velocityY = -16;
    }
}
