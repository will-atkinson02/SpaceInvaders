package Entities;

import GameState.GameState;

public class Bullet extends Entity {
    public int velocityY;

    public Bullet(GameState gs, int x, int y) {
        super(x, y, gs.tileSize/8, gs.tileSize/2);
        this.velocityY = -16;
    }
}
