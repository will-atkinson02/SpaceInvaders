package Rendering;
import java.awt.*;
import java.util.ArrayList;

import Entities.*;
import Utilities.FontLoader;

public class EntityRenderer {
    public static void renderBullets(Graphics g, ArrayList<Entity> bulletArray, int bulletWidth, int bulletHeight) {
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Entity bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.fillRect(bullet.x, bullet.y, bulletWidth, bulletHeight);
            }
        }
    }

    public static void renderZigzagProjectile(Graphics g, Entity zigzag, ArrayList<Integer> zigzagConvolution, int tileSize) {
        if (zigzag != null) {
            g.setColor(Color.white);
            for (int i = 0; i < 7; i++) {
                g.fillRect(zigzag.x + zigzagConvolution.get(i), zigzag.y + 4*i, tileSize/8, tileSize/8);
            }
        }
    }

    public static void renderTProjectile(Graphics g, ArrayList<Entity> tArray, ArrayList<int[]> tShapesX, ArrayList<int[]> tShapesY, int animationFrame, int tileSize) {
        if (!tArray.isEmpty()) {
            g.setColor(Color.white);
            for (int i = 0; i < tArray.size(); i++) {
                Entity tProjectile = tArray.get(i);
                for (int j = 0; j < 8; j++) {
                    g.fillRect(tProjectile.x + tShapesX.get(animationFrame)[j], tProjectile.y + tShapesY.get(animationFrame)[j], tileSize/8, tileSize/8);
                }
            }
        }
    }

    public static void renderAliens(Graphics g, ArrayList<Entity> alienArray, int recentlyExploded, int explosionDuration, int alienWidth, int alienHeight, int alienSpriteState) {
        for (int i = 0; i < alienArray.size(); i++) {
            Entity alien = alienArray.get(i);
            if (i == recentlyExploded) {
                g.drawImage(alien.imgArray.get(2), alien.x, alien.y, alienWidth, alienHeight, null);
                
            }
            else if (alien.alive) {
                g.drawImage(alien.imgArray.get(alienSpriteState), alien.x, alien.y, alienWidth, alienHeight, null);
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
