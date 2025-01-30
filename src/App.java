import javax.swing.*;
import GameState.GameState;

public class App {
    public static void main(String[] args) throws Exception {
        // window
        GameState gs = new GameState();

        JFrame frame = new JFrame("Space Invaders");
        frame.setVisible(true);
        frame.setSize(gs.boardWidth, gs.boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders(gs);
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);
    }
}
