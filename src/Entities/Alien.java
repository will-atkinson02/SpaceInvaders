package Entities;

import java.awt.Image;
import java.util.ArrayList;

import GameState.GameState;

public class Alien extends Entity {
    public ArrayList<Image> imgArray;
    public int points;
    public double paddingRatio;

    public Alien(GameState gs, int x, int y, ArrayList<Image> imgArray, double paddingRatio, int points) {
        super(gs.tileSize * 4 + x * gs.tileSize * 2, gs.tileSize * 4 + y * gs.tileSize * 3 / 2, gs.tileSize * 2,
                gs.tileSize);
        this.imgArray = imgArray;
        this.points = points;
        this.paddingRatio = paddingRatio;
    }

    @Override
    public double getPaddingRatio() {
        return paddingRatio;
    }
}