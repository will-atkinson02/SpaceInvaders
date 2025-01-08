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

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
    }
}
