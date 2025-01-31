package Entities;

import java.util.ArrayList;
import GameState.GameState;

public class ZProjectile extends Entity {
    public ArrayList<Integer> zConvolution;

    public int zAnimationInterval;
    public int zAnimationTimer;

    public int zSpawnInterval;
    public int zSpawnTimer;

    public int alienIndex;

    public ZProjectile(int x, int y, GameState gs) {
        super(x, y, gs.tileSize*3/8, gs.tileSize*7/8);
        this.zConvolution = initialiseZConvolution();

        this.zAnimationInterval = 5;
        this.zAnimationTimer = 0;
    }

    public ArrayList<Integer> initialiseZConvolution() {
        zConvolution = new ArrayList<Integer>();
        zConvolution.add(0);
        zConvolution.add(4);
        zConvolution.add(8);
        zConvolution.add(4);
        zConvolution.add(0);
        zConvolution.add(4);
        zConvolution.add(8);
        zConvolution.add(4);

        return zConvolution;
    }

    public void shuffleZConvolution() {
        this.zAnimationTimer++;
        if (this.zAnimationTimer == this.zAnimationInterval) {
            moveFirstToEnd(this.zConvolution);
            this.zAnimationTimer = 0;
        }
    }
    public void moveFirstToEnd(ArrayList<Integer> list) {
        Integer firstElement = list.remove(0);
        list.add(firstElement);
    }
}
