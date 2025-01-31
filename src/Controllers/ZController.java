package Controllers;

import java.util.Random;

import Entities.*;
import GameState.GameState;

public class ZController {
    public ZProjectile zProjectile;
    public int spawnInterval;
    public int spawnTimer;

    public ZController() {
        this.zProjectile = null;
        this.spawnInterval = 90;
        this.spawnTimer = 0;
    }

    public void createZ(GameState gs, AlienArray alienArray, int alienIndex) {
        int y = alienArray.alienArray.get(alienIndex).y + gs.tileSize;
        int x = alienArray.alienArray.get(alienIndex).x + gs.tileSize*13/16;
        this.zProjectile = new ZProjectile(x, y, gs);
    }
    
    public void handleZSpawn(GameState gs, Random r, AlienArray alienArray, int alienIndex) {
        this.spawnTimer++;
        if (this.spawnTimer == this.spawnInterval) {
            alienIndex = r.nextInt(11);
            if (r.nextDouble() < 0.4) {
                if (alienArray.aliveSquidCount == 1) {
                    alienArray.rechargingAlien = -1;
                }

                while (alienIndex == alienArray.rechargingAlien || !alienArray.alienArray.get(alienIndex).alive) {
                    alienIndex = r.nextInt(11);
                }

                if (alienArray.aliveSquidCount > 1) {
                    createZ(gs, alienArray, alienIndex);
                    alienArray.rechargingAlien = alienIndex;
                } else {
                    if (r.nextDouble() < 0.2) {
                        createZ(gs, alienArray, alienIndex);
                    }
                }
            }
            this.spawnTimer = 0;
        }
    }

    public void handleZMovement(GameState gs, Ship ship) {
        if (zProjectile != null) {
            if (zProjectile.y >= gs.boardHeight) {
                zProjectile = null;
            } else {
                zProjectile.y += 8;

                if (CollisionHandler.detectCollision(zProjectile, ship)) {
                    ship.hit();
                    zProjectile = null;
                } 
            }
        }
    }
}
