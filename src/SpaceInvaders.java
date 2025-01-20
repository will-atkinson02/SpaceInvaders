import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
//import java.util.Random;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    //board
    int tileSize = 32;
    int rows = 20;
    int cols = 28;
    int boardHeight = tileSize*rows;
    int boardWidth = tileSize*cols;

    //images
    //ship images
    ArrayList<Image> shipImgs;
    Image shipImg;
    
    // Image alienCyanImage;
    // Image alienMagentaImage;
    // Image alienYellowImage;
    //green alien images
    ArrayList<ArrayList<Image>> alienImgsArray; 
    ArrayList<Image> greenAlienImgs;
    Image greenAlienA;
    Image greenAlienB;

    int spriteState = 0;

    //ship
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = (tileSize*cols)/2 - tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = tileSize;
    Entity ship;

    //aliens
    ArrayList<Entity> alienArray;
    int alienWidth = tileSize*11/8;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 5;
    int alienCols = 11;
    int alienCount = 0;
    int alienSpeedX = 16;
    int alienDirectionX = 1;
    int alienVelocity = alienSpeedX*alienDirectionX;

    int alienPosition = 0;

    //bullets
    ArrayList<Entity> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10;

    JPanel titleOverlay;
    JPanel playAgainOverlay;

    HighScore highScoreManager = new HighScore();
    int highscore = highScoreManager.readHighScore();

    Timer gameLoop;
    int timerState = 0;
    int score = 0;
    boolean gameOver = false;

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        initialiseTitleModalOverlay();

        //load images
        String spritesFolder = "C:/Users/Will Atkinson/Documents/Coding/Coding projects 2025/SpaceInvaders/sprites/";
        shipImg = new ImageIcon(getClass().getResource(spritesFolder + "ship.png")).getImage();
        greenAlienA = new ImageIcon(getClass().getResource(spritesFolder + "alienA.png")).getImage();
        greenAlienB = new ImageIcon(getClass().getResource(spritesFolder + "alienB.png")).getImage();
        // alienCyanImage = new ImageIcon(getClass().getResource(spritesFolder + "alien-cyan.png")).getImage();
        // alienMagentaImage = new ImageIcon(getClass().getResource(spritesFolder + "alien-magenta.png")).getImage();
        // alienYellowImage = new ImageIcon(getClass().getResource(spritesFolder + "alien-yellow.png")).getImage();

        // shipImgArray
        shipImgs = new ArrayList<Image>();
        shipImgs.add(shipImg);

        greenAlienImgs = new ArrayList<Image>();
        greenAlienImgs.add(greenAlienA);
        greenAlienImgs.add(greenAlienB);
        // alienImgArray.add(alienImg);
        // alienImgArray.add(alienCyanImage);
        // alienImgArray.add(alienMagentaImage);
        // alienImgArray.add(alienYellowImage);

        ship = new Entity(shipX, shipY, shipWidth, shipHeight, shipImgs);
        alienArray = new ArrayList<Entity>();
        bulletArray = new ArrayList<Entity>();

        //game timer
        gameLoop = new Timer(1000/60, this);
        createAliens();

        showModalOverlay(titleOverlay);
    }

    public void initialiseTitleModalOverlay() {
        initialiseJPanel("SPACE INVADERS", "Play", true);
    }

    public void initialisePlayAgainOverlay() {
        String text = "Score: " + Integer.toString(this.score);
        initialiseJPanel(text, "Play again", false);
    }

    private void initialiseJPanel(String labelText, String buttonText, boolean isTitle) {
        JPanel modal = new JPanel();
        modal.setBackground(new Color(0, 0, 0, 200));
        modal.setPreferredSize(new Dimension(boardWidth, boardHeight));
        modal.setLayout(new GridBagLayout());

        JLabel titleLabel = new JLabel(labelText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        JButton startButton = new JButton(buttonText);
        startButton.setFont(new Font("Arial", Font.PLAIN, 25));
        startButton.setForeground(Color.white);
        startButton.setBackground(new Color(40, 0, 128));
        startButton.setMargin(new Insets(5, 10, 5, 10));
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                startButton.setBackground(new Color(80, 0, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                startButton.setBackground(new Color(40, 0, 128));
            }
        });
        startButton.addActionListener(e -> handleButtonAction(buttonText, isTitle));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridy = 0;
        modal.add(titleLabel, gbc);
        gbc.gridy = 1;
        modal.add(startButton, gbc);

        if (isTitle) {
            this.titleOverlay = modal;
        } else {
            this.playAgainOverlay = modal;
        }
    }

    public void handleButtonAction(String buttonText, boolean isTitle) {
        if (isTitle) {
            removeModalOverlay(this.titleOverlay);
            gameLoop.start();
        } else {
            removeModalOverlay(this.playAgainOverlay);
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienSpeedX = 10;
            alienDirectionX = 10;
            alienCols = 3;
            alienRows = 2;
            createAliens();
            gameLoop.start();
            gameOver = false;
        }
    }

    private void showModalOverlay(JPanel modal) {
        add(modal);
        repaint();
        revalidate();
    }

    private void removeModalOverlay(JPanel modal) {
        remove(modal);
        repaint();
        revalidate();
        playAgainOverlay = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //ship
        g.drawImage(ship.imgArray.get(0), ship.x, ship.y, ship.width, ship.height, null);

        //aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Entity alien = alienArray.get(i);
            if (alien.alive) {
                g.drawImage(alien.imgArray.get(spriteState), alien.x, alien.y, alienWidth, alienHeight, null);
            }
        }

        //bullets
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Entity bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.fillRect(bullet.x, bullet.y, bulletWidth, bulletHeight);
            }
        }

        //score 
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            if (playAgainOverlay == null) {
                if (score > highscore) {
                    highScoreManager.writeHighScore(score);
                }
                initialisePlayAgainOverlay();
                showModalOverlay(playAgainOverlay);
            }
        } else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    public void move() {
        //aliens
        boolean wallCollison = false;
        for (int i = 0; i < alienArray.size(); i++) {
            Entity alien = alienArray.get(i);
            if (alien.alive) {
                if (alien.y >= ship.y) {
                    gameOver = true;
                }

                //if alien touches borders
                if (alien.x + alienWidth*3/2 >= boardWidth || alien.x <= alienWidth/2) {
                    wallCollison = true;
                    break;
                }
            }
        }

        // If any alien hit the border, reverse the direction for all and move them down
        if (wallCollison) {
            alienDirectionX *= -1;

            for (int i = 0; i < alienArray.size(); i++) {
                Entity alien = alienArray.get(i);
                if (alien.alive) {
                    alien.x += alienVelocity*2;
                    alien.y += tileSize;
                }
            }

            wallCollison = false;
        }
        
        // Update positions of all aliens that haven't hit the borders yet
        if (alienPosition >= 30) {
            for (int i = 0; i < alienArray.size(); i++) {
                Entity alien = alienArray.get(i);
                if (alien.alive) {
                    System.out.println(alienPosition);
                    alien.x += alienVelocity;  
                }
            }
            alienPosition = 0;
        }
        

        //bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Entity bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.size(); j++) {
                Entity alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        //clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0); // stretch change bulletArray to a linked list
        }

        //next level
        if (alienCount == 0) {
            score += alienRows * alienCols * 100;

            alienCols = Math.min(alienCols + 1, cols/2 - 2);
            alienRows = Math.min(alienRows + 1, rows - 6);
            alienArray.clear();
            bulletArray.clear();
            alienSpeedX = 10;
            alienDirectionX = 1;
            createAliens();
        }
    }

    public void createAliens() {
        // Random random = new Random();
        for (int row = 0; row < alienRows; row++) {
            for (int col = 0; col < alienCols; col++) {
                // int randomImgIndex = random.nextInt(alienImgsArray.size());
                Entity alien = new Entity(
                    alienX + col*alienWidth,
                    alienY + row*alienHeight,
                    alienWidth,
                    alienHeight,
                    greenAlienImgs
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Entity a, Entity b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timerState++;
        alienPosition++;
        if (timerState == 30) {
            spriteState = (spriteState == 0) ? 1 : 0;
            timerState = 0;
        }

        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            ship.x -= shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth < boardWidth) {
            ship.x += shipVelocityX;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Entity bullet = new Entity(ship.x + shipWidth*15/32, ship.y, bulletWidth, boardHeight, null);
            bulletArray.add(bullet);
        }
    }
}
