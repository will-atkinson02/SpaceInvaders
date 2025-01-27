//TODO
//make code more modular and readable - draw completed
//ensure state is reset on play again or increased correctly on level complete
//improve overlays
//add sound effects and music
// BONUS add green shield things, alien bullet collisions with floor

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    //board
    int tileSize = 32;
    int rows = 22;
    int cols = 30;
    int boardHeight = tileSize*rows;
    int boardWidth = tileSize*cols;

    //images
    ArrayList<ArrayList<Image>> alienImgsArray;
    Image alienExplosion;

    ArrayList<Image> crabImgs;
    Image crabA;
    Image crabB;

    ArrayList<Image> squidImgs;
    Image squidA;
    Image squidB;

    ArrayList<Image> octopusImgs;
    Image octopusA;
    Image octopusB;

    ArrayList<Image> ufoImgs;
    Image ufoImg;

    ArrayList<Image> shipImgs;
    Image shipImgA;
    Image shipImgB;
    Image shipImgC;

    //properties
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = (tileSize*cols)/2 - tileSize;
    int shipY = boardHeight - tileSize*2;
    int shipVelocityX = 8;
    boolean movingLeft = false;
    boolean movingRight = false;
    int reloadTime = 30;
    boolean shipHit = false;
    Entity ship;

    ArrayList<Entity> alienArray;
    int alienRows = 5;
    int alienCols = 11;
    int alienCount = 0;
    
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize*4;
    int alienY = tileSize*11; 
    int alienVelocityX = 8;

    Entity ufoEntity = null;
    int ufoX;
    int ufoY = tileSize*4;
    int ufoVelocityX = 4;
    boolean ufoHit = false;
    boolean allowUFOs = false;

    ArrayList<Entity> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -16;

    //states
    boolean wallCollison = false;
    boolean allowMove = true;

    int tickRate = 24;
    int alienSpriteState = 0;
    int shipSpriteState = 0;
    int shipTimer = 0;
    int timerState = 0;
    int ufoCount = 0;
    int alienPosition = 0;

    int recentlyExploded = -1;
    int explosionDuration = 0;

    int score = 0;
    int lives = 3;
    int level = 0;
    boolean gameOver = false;

    //zigzag projectile
    Entity zigzag;
    ArrayList<Integer> zigzagConvolution;

    int zigzagAnimationInterval = 5;
    int zigzagAnimationTimer = 0;

    int zigzagSpawnInterval = 90;
    int zigzagSpawnTimer = 0;

    int alienIndex;

    //t projectile
    ArrayList<Entity> tArray;
    ArrayList<int[]> tShapesX = new ArrayList<>();
    ArrayList<int[]> tShapesY = new ArrayList<>();

    int tAnimationInterval = 5;
    int tAnimationTimer = 0;

    int tSpawnInterval = 60;
    int tSpawnTimer = 0;

    int animationDirection = -1;
    int animationFrame = 4;

    //prevent same alien firing twice
    ArrayList<Integer> rechargingAliens;

    //miscellaneous
    JPanel titleOverlay;
    JPanel playAgainOverlay;

    Timer gameLoop;

    HighScore highScoreManager = new HighScore();
    int highscore = highScoreManager.readHighScore();

    Font customFont = FontLoader.loadFont("visitor1.ttf");

    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        initialiseTitleModalOverlay();

        //load images
        String spritesFolder = "C:/Users/Will Atkinson/Documents/Coding/Coding projects 2025/SpaceInvaders/sprites/";
        shipImgA = new ImageIcon(getClass().getResource(spritesFolder + "shipA.png")).getImage();
        shipImgB = new ImageIcon(getClass().getResource(spritesFolder + "shipB.png")).getImage();
        shipImgC = new ImageIcon(getClass().getResource(spritesFolder + "shipC.png")).getImage();

        crabA = new ImageIcon(getClass().getResource(spritesFolder + "alienA.png")).getImage();
        crabB = new ImageIcon(getClass().getResource(spritesFolder + "alienB.png")).getImage();
       
        squidA = new ImageIcon(getClass().getResource(spritesFolder + "squidA.png")).getImage();
        squidB = new ImageIcon(getClass().getResource(spritesFolder + "squidB.png")).getImage();
       
        octopusA = new ImageIcon(getClass().getResource(spritesFolder + "octopusA.png")).getImage();
        octopusB = new ImageIcon(getClass().getResource(spritesFolder + "octopusB.png")).getImage();
        
        alienExplosion = new ImageIcon(getClass().getResource(spritesFolder + "alien-explosion.png")).getImage();
        
        ufoImg = new ImageIcon(getClass().getResource(spritesFolder + "ufo.png")).getImage();

        //create image arrays
        shipImgs = new ArrayList<Image>();
        shipImgs.add(shipImgA);
        shipImgs.add(shipImgB);
        shipImgs.add(shipImgC);

        crabImgs = new ArrayList<Image>();
        crabImgs.add(crabA);
        crabImgs.add(crabB);
        crabImgs.add(alienExplosion);

        squidImgs = new ArrayList<Image>();
        squidImgs.add(squidA);
        squidImgs.add(squidB);
        squidImgs.add(alienExplosion);
        
        octopusImgs = new ArrayList<Image>();
        octopusImgs.add(octopusA);
        octopusImgs.add(octopusB);
        octopusImgs.add(alienExplosion);

        ufoImgs = new ArrayList<Image>();
        ufoImgs.add(ufoImg);

        //initalise entities
        ship = new Entity(shipX, shipY, shipWidth, shipHeight, shipImgs, 0.0, 0);

        alienArray = new ArrayList<Entity>();
        bulletArray = new ArrayList<Entity>();

        createAliens();

        gameLoop = new Timer(1000/60, this);
        
        showModalOverlay(titleOverlay);

        zigzagConvolution = new ArrayList<Integer>();
        zigzagConvolution.add(0);
        zigzagConvolution.add(4);
        zigzagConvolution.add(8);
        zigzagConvolution.add(4);
        zigzagConvolution.add(0);
        zigzagConvolution.add(4);
        zigzagConvolution.add(8);
        zigzagConvolution.add(4);

        tArray = new ArrayList<Entity>();
        rechargingAliens = new ArrayList<Integer>();
        
        tShapesX = new ArrayList<int[]>();
        tShapesY = new ArrayList<int[]>();
            

        //standard T
        tShapesX.add(new int[]{0, 4, 8, 4, 4, 4, 4, 4});
        tShapesY.add(new int[]{0, 0, 0, 4, 8, 12, 16, 20});

        //Top-heavy middle cross
        tShapesX.add(new int[]{4, 4, 0, 4, 8, 4, 4, 4});
        tShapesY.add(new int[]{0, 4, 8, 8, 8, 12, 16, 20});

        //Bottom-heavy middle cross
        tShapesX.add(new int[]{4, 4, 4, 0, 4, 8, 4, 4});
        tShapesY.add(new int[]{0, 4, 8, 12, 12, 12, 16, 20});

        //upside-down T
        tShapesX.add(new int[]{4, 4, 4, 4, 4, 0, 4, 8});
        tShapesY.add(new int[]{0, 4, 8, 12, 16, 20, 20, 20});
    }

    public void initialiseTitleModalOverlay() {
        initialiseJPanel("SPACE INVADERS", "Play", true);
    }

    public void initialisePlayAgainOverlay() {
        String text = "Score: " + Integer.toString(this.score);
        initialiseJPanel(text, "Play again", false);
    }

    private void initialiseJPanel(String labelText, String buttonText, boolean isTitle) {
        JPanel modal = CreateJComponents.createJPanel(boardWidth, boardHeight);
        JLabel titleLabel = CreateJComponents.createTitleLabel(labelText, customFont);
        JButton startButton = CreateJComponents.createJButton(buttonText, customFont, isTitle);
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
            alienArray.clear();
            bulletArray.clear();

            ship.x = shipX;
            alienVelocityX = 8;
            
            score = 0;
            lives = 3;

            timerState = 0;
            tickRate = 24;
            shipSpriteState = 0;

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
        //render Entities
        g.drawImage(ship.imgArray.get(shipSpriteState), ship.x, ship.y, ship.width, ship.height, null);
        
        EntityRenderer.renderAliens(g, alienArray, recentlyExploded, explosionDuration, alienWidth, alienHeight, alienSpriteState);
        EntityRenderer.renderUFO(g, ufoEntity, ufoHit, customFont, explosionDuration, ufoVelocityX, ufoCount, alienWidth, alienHeight, ufoImg);
        
        EntityRenderer.renderZigzagProjectile(g, zigzag, zigzagConvolution, tileSize);
        EntityRenderer.renderTProjectile(g, tArray, tShapesX, tShapesY, animationFrame, tileSize);
        
        EntityRenderer.renderBullets(g, bulletArray, bulletWidth, bulletHeight);

        //render HUD
        HUDRenderer.renderHUD(g, customFont, gameOver, score, highscore, lives, shipImgA, shipWidth, shipHeight);
    }

    public void move() {
        //aliens
        if (alienPosition >= tickRate && !shipHit) {
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
                            alienArray.clear();
                            shipHit = true;
                            shipSpriteState = 1;
                            lives = 0;
                        } else {
                            alien.y += tileSize;
                            if (alien.y == tileSize*6 && !allowUFOs) {
                                allowUFOs = true;
                            }
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
        
        //ufo ** maybe move some of this logic elsewhere if its not to do with movement
        if (allowUFOs) {
            if (ufoEntity == null) {
                if (ufoCount == 160) {
                    double randomNumber = Math.random();
                    int points = ufoPoints();
                    if (randomNumber > 0.75 || randomNumber < 0.25) {
                        createUFO(randomNumber, points);
                    }
                    ufoCount = 0;
                }
            } else if (ufoEntity != null) {
                ufoEntity.x += ufoVelocityX;
                if (ufoVelocityX > 0 && ufoEntity.x == boardWidth) {
                    ufoEntity = null;
                } else if (ufoVelocityX < 0 && ufoEntity.x == -tileSize*2) {
                    ufoEntity = null;
                }
            }
        }

        //zigzag
        if (zigzag != null) {
            if (zigzag.y >= boardHeight) {
                zigzag = null;
            } else {
                zigzag.y += 8;

                if (detectCollision(zigzag, ship)) {
                    lives -= 1;
                    shipHit = true;
                    zigzag = null;
                } 
            }
        }

        //t projectile
        if (!tArray.isEmpty()) {
            for (int i = 0; i < tArray.size(); i++) {
                Entity tProjectile = tArray.get(i);

                if (tProjectile.y == boardHeight) {
                    tArray.remove(i);
                } else {
                    tProjectile.y += 8;

                    if (detectCollision(tProjectile, ship)) {
                        lives -= 1;
                        shipHit = true;
                        tArray.remove(i);
                    } 
                }
            }
        }
        
        //bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Entity bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.size(); j++) {
                Entity alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    reloadTime = 30;
                    recentlyExploded = j;
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += alien.points;
                }
            }

            //bullet collision with ufo
            if (ufoEntity != null && detectCollision(bullet, ufoEntity)) {
                bullet.used = true;
                reloadTime = 30;
                score += ufoEntity.points;
                ufoHit = true;
                ufoVelocityX = 0;
            }
        }

        //clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 75)) {
            bulletArray.remove(0); // stretch change bulletArray to a linked list
        }

        //next level
        if (alienCount == 0) {
            alienArray.clear();
            bulletArray.clear();
            tArray.clear();
            zigzag = null;

            level += 1;
            score += level * alienCols * 100;

            shipSpriteState = 0;

            timerState = 0;
            tickRate = 24 - level;

            if (alienVelocityX < 0) {
                alienVelocityX *= -1;
            }
            alienVelocityX += 4;

            tSpawnInterval -= 5;
            zigzagSpawnInterval -=5;

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

    public void createUFO(double randomNumber, int points) {
        if (randomNumber > 0.75) {
            if (ufoVelocityX > 0) {
                ufoVelocityX *= -1;
            }
            ufoX = boardWidth;
            ufoEntity = new Entity(ufoX, ufoY, alienWidth, alienHeight, ufoImgs, 0, points);

        } else if (randomNumber < 0.25) {
            if (ufoVelocityX < 0) {
                ufoVelocityX *= -1;
            }
            ufoX = -tileSize*2;
            ufoEntity = new Entity(ufoX, ufoY, alienWidth, alienHeight, ufoImgs, 0, points);
        }
    }

    public int ufoPoints() {
        Random r = new Random();
        int randomInteger = r.nextInt(450) + 1;
        int points = 50 + (randomInteger/50)*50;
        return points;
    }

    public void moveFirstToEnd(ArrayList<Integer> list) {
        Integer firstElement = list.remove(0);
        list.add(firstElement);
    }

    public boolean detectCollision(Entity a, Entity b) {
        int innerBx = b.x + (int)(b.width*b.paddingRatio/2);
        int innerBWidth = (int)(b.width*(1 - b.paddingRatio));
    
        return a.x < innerBx + innerBWidth &&
               a.x + a.width > innerBx &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y;
    }

    public void createZigzag() {
        int y = alienArray.get(alienIndex).y + tileSize;
        int x = alienArray.get(alienIndex).x + tileSize*13/16;
        zigzag = new Entity(x, y, tileSize*3/8, tileSize*7/8, null, 0.0, 0);
    }

    public void createTProjectile() {
        int y = alienArray.get(alienIndex).y + tileSize;
        int x = alienArray.get(alienIndex).x + tileSize*13/16;
        Entity tProjectile = new Entity(x, y, tileSize*3/8, tileSize*7/8, null, 0.0, 0);
        tArray.add(tProjectile);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int aliveSquidCount = 0;
        if (!gameOver) {
            for (int i = 0; i < 11; i++) {
                Entity alien = alienArray.get(i);
                if (alien.alive) {
                    aliveSquidCount++;
                }
            }
        }
        if (aliveSquidCount > 0) {
            Random r = new Random();

            zigzagAnimationTimer++;
            if (zigzagAnimationTimer == zigzagAnimationInterval) {
                moveFirstToEnd(zigzagConvolution);
                zigzagAnimationTimer = 0;
            }
            if (zigzag == null) {
                zigzagSpawnTimer++;
                if (zigzagSpawnTimer == zigzagSpawnInterval) {
                    alienIndex = r.nextInt(11);
                    if (aliveSquidCount > 3) {
                        while (rechargingAliens.contains(alienIndex) || !alienArray.get(alienIndex).alive) {
                            alienIndex = r.nextInt(11);
                        }
                        if (rechargingAliens.size() == 3) {
                            rechargingAliens.remove(0);
                        }
                        createZigzag();
                    } else {
                        rechargingAliens.clear();
                        if (r.nextDouble() < 0.4) {
                            while (!alienArray.get(alienIndex).alive) {
                                alienIndex = r.nextInt(11);
                            }
                            createZigzag();
                        }
                    }
                    zigzagSpawnTimer = 0;
                }
            }
    
            tAnimationTimer++;
            if (tAnimationTimer == tAnimationInterval) {
                if (animationFrame == 3 && animationDirection == 1) {
                    animationDirection = -1;
                } else if (animationFrame == 0 && animationDirection == -1) {
                    animationDirection = 1;
                }
                animationFrame += animationDirection;
                tAnimationTimer = 0;
            }
            if (tArray.size() < 3) {
                tSpawnTimer++;
                if (tSpawnTimer == tSpawnInterval) {
                    alienIndex = r.nextInt(11);

                    if (aliveSquidCount > 3) {
                        while (rechargingAliens.contains(alienIndex) || !alienArray.get(alienIndex).alive) {

                            alienIndex = r.nextInt(11);
                        }
        
                        createTProjectile();
        
                        if (rechargingAliens.size() == 3) {
                            rechargingAliens.remove(0);
                        } 
                        
                        rechargingAliens.add(alienIndex);
                        
                    } else {
                        rechargingAliens.clear();
                        if (r.nextDouble() < 0.4) {
                            while (!alienArray.get(alienIndex).alive) {

                                alienIndex = r.nextInt(11);
                            }
                            createTProjectile();
                        }
                    }
                    tSpawnTimer = 0;
                }
            }
        }
        
        if (reloadTime < 30) {
            reloadTime++;
        }
        
        if (recentlyExploded >= 0) {
            explosionDuration++;
            if (explosionDuration == 2) {
                recentlyExploded = -1;
                explosionDuration = 0;
            }
        }

        if (ufoEntity == null && allowUFOs) {
            ufoCount++;
        }

        if (ufoHit) {
            explosionDuration++;
            if (explosionDuration >= 24) {
                ufoVelocityX = 4;
                ufoHit = false;
                ufoEntity = null;
                explosionDuration = 0;
                ufoCount = 0;
            }
        }

        if (shipHit) {
            shipTimer++;
            
            if (shipTimer % 5 == 0) {
                shipSpriteState = (shipSpriteState == 1) ? 2 : 1;
            }

            if (shipTimer == 50) {
                if (lives == 0) {
                    gameOver = true;
                    shipHit = false;
                } else {
                    shipHit = false;
                    shipSpriteState = 0;
                }
                shipTimer = 0;
            }
        } else {
            alienPosition++;
            timerState++;
            if (timerState == tickRate) {
                alienSpriteState = (alienSpriteState == 0) ? 1 : 0;
                timerState = 0;
            }
        }

        // Update ship's position based on key press flags
        if (movingLeft && ship.x > shipVelocityX && !shipHit) {
            ship.x -= shipVelocityX;
        } else if (movingRight && ship.x < boardWidth - shipWidth - shipVelocityX && !shipHit) {
            ship.x += shipVelocityX;
        }

        if (highscore < score) {
            highscore = score;
        }

        move();
        repaint();
        if (gameOver) {
            if (score == highscore) {
                highScoreManager.writeHighScore(score);
            }

            if (playAgainOverlay == null) {
                initialisePlayAgainOverlay();
                showModalOverlay(playAgainOverlay); 
            }

            gameLoop.stop();
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

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !shipHit) {
            Entity bullet = new Entity(ship.x + shipWidth*15/32, ship.y, bulletWidth, boardHeight, null, 0.0, 0);
            if (reloadTime == 30) {
                bulletArray.add(bullet);
                reloadTime = 0;
            }
        }
    }
}
