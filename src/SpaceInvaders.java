import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel {
    //board
    int tileSize = 32;
    int rows = 16;
    int cols = 16;
    int boardHeight = tileSize * rows;
    int boardWidth = tileSize * cols;

    Image shipImg;
    Image alienImg;
    Image alienCyanImage;
    Image alienMagentaImage;
    Image alienYellowImage;
    ArrayList<Image> alienImgArray; 

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);

        //load images
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage();
        alienCyanImage = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage();
        alienMagentaImage = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage();
        alienYellowImage = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImage);
        alienImgArray.add(alienMagentaImage);
        alienImgArray.add(alienYellowImage);

    }
}
