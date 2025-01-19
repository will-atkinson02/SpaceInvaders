// import java.awt.event.*;

// public class ButtonPress implements KeyListener {
//     @Override
//     public void keyTyped(KeyEvent e) {}

//     @Override
//     public void keyPressed(KeyEvent e) {}

//     @Override
//     public void keyReleased(KeyEvent e) {
//         if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x > 0) {
//             ship.x -= shipVelocityX;
//         } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth < boardWidth) {
//             ship.x += shipVelocityX;
//         } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
//             Entity bullet = new Entity(ship.x + shipWidth*15/32, ship.y, bulletWidth, boardHeight, null);
//             bulletArray.add(bullet);
//         }
//     }
// }
