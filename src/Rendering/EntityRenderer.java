package Rendering;
import java.awt.*;
import java.util.ArrayList;

import Entities.*;
import Utilities.FontLoader;

public class EntityRenderer {
    public static void renderBullets(Graphics g, ArrayList<Bullet> bulletArray) {
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Bullet bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }
    }

    public static void renderZProjectile(Graphics g, ZProjectile zProjectile, int tileSize) {
        if (zProjectile != null) {
            g.setColor(Color.white);
            for (int i = 0; i < 7; i++) {
                g.fillRect(zProjectile.x + zProjectile.zConvolution.get(i), zProjectile.y + 4*i, tileSize/8, tileSize/8);
            }
        }
    }

    public static void renderTProjectile(Graphics g, ArrayList<TProjectile> tArray, int tileSize) {
        if (!tArray.isEmpty()) {
            g.setColor(Color.white);
            for (int i = 0; i < tArray.size(); i++) {
                TProjectile t = tArray.get(i);
                for (int j = 0; j < 8; j++) {
                    g.fillRect(t.x + t.tShapesX.get(t.animationFrame)[j], t.y + t.tShapesY.get(t.animationFrame)[j], tileSize/8, tileSize/8);
                }
            }
        }
    }

    public static void renderAliens(Graphics g, ArrayList<Alien> alienArray, int recentlyExploded, int explosionDuration, int alienSpriteState) {
        for (int i = 0; i < alienArray.size(); i++) {
            Alien alien = alienArray.get(i);
            if (i == recentlyExploded) {
                g.drawImage(alien.imgArray.get(2), alien.x, alien.y, alien.width, alien.height, null);
            }
            else if (alien.alive) {
                g.drawImage(alien.imgArray.get(alienSpriteState), alien.x, alien.y, alien.width, alien.height, null);
            }
        }
    }

    public static void renderUFO(Graphics g, UFO ufo, Font customFont, Image ufoImg) {
        if (ufo.isActive) {
            if (ufo.hit) {
                g.setColor(Color.white);
                g.setFont(FontLoader.customiseFont(customFont, 0, 30));
                int xShift;
                if (ufo.points < 100) {
                    xShift = 8;
                } else {
                    xShift = 4;
                }

                //flash the point 0th-8th frames and then 16th-24th frames
                if (ufo.explosionTimer < 8) {
                    g.drawString(Integer.toString(ufo.points), ufo.x + xShift, ufo.y + 23);
                } else if (ufo.explosionTimer < 24 && ufo.explosionTimer > 16) {
                    g.drawString(Integer.toString(ufo.points), ufo.x + xShift, ufo.y + 23);
                }
            } else {
                g.drawImage(ufoImg, ufo.x, ufo.y, ufo.width, ufo.height, null);
            }
        } 
    }
}
