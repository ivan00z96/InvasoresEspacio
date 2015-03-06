
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author http://zetcode.com/
 */
public class Player extends Sprite implements Commons{

    private final int START_Y = 445; 
    private final int START_X = 270;
    private Animacion aniPlayer;

    private final String player = "player1.jpg";
    private int width;

    public Player() {
        //Se cargan las imÃ¡genes(cuadros) para la animaciÃ³n de Hank
        Image imaNave1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("player1.jpg"));
        Image imaNave2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("player2.jpg"));
        Image imaNave3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("player3.jpg"));
        

        //Se crea la animaciÃ³n
        aniPlayer = new Animacion();
        aniPlayer.sumaCuadro(imaNave1, 100);
        aniPlayer.sumaCuadro(imaNave2, 100);
        aniPlayer.sumaCuadro(imaNave3, 100);

        ImageIcon ii = new ImageIcon(this.getClass().getResource(player));

        width = ii.getImage().getWidth(null); 

        setImage(imaNave3);
        setX(START_X);
        setY(START_Y);
    }

    public void act(long tiempo) {
//        //Determina el tiempo que ha transcurrido desde que el JFrame inicio su ejecuciÃ³n
//         long tiempoTranscurrido = System.currentTimeMillis() - tiempo;
//            
//         //Guarda el tiempo actual
//       	 tiempo += tiempoTranscurrido;
//       	 
//       	 //Actualiza la animaciÃ³n del elefante en base al tiempo transcurrido
       	aniPlayer.actualiza(tiempo);
        iX += dx;
        if (iX <= 2) {
            iX = 2;
        }
        if (iX >= BOARD_WIDTH - 2*width) {
            iX = BOARD_WIDTH - 2*width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT)
        {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT)
        {
            dx = 0;
        }
    }
}
