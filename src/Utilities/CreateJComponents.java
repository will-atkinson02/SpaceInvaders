package Utilities;
import java.awt.*;
import javax.swing.*;

public class CreateJComponents {

    public static JPanel createJPanel(int boardWidth, int boardHeight) {
        JPanel modal = new JPanel();
        modal.setBackground(new Color(0, 0, 0, 200));
        modal.setPreferredSize(new Dimension(boardWidth, boardHeight));
        modal.setLayout(new GridBagLayout());

        return modal;
    }

    public static JLabel createTitleLabel(String labelText, Font customFont) {
        JLabel titleLabel = new JLabel(labelText);
        titleLabel.setFont(FontLoader.customiseFont(customFont, 0, 50));
        titleLabel.setForeground(Color.WHITE);

        return titleLabel;
    }

    public static JButton createJButton(String buttonText, Font customFont, boolean isTitle) {
        JButton startButton = new JButton(buttonText);
        startButton.setFont(FontLoader.customiseFont(customFont, 0, 35));

        startButton.setForeground(Color.white);
        startButton.setBackground(new Color(0, 204, 0));

        startButton.setMargin(new Insets(20, 10, 20, 10));

        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                startButton.setBackground(new Color(0, 102, 0));
                startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                startButton.setBackground(new Color(0, 204, 0));
                startButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }
        });

        return startButton;
    }
}
