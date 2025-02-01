package Controllers;

import java.util.ArrayList;

import Entities.*;
import GameState.GameState;

public class BulletController {
    public ArrayList<Bullet> bulletArray;

    public BulletController() {
        this.bulletArray = new ArrayList<Bullet>();
    }

    public void handleMovement(GameState gs, AlienArray alienArray, Ship ship, UFO ufo) {
        for (int i = 0; i < this.bulletArray.size(); i++) {
            Bullet bullet = this.bulletArray.get(i);
            bullet.y += bullet.velocityY;

            // bullet collision with aliens
            for (int j = 0; j < alienArray.alienArray.size(); j++) {
                Alien alien = alienArray.alienArray.get(j);
                if (!bullet.used && alien.alive && CollisionHandler.detectCollision(bullet, alien)) {
                    ship.reloadTime = 60;
                    alienArray.explodedAlienIndex = j;
                    bullet.used = true;
                    alien.alive = false;
                    alienArray.alienCount--;
                    gs.score += alien.points;
                }
            }

            // bullet collision with ufo
            if (ufo != null && CollisionHandler.detectCollision(bullet, ufo)) {
                bullet.used = true;
                ship.reloadTime = 60;
                gs.score += UFO.points();
                ufo.hit = true;
                ufo.velocityX = 0;
            }
        }
    }

    public void clearBullets() {
        while (this.bulletArray.size() > 0 && (this.bulletArray.get(0).used || this.bulletArray.get(0).y < 75)) {
            this.bulletArray.remove(0);
        }
    }
}
