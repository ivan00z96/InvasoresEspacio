/**
 * Juego de Space Invaders
 *
 *
 * @author Fred Roblero Maldonado A01037070
 * @author Iván Alejandro Leal Cervantes A00815154
 * @version 1.0
 * @date ?3&6&2015
 */
import javax.swing.JFrame;

/**
 *
 * @author http://zetcode.com/
 */
public class SpaceInvaders extends JFrame implements Commons {

    public SpaceInvaders()
    {
        add(new Board());
        setTitle("Space Invaders");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(BOARD_WIDTH, BOARD_HEIGTH);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        new SpaceInvaders();
    }
}