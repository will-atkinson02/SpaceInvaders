//TODO
// fix level up problems
// put projectiles into own classes
// add sound effects and music
// BONUS add green shield things, alien bullet collisions with floor
// stretch change bulletArray to a linked list

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import Entities.*;
import GameState.GameState;
import Rendering.*;
import Utilities.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    //board
    int tileSize = 32;
    int rows = 22;
    int cols = 30;
    int boardHeight = tileSize*rows;
    int boardWidth = tileSize*cols;
    GameState gameState = new GameState();

    //images
    ImageLoader images = new ImageLoader();

    //Entities
    Ship ship;
    AlienArray alienArray = new AlienArray();
    UFO ufo;
    
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize*4;
    int alienY = tileSize*4; 

    ArrayList<Entity> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -16;

    //states
    int recentlyExploded = -1;
    int explosionDuration = 0;

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
        showModalOverlay(titleOverlay);
        
        ship = new Ship(tileSize, cols, boardHeight, images.shipImgs, 0.0, 0);
        ufo = new UFO(tileSize, images.ufoImgs);
        bulletArray = new ArrayList<Entity>();
        tArray = new ArrayList<Entity>();
        rechargingAliens = new ArrayList<Integer>();

        alienArray.createAliens(alienX, alienY, alienWidth, alienHeight, images.squidImgs, images.crabImgs, images.octopusImgs);
        initialiseZigzagConvolution();
        initaliseTSHapes();

        gameLoop = new Timer(1000/60, this);
    }

    public void initialiseZigzagConvolution() {
        zigzagConvolution = new ArrayList<Integer>();
        zigzagConvolution.add(0);
        zigzagConvolution.add(4);
        zigzagConvolution.add(8);
        zigzagConvolution.add(4);
        zigzagConvolution.add(0);
        zigzagConvolution.add(4);
        zigzagConvolution.add(8);
        zigzagConvolution.add(4);
    }

    public void initaliseTSHapes() {
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
        String text = "Score: " + Integer.toString(gameState.score);
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
            clearEntities();

            ship.x = (tileSize*cols)/2 - tileSize;
            alienArray.alienVelocityX = 8;
            
            gameState.score = 0;
            ship.lives = 3;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienStepRate = 24;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienSpriteState = 0;

            ship.shipSpriteState = 0;

            alienArray.createAliens(alienX, alienY, alienWidth, alienHeight, images.squidImgs, images.crabImgs, images.octopusImgs);

            gameLoop.start();
            gameState.gameOver = false;
        }
    }

    public void clearEntities() {
        alienArray.alienArray.clear();
        bulletArray.clear();
        tArray.clear();
        zigzag = null;
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
        g.drawImage(ship.imgArray.get(ship.shipSpriteState), ship.x, ship.y, ship.width, ship.height, null);
        
        EntityRenderer.renderAliens(g, alienArray.alienArray, recentlyExploded, explosionDuration, alienWidth, alienHeight, alienArray.alienSpriteState);
        
        if (ufo.isActive) {
            EntityRenderer.renderUFO(g, ufo, customFont, images.ufoImg);
        }

        EntityRenderer.renderZigzagProjectile(g, zigzag, zigzagConvolution, tileSize);
        EntityRenderer.renderTProjectile(g, tArray, tShapesX, tShapesY, animationFrame, tileSize);
        
        EntityRenderer.renderBullets(g, bulletArray, bulletWidth, bulletHeight);

        //render HUD
        HUDRenderer.renderHUD(g, customFont, gameState, highscore, ship.lives, images.shipImgA, ship.width, ship.height);
    }

    public void move() {
        alienArray.alienMovement(alienWidth, boardWidth, tileSize, ship, ufo);

        zigzagMovement();
        tProjectileMovement();
        bulletMovement();

        //clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 75)) {
            bulletArray.remove(0); 
        }
    }

    public void zigzagMovement() {
        if (zigzag != null) {
            if (zigzag.y >= boardHeight) {
                zigzag = null;
            } else {
                zigzag.y += 8;

                if (Entity.detectCollision(zigzag, ship)) {
                    ship.hit();
                    zigzag = null;
                } 
            }
        }
    }

    public void tProjectileMovement() {
        if (!tArray.isEmpty()) {
            for (int i = 0; i < tArray.size(); i++) {
                Entity tProjectile = tArray.get(i);

                if (tProjectile.y == boardHeight) {
                    tArray.remove(i);
                } else {
                    tProjectile.y += 8;

                    if (Entity.detectCollision(tProjectile, ship)) {
                        ship.hit();
                        tArray.remove(i);
                    } 
                }
            }
        }
    }

    public void bulletMovement() {
        for (int i = 0; i < bulletArray.size(); i++) {
            Entity bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.alienArray.size(); j++) {
                Entity alien = alienArray.alienArray.get(j);
                if (!bullet.used && alien.alive && Entity.detectCollision(bullet, alien)) {
                    ship.reloadTime = 60;
                    recentlyExploded = j;
                    bullet.used = true;
                    alien.alive = false;
                    alienArray.alienCount--;
                    gameState.score += alien.points;
                }
            }

            //bullet collision with ufo
            if (ufo != null && Entity.detectCollision(bullet, ufo)) {
                bullet.used = true;
                ship.reloadTime = 60;
                gameState.score += UFO.points();
                ufo.hit = true;
                ufo.velocityX = 0;
            }
        }
    }

    public void moveFirstToEnd(ArrayList<Integer> list) {
        Integer firstElement = list.remove(0);
        list.add(firstElement);
    }

    public void createZigzag() {
        int y = alienArray.alienArray.get(alienIndex).y + tileSize;
        int x = alienArray.alienArray.get(alienIndex).x + tileSize*13/16;
        zigzag = new Entity(x, y, tileSize*3/8, tileSize*7/8, null, 0.0, 0);
    }

    public void createTProjectile() {
        int y = alienArray.alienArray.get(alienIndex).y + tileSize;
        int x = alienArray.alienArray.get(alienIndex).x + tileSize*13/16;
        Entity tProjectile = new Entity(x, y, tileSize*3/8, tileSize*7/8, null, 0.0, 0);
        tArray.add(tProjectile);
    }

    public void nextLevel() {
        if (alienArray.alienCount == 0) {
            clearEntities();

            gameState.level += 1;
            gameState.score += gameState.level * alienArray.alienCols * 100;

            ship.shipSpriteState = 0;

            alienArray.alienSpriteTimer = 0;
            alienArray.alienStepRate = 24 - gameState.level;

            if (alienArray.alienVelocityX < 0) {
                alienArray.alienVelocityX *= -1;
            }
            alienArray.alienVelocityX += 4;

            tSpawnInterval -= 5;
            zigzagSpawnInterval -=5;

            alienArray.createAliens(alienX, alienY, alienWidth, alienHeight, images.squidImgs, images.crabImgs, images.octopusImgs);
        }
    }

    public void handleZigzagSpawn(Random r) {
        zigzagAnimationTimer++;
        if (zigzagAnimationTimer == zigzagAnimationInterval) {
            moveFirstToEnd(zigzagConvolution);
            zigzagAnimationTimer = 0;
        }
        
        if (zigzag == null) {
            zigzagSpawnTimer++;
            if (zigzagSpawnTimer == zigzagSpawnInterval) {
                alienIndex = r.nextInt(11);
                if (alienArray.aliveSquidCount > 3) {
                    while (rechargingAliens.contains(alienIndex) || !alienArray.alienArray.get(alienIndex).alive) {
                        alienIndex = r.nextInt(11);
                    }
                    if (rechargingAliens.size() == 3) {
                        rechargingAliens.remove(0);
                    }
                    createZigzag();
                } else {
                    rechargingAliens.clear();
                    if (r.nextDouble() < 0.4) {
                        while (!alienArray.alienArray.get(alienIndex).alive) {

                            alienIndex = r.nextInt(11);
                        }
                        createZigzag();
                    }
                }
                zigzagSpawnTimer = 0;
            }
        }
    }

    public void handleTProjectileSpawn(Random r) {
        tAnimationTimer++;
        if (tAnimationTimer == tAnimationInterval) {
            handleTProjectileAnimation();
            tAnimationTimer = 0;
        }
        if (tArray.size() < 3) {
            tSpawnTimer++;
            if (tSpawnTimer == tSpawnInterval) {
                alienIndex = r.nextInt(11);
                if (alienArray.aliveSquidCount > 3) {
                    while (rechargingAliens.contains(alienIndex) || !alienArray.alienArray.get(alienIndex).alive) {

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
                        while (!alienArray.alienArray.get(alienIndex).alive) {
                            alienIndex = r.nextInt(11);
                        }
                        createTProjectile();
                    }
                }
                tSpawnTimer = 0;
            }
        }
    }

    public void handleTProjectileAnimation() {
        if (animationFrame == 3 && animationDirection == 1) {
            animationDirection = -1;
        } else if (animationFrame == 0 && animationDirection == -1) {
            animationDirection = 1;
        }
        animationFrame += animationDirection;
    }

    public void alienExplosionTimer() {
        if (recentlyExploded >= 0) {
            explosionDuration++;
            if (explosionDuration == 2) {
                recentlyExploded = -1;
                explosionDuration = 0;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int aliveSquidCount = 0;
        if (!gameState.gameOver) {
            aliveSquidCount = alienArray.countAliveSquids();
        }
        if (aliveSquidCount > 0) {
            Random r = new Random();
            handleZigzagSpawn(r);
            handleTProjectileSpawn(r);
        }
        alienExplosionTimer();

        if (ship.reloadTime < 60) {
            ship.reloadTime++;
        }
        ship.handleShipHit(gameState, alienArray);
        ship.allowShipControl(boardWidth);

        ufo.incrementUFOs();
        ufo.handle(gameState, boardWidth, tileSize, images.ufoImgs);

        move();
        nextLevel();
        repaint();

        if (gameState.gameOver) {
            if (gameState.score > highscore) {
                highScoreManager.writeHighScore(gameState.score);
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
            ship.movingLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width < boardWidth) {
            ship.movingRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
            ship.movingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + ship.width < boardWidth) {
            ship.movingRight = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !ship.shipHit) {
            Entity bullet = new Entity(ship.x + ship.width*15/32, ship.y, bulletWidth, boardHeight, null, 0.0, 0);
            if (ship.reloadTime == 60) {
                bulletArray.add(bullet);
                // SoundPlayer.playSound("sounds/bullet.wav");
                ship.reloadTime = 0;
            }
        }
    }
}
