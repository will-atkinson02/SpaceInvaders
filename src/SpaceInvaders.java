import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    //board
    int tileSize = 32;
    int rows = 24;
    int cols = 30;
    int boardHeight = tileSize*rows;
    int boardWidth = tileSize*cols;

    //images
    //ship
    ArrayList<Image> shipImgs;
    Image shipImg;
    
    //all alien images
    ArrayList<ArrayList<Image>> alienImgsArray;
    Image alienExplosion;

    //crab 
    ArrayList<Image> crabImgs;
    Image crabA;
    Image crabB;

    //squid
    ArrayList<Image> squidImgs;
    Image squidA;
    Image squidB;

    //octopus
    ArrayList<Image> octopusImgs;
    Image octopusA;
    Image octopusB;

    int spriteState = 0;

    //ship
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = (tileSize*cols)/2 - tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = 4;
    boolean movingLeft = false;
    boolean movingRight = false;
    Entity ship;

    //aliens
    ArrayList<Entity> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize*4;
    int alienY = tileSize*6;

    int alienRows = 5;
    int alienCols = 11;
    int alienCount = 0;
    int alienVelocityX = 16;
    int alienPosition = 0;

    //bullets
    ArrayList<Entity> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -16;

    //overlays
    JPanel titleOverlay;
    JPanel playAgainOverlay;

    //highscore
    HighScore highScoreManager = new HighScore();
    int highscore = highScoreManager.readHighScore();

    Font customFont = FontLoader.loadFont("PressStart2P-Regular.ttf");

    // movement
    boolean wallCollison = false;
    boolean allowMove = true;
    int tickRate = 24;
    
    // explosion duration
    int recentlyExploded = -1;
    int explosionDuration = 0;

    Timer gameLoop;
    int timerState = 0;
    int score = 0;
    int lives = 3;
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
        crabA = new ImageIcon(getClass().getResource(spritesFolder + "alienA.png")).getImage();
        crabB = new ImageIcon(getClass().getResource(spritesFolder + "alienB.png")).getImage();
        squidA = new ImageIcon(getClass().getResource(spritesFolder + "squidA.png")).getImage();
        squidB = new ImageIcon(getClass().getResource(spritesFolder + "squidB.png")).getImage();
        octopusA = new ImageIcon(getClass().getResource(spritesFolder + "octopusA.png")).getImage();
        octopusB = new ImageIcon(getClass().getResource(spritesFolder + "octopusB.png")).getImage();
        alienExplosion = new ImageIcon(getClass().getResource(spritesFolder + "alien-explosion.png")).getImage();

        // shipImgArray
        shipImgs = new ArrayList<Image>();
        shipImgs.add(shipImg);

        //crabs
        crabImgs = new ArrayList<Image>();
        crabImgs.add(crabA);
        crabImgs.add(crabB);
        crabImgs.add(alienExplosion);

        //squids
        squidImgs = new ArrayList<Image>();
        squidImgs.add(squidA);
        squidImgs.add(squidB);
        squidImgs.add(alienExplosion);
        
        //octopi
        octopusImgs = new ArrayList<Image>();
        octopusImgs.add(octopusA);
        octopusImgs.add(octopusB);
        octopusImgs.add(alienExplosion);

        ship = new Entity(shipX, shipY, shipWidth, shipHeight, shipImgs, 0.0, 0);
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
            bulletArray.clear();
            score = 0;
            alienVelocityX = 32;
            timerState = 0;
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
        g.drawLine(0, 75, 960, 75);
    }

    public void draw(Graphics g) {
        //ship
        g.drawImage(ship.imgArray.get(0), ship.x, ship.y, ship.width, ship.height, null);

        //aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Entity alien = alienArray.get(i);
            if (i == recentlyExploded) {
                g.drawImage(alien.imgArray.get(2), alien.x, alien.y, alienWidth, alienHeight, null);
                if (explosionDuration == 2) {
                    recentlyExploded = -1;
                    explosionDuration = 0;
                }
            }
            else if (alien.alive) {
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
        g.setFont(customFont);
        if (gameOver) {
            if (playAgainOverlay == null) {
                if (score > highscore) {
                    highScoreManager.writeHighScore(score);
                }
                initialisePlayAgainOverlay();
                showModalOverlay(playAgainOverlay);
            }
        } else {

            g.setFont(customFont);

            g.drawString("Score:",30, 32);
            g.drawString("0".repeat(6 - String.valueOf(score).length()) + String.valueOf(score), 30, 62);
            g.drawString("Highscore:",410, 32);
            g.drawString("0".repeat(6 -  String.valueOf(highscore).length()) + String.valueOf(highscore), 435, 62);
            g.drawString("Lives:", 825, 32);
            for (int i = 0; i < lives; i++) {
                g.drawImage(shipImg, 795 + shipWidth*3/4*i, 40, shipWidth*3/4, shipHeight*3/4, null);
            }
        }
    }

    public void move() {
        //aliens
        if (alienPosition >= tickRate) {
            if (!wallCollison) {
                for (int i = 0; i < alienArray.size(); i++) {
                    Entity alien = alienArray.get(i);
                    if (alien.alive) {
                        //check if aliens are touching walls
                        if (alien.x + alienWidth == boardWidth || alien.x == 0) {
                            wallCollison = true;
                            allowMove = false;
                            tickRate -= 2;
                            break;
                        }
                    }
                }
            } 

            if (!allowMove) {
                alienVelocityX *= -1;

                for (int i = 0; i < alienArray.size(); i++) {
                    Entity alien = alienArray.get(i);
                    if (alien.alive) {
                        if (alien.y + tileSize == ship.y) {
                            gameOver = true;
                        } else {
                            alien.y += tileSize;
                        }
                    }
                }
                allowMove = true;
            } else {
                for (int i = 0; i < alienArray.size(); i++) {
                    Entity alien = alienArray.get(i);
                    if (alien.alive) {
                        alien.x += alienVelocityX;  
                    }
                }
                wallCollison = false;
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
                    recentlyExploded = j;
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += alien.points;
                }
            }
        }

        //clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 75)) {
            bulletArray.remove(0); // stretch change bulletArray to a linked list
        }

        //next level
        if (alienCount == 0) {
            score += alienRows * alienCols * 100;
            alienArray.clear();
            bulletArray.clear();
            alienVelocityX = 8;
            timerState = 0;
            createAliens();
        }
    }

    public void createAliens() {
        for (int row = 0; row < alienRows; row++) {
            for (int col = 0; col < alienCols; col++) {
                ArrayList<Image> imgSet;
                double paddingRatio;
                int points; 

                if (row < 1) {
                    imgSet = squidImgs;
                    paddingRatio = 0.5;
                    points = 30;
                }
                else if (row < 3) {
                    imgSet = crabImgs;
                    paddingRatio = 0.3125;
                    points = 20;
                } else {
                    imgSet = octopusImgs;
                    paddingRatio = 0.25;
                    points = 10;
                }

                Entity alien = new Entity(
                        alienX + col*alienWidth,
                        alienY + row*alienHeight*3/2,
                        alienWidth,
                        alienHeight,
                        imgSet,
                        paddingRatio,
                        points
                    );

                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Entity a, Entity b) {
        int innerBx = b.x + (int)(b.width*b.paddingRatio/2);
        int innerBWidth = (int)(b.width*(1 - b.paddingRatio));
    
        return a.x < innerBx + innerBWidth &&
               a.x + a.width > innerBx &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timerState++;
        alienPosition++;
        
        if (timerState == tickRate) {
            spriteState = (spriteState == 0) ? 1 : 0;
            timerState = 0;
        }

        if (recentlyExploded >= 0) {
            explosionDuration++;
        }

        // Update ship's position based on key press flags
        if (movingLeft) {
            ship.x -= shipVelocityX;
        } else if (movingRight) {
            ship.x += shipVelocityX;
        }

        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            alienArray.clear();
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            movingLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth < boardWidth) {
            movingRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            movingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth < boardWidth) {
            movingRight = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Entity bullet = new Entity(ship.x + shipWidth*15/32, ship.y, bulletWidth, boardHeight, null, 0.0, 0);
            bulletArray.add(bullet);
        }
    }
}
