package Controllers;

import java.util.*;

import Entities.*;
import GameState.GameState;

public class TController {
    public ArrayList<TProjectile> tArray;
    public int spawnInterval;
    public int spawnTimer;

    public TController() {
        this.tArray = new ArrayList<TProjectile>();
        this.spawnInterval = 30;
        this.spawnTimer = 0;
    }

    public void createTProjectile(GameState gs, AlienArray alienArray, int alienIndex) {
        int y = alienArray.alienArray.get(alienIndex).y + gs.tileSize;
        int x = alienArray.alienArray.get(alienIndex).x + gs.tileSize*13/16;
        TProjectile tProjectile = new TProjectile(x, y, gs);
        tArray.add(tProjectile);
    }

    public void handleAllTAnimations() {
        if (!tArray.isEmpty()) {
            for (TProjectile t : tArray) {
                t.handleTProjectileAnimation();
            }
        }
    }

    public void handleTProjectileSpawn(GameState gs, Random r, AlienArray alienArray) {
        if (tArray.size() <= 3) {
            this.spawnTimer++;
            if (this.spawnTimer == this.spawnInterval) {
                int alienIndex = r.nextInt(11);
                if (r.nextDouble() < 0.4) {
                    if (alienArray.aliveSquidCount == 1) {
                        alienArray.rechargingAlien = -1;
                    }

                    while (alienIndex == alienArray.rechargingAlien || !alienArray.alienArray.get(alienIndex).alive) {
                        alienIndex = r.nextInt(11);
                    }

                    if (alienArray.aliveSquidCount > 1) {
                        alienArray.rechargingAlien = alienIndex;
                        createTProjectile(gs, alienArray, alienIndex);
                    } else {
                        if (r.nextDouble() < 0.2) {
                            createTProjectile(gs, alienArray, alienIndex);
                        }
                    }
                }
                this.spawnTimer = 0;
            }
        }
    }

    public void handleTMovement(GameState gs, Ship ship) {
        if (!this.tArray.isEmpty()) {
            for (int i = 0; i < tArray.size(); i++) {
                TProjectile t = tArray.get(i);

                if (t.y == gs.boardHeight) {
                    tArray.remove(i);
                } else {
                    t.y += 8;

                    if (CollisionHandler.detectCollision(t, ship)) {
                        ship.hit();
                        this.tArray.remove(i);
                    } 
                }
            }
        }
    }
}
