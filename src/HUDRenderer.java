import java.awt.*;

public class HUDRenderer {
    public static void renderHUD(Graphics g, Font customFont, GameState gameState, int highscore, int lives, Image shipImgA, int shipWidth, int shipHeight) {
        if (!gameState.gameOver) {
            if (gameState.score > highscore) {
                highscore = gameState.score;
            }

            g.setFont(FontLoader.customiseFont(customFont, 0, 30));

            g.setColor(new Color(62, 250, 47));
            g.drawString("0".repeat(6 - String.valueOf(gameState.score).length()) + String.valueOf(gameState.score), 125, 62);
            g.drawString("0".repeat(6 -  String.valueOf(highscore).length()) + String.valueOf(highscore), 435, 62);
            
            g.setColor(Color.white);
            g.drawString("Score:",130, 32);
            g.drawString("Highscore:",405, 32);
            g.drawString("Lives:", 735, 32);

            for (int i = 0; i < lives; i++) {
                g.drawImage(shipImgA, 705 + shipWidth*3/4*i, 40, shipWidth*3/4, shipHeight*3/4, null);
            }
        }
    }
}
