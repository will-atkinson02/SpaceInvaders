package Controllers;

import Entities.*;

public class CollisionHandler {
    public static boolean detectCollision(Entity a, Entity b) {
        int bX = b.x;
        int bWidth = b.width;

        if (b instanceof Alien) {
            bX = b.x + (int)(b.width*b.getPaddingRatio()/2);
            bWidth = (int)(b.width*(1 - b.getPaddingRatio()));
        }

        return a.x < bX + bWidth &&
               a.x + a.width > bX &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }
}
