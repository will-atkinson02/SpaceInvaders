package Entities;

import java.util.ArrayList;

import GameState.GameState;

public class TProjectile extends Entity {
    public ArrayList<int[]> tShapesX;
    public ArrayList<int[]> tShapesY;

    public int animationFrame;
    public int animationDirection;

    public int animationInterval = 5;
    public int animationTimer = 0;


    public TProjectile(int x, int y, GameState gs) {
        super(x, y, gs.tileSize*3/8, gs.tileSize*7/8);
        this.tShapesX = initaliseTShapesX();
        this.tShapesY = initaliseTShapesY();
        this.animationFrame = 3;
        this.animationDirection = -1;
        this.animationInterval = 5;
        this.animationTimer = 0;
    }

    public ArrayList<int[]> initaliseTShapesX() {
        tShapesX = new ArrayList<int[]>();

        tShapesX.add(new int[]{0, 4, 8, 4, 4, 4, 4, 4});
        tShapesX.add(new int[]{4, 4, 0, 4, 8, 4, 4, 4});
        tShapesX.add(new int[]{4, 4, 4, 0, 4, 8, 4, 4});
        tShapesX.add(new int[]{4, 4, 4, 4, 4, 0, 4, 8});

        return tShapesX;
    }

    public ArrayList<int[]> initaliseTShapesY() {
        tShapesY = new ArrayList<int[]>();

        tShapesY.add(new int[]{0, 0, 0, 4, 8, 12, 16, 20});
        tShapesY.add(new int[]{0, 4, 8, 8, 8, 12, 16, 20});
        tShapesY.add(new int[]{0, 4, 8, 12, 12, 12, 16, 20});
        tShapesY.add(new int[]{0, 4, 8, 12, 16, 20, 20, 20});

        return tShapesY;
    }

    public void handleTProjectileAnimation() {
        this.animationTimer++;
        if (this.animationTimer == this.animationInterval) {
            if (this.animationFrame == 3 && this.animationDirection == 1) {
                this.animationDirection = -1;
            } else if (this.animationFrame == 0 && this.animationDirection == -1) {
                this.animationDirection = 1;
            }
            this.animationFrame += this.animationDirection;
            this.animationTimer = 0;
        }
        
    }
}
