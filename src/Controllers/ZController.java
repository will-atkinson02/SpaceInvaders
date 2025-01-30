package Controllers;

import java.util.ArrayList;
import java.util.Random;

import Entities.*;
import GameState.GameState;

public class ZController {
    public ZProjectile zProjectile;
    public int zSpawnInterval;
    public int zSpawnTimer;

    public ZController() {
        this.zProjectile = null;
        this.zSpawnInterval = 90;
        this.zSpawnTimer = 0;
    }

    public void createZ(GameState gs, AlienArray alienArray, int alienIndex) {
        int y = alienArray.alienArray.get(alienIndex).y + gs.tileSize;
        int x = alienArray.alienArray.get(alienIndex).x + gs.tileSize*13/16;
        this.zProjectile = new ZProjectile(x, y, gs);
    }
    
    public void handleZSpawn(GameState gs, Random r, AlienArray alienArray, int alienIndex, ArrayList<Integer> rechargingAliens) {
        this.zSpawnTimer++;
        if (this.zSpawnTimer == this.zSpawnInterval) {
            alienIndex = r.nextInt(11);
            if (alienArray.aliveSquidCount > 3) {
                while (rechargingAliens.contains(alienIndex) || !alienArray.alienArray.get(alienIndex).alive) {
                    alienIndex = r.nextInt(11);
                }
                if (rechargingAliens.size() == 3) {
                    rechargingAliens.remove(0);
                }
                createZ(gs, alienArray, alienIndex);
            } else {
                rechargingAliens.clear();
                if (r.nextDouble() < 0.4) {
                    while (!alienArray.alienArray.get(alienIndex).alive) {

                        alienIndex = r.nextInt(11);
                    }
                    createZ(gs, alienArray, alienIndex);
                }
            }
            this.zSpawnTimer = 0;
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
