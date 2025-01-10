import javax.swing.*;
public class App {
    public static void main(String[] args) throws Exception {
        // window
        int tileSize = 36;
        int rows = 16;
        int cols = 16;
        int boardHeight = tileSize * rows;
        int boardWidth = tileSize * cols;

        JFrame frame = new JFrame("Space Invaders");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);
    }
}
