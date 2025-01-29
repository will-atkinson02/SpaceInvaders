package Utilities;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ImageLoader {
    ArrayList<ArrayList<Image>> alienImgsArray;
    public ArrayList<Image> crabImgs;
    Image crabA;
    Image crabB;

    public ArrayList<Image> squidImgs;
    Image squidA;
    Image squidB;

    public ArrayList<Image> octopusImgs;
    Image octopusA;
    Image octopusB;

    Image alienExplosion;

    public ArrayList<Image> ufoImgs;
    public Image ufoImg;

    public ArrayList<Image> shipImgs;
    public Image shipImgA;
    Image shipImgB;
    Image shipImgC;

    public ImageLoader() {
        String spritesFolder = "../../assets/sprites/";
        this.shipImgA = new ImageIcon(getClass().getResource(spritesFolder + "shipA.png")).getImage();
        this.shipImgB = new ImageIcon(getClass().getResource(spritesFolder + "shipB.png")).getImage();
        this.shipImgC = new ImageIcon(getClass().getResource(spritesFolder + "shipC.png")).getImage();

        this.crabA = new ImageIcon(getClass().getResource(spritesFolder + "alienA.png")).getImage();
        this.crabB = new ImageIcon(getClass().getResource(spritesFolder + "alienB.png")).getImage();

        this.squidA = new ImageIcon(getClass().getResource(spritesFolder + "squidA.png")).getImage();
        this.squidB = new ImageIcon(getClass().getResource(spritesFolder + "squidB.png")).getImage();

        this.octopusA = new ImageIcon(getClass().getResource(spritesFolder + "octopusA.png")).getImage();
        this.octopusB = new ImageIcon(getClass().getResource(spritesFolder + "octopusB.png")).getImage();

        this.alienExplosion = new ImageIcon(getClass().getResource(spritesFolder + "alien-explosion.png")).getImage();

        this.ufoImg = new ImageIcon(getClass().getResource(spritesFolder + "ufo.png")).getImage();

        createImageArrays();
    }

    public void createImageArrays() {
        this.shipImgs = new ArrayList<Image>();
        this.shipImgs.add(shipImgA);
        this.shipImgs.add(shipImgB);
        this.shipImgs.add(shipImgC);

        this.crabImgs = new ArrayList<Image>();
        this.crabImgs.add(crabA);
        this.crabImgs.add(crabB);
        this.crabImgs.add(alienExplosion);

        this.squidImgs = new ArrayList<Image>();
        this.squidImgs.add(squidA);
        this.squidImgs.add(squidB);
        this.squidImgs.add(alienExplosion);
        
        this.octopusImgs = new ArrayList<Image>();
        this.octopusImgs.add(octopusA);
        this.octopusImgs.add(octopusB);
        this.octopusImgs.add(alienExplosion);

        this.ufoImgs = new ArrayList<Image>();
        this.ufoImgs.add(ufoImg);
    } 
}
