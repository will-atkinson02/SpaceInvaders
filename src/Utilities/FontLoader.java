package Utilities;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    public static Font loadFont(String fileName) {
        String fontPath = "C:/Users/Will Atkinson/Documents/Coding/Coding projects 2025/SpaceInvaders/assets/fonts/Visitor/";

        try {
            // Load the custom font
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath + fileName));
            // Derive the font with a specific size
            return customFont;
        } catch (FontFormatException e) {
            System.err.println("Invalid font format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading font file: " + e.getMessage());
        }
        return null; // Return null if the font couldn't be loaded
    }

    public static Font customiseFont(Font font, int style, int size) {
        return font.deriveFont(style, size);
    }
}
