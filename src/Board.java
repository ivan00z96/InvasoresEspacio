
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author http://zetcode.com/
 */
public class Board extends JPanel implements Runnable, Commons { 

    private Dimension d;
    private ArrayList arlAliens;
    private Player plaPlayer;
    private Shot shShot;
    private boolean bPausa;
    private boolean bInstruccion;
    private boolean bAutores;
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen

    private int iAlienX = 150;
    private int iAlienY = 5;
    private int iDirection = -1;
    private int iDeaths = 0;

    private boolean bIngame = true;
    private final String strExpl = "explosion.png";
    private final String strAlienpix = "alien.png";
    private String strMessage = "Game Over";

    private Thread animator;

    public Board() 
    {

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(BOARD_WIDTH, BOARD_HEIGTH);
        setBackground(Color.black);

        gameInit();
        setDoubleBuffered(true);
    }

    public void addNotify() {
        super.addNotify();
        gameInit();
    }

    public void gameInit() {

        bPausa = false;
        bInstruccion = false;
        
        arlAliens = new ArrayList();

        ImageIcon ii = new ImageIcon(this.getClass().getResource(strAlienpix));

        for (int i=0; i < 4; i++) {
            for (int j=0; j < 6; j++) {
                Alien alien = new Alien(iAlienX + 18*j, iAlienY + 18*i);
                alien.setImage(ii.getImage());
                arlAliens.add(alien);
            }
        }

        plaPlayer = new Player();
        shShot = new Shot();

        if (animator == null || !bIngame) {
            animator = new Thread(this);
            animator.start();
        }
    }

    public void drawAliens(Graphics g) 
    {
        Iterator it = arlAliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();

            if (alien.isVisible()) {
                g.drawImage(alien.getImage(), alien.getX(), alien.getY(), this);
            }

            if (alien.isDying()) {
                alien.die();
            }
        }
    }

    public void drawPlayer(Graphics g) {

        if (plaPlayer.isVisible()) {
            g.drawImage(plaPlayer.getImage(), plaPlayer.getX(), plaPlayer.getY(), this);
        }

        if (plaPlayer.isDying()) {
            plaPlayer.die();
            bIngame = false;
        }
    }

    public void drawShot(Graphics g) {
        if (shShot.isVisible())
            g.drawImage(shShot.getImage(), shShot.getX(), shShot.getY(), this);
    }

    public void drawBombing(Graphics g) {

        Iterator i3 = arlAliens.iterator();

        while (i3.hasNext()) {
            Alien a = (Alien) i3.next();

            Alien.Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                g.drawImage(b.getImage(), b.getX(), b.getY(), this); 
            }
        }
    }

    public void paint(Graphics g)
    {
      super.paint(g);

      g.setColor(Color.black);
      g.fillRect(0, 0, d.width, d.height);
      g.setColor(Color.green);   

      if (bIngame) {
          if (bInstruccion) {
              URL urlImagenFondo = this.getClass().getResource("inst.jpg");
                Image imaImagenFondo
                        = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
                g.drawImage(imaImagenFondo, 0, 0,
                        getWidth(), getHeight(), this);
          }
          
          if (bPausa) {
              URL urlImagenFondo = this.getClass().getResource("pausa.jpg");
                Image imaImagenFondo
                        = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
                g.drawImage(imaImagenFondo, 0, 0,
                        getWidth(), getHeight(), this);
          }

        if (!bPausa) {
          g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);
        drawAliens(g);
        drawPlayer(g);
        drawShot(g);
        drawBombing(g);
      }
      }

      Toolkit.getDefaultToolkit().sync();
      g.dispose();
    }
    
     /**
     * guardarJuego
     *
     * Metodo que guarda todos los datos del juego
     *
     */
    public void guardarJuego() {

        //PrintWriter pwrOut = new PrintWriter(new FileWriter("Archivo.txt"));
        try (PrintWriter pwrOut = new PrintWriter(new FileWriter("Archivo.txt"))) {
            pwrOut.println(bPausa ? 1 : 0);
            pwrOut.println(bInstruccion ? 1 : 0);
            pwrOut.println(bAutores ? 1 : 0);
            pwrOut.println(plaPlayer.getX());
            pwrOut.println(plaPlayer.getY());
            pwrOut.println(iDeaths);
            Iterator it = arlAliens.iterator();
            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                pwrOut.println(alien.getX());
                pwrOut.println(alien.getY());
                pwrOut.println(alien.isVisible() ? 1 : 0);
//                int iAlienX = alien.getX();
//                int iAlienY = alien.getY();
                Alien.Bomb b = alien.getBomb();
                pwrOut.println(b.getX());
                pwrOut.println(b.getY());
                pwrOut.println(b.isDestroyed() ? 1 : 0);
            }
            pwrOut.println(shShot.getX());
            pwrOut.println(shShot.getY());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * cargarJuego
     *
     * Metodo que carga los datos de un juego anterior que se haya guardado
     *
     */
    public void cargarJuego() {
        int iaux;
        URL urlImagenMalo = this.getClass().getResource("chimpy.gif");
        URL urlImagenBueno = this.getClass().getResource("diddy.gif");
        BufferedReader buffer;
        try {
//            // Abrimos el archivo
//            FileInputStream fstream = new FileInputStream("Archivo.txt");
//            // Creamos el objeto de entrada
//            DataInputStream entrada = new DataInputStream(fstream);
//            // Creamos el Buffer de Lectura
//            BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
            buffer = new BufferedReader(new FileReader("Archivo.txt"));
            String strLinea;
            // Leer el archivo linea por linea
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            bPausa = (iaux == 1);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            bInstruccion = (iaux == 1);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            bPausa = (iaux == 1);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            plaPlayer.setX(iaux);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            plaPlayer.setY(iaux);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            iDeaths = iaux;
            arlAliens.clear();
            ImageIcon ii = new ImageIcon(this.getClass().getResource(strAlienpix));
            for (int iI = 0; iI < NUMBER_OF_ALIENS_TO_DESTROY; iI++) {
                Alien alien = new Alien(18, 18);
                alien.setImage(ii.getImage());
                arlAliens.add(alien);
            }
            Iterator it = arlAliens.iterator();
            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                strLinea = buffer.readLine();
                iaux = Integer.parseInt(strLinea);
                alien.setX(iaux);
                strLinea = buffer.readLine();
                iaux = Integer.parseInt(strLinea);
                alien.setY(iaux);
                strLinea = buffer.readLine();
                iaux = Integer.parseInt(strLinea);
                alien.setVisible(iaux == 1);
                Alien.Bomb b = alien.getBomb();
                strLinea = buffer.readLine();
                iaux = Integer.parseInt(strLinea);
                b.setX(iaux);
                strLinea = buffer.readLine();
                iaux = Integer.parseInt(strLinea);
                b.setY(iaux);
                strLinea = buffer.readLine();
                iaux = Integer.parseInt(strLinea);
                b.setDestroyed(iaux == 1);
            }
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            shShot.setX(iaux);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            shShot.setY(iaux);
            strLinea = buffer.readLine();
            iaux = Integer.parseInt(strLinea);
            shShot.setVisible(iaux == 1);

            // Cerramos el archivo
            buffer.close();
        } catch (Exception e) { //Catch de excepciones
            System.err.println("Ocurrio un error: " + e.getMessage());
        }
    }
    
    public void gameOver()
    {

        Graphics g = this.getGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGTH);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH/2 - 30, BOARD_WIDTH-100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(strMessage, (BOARD_WIDTH - metr.stringWidth(strMessage))/2, 
            BOARD_WIDTH/2);
    }

    public void animationCycle()  {

        if (iDeaths == NUMBER_OF_ALIENS_TO_DESTROY) {
            bIngame = false;
            strMessage = "Game won!";
        }

        // plaPlayer

        plaPlayer.act();

        // shShot
        if (shShot.isVisible()) {
            Iterator it = arlAliens.iterator();
            int shShotX = shShot.getX();
            int shShotY = shShot.getY();

            while (it.hasNext()) {
                Alien alien = (Alien) it.next();
                int iAlienX = alien.getX();
                int iAlienY = alien.getY();

                if (alien.isVisible() && shShot.isVisible()) {
//                    if (shShotX >= (iAlienX) && 
//                        shShotX <= (iAlienX + ALIEN_WIDTH) &&
//                        shShotY >= (iAlienY) &&
//                        shShotY <= (iAlienY+ALIEN_HEIGHT) ) 
                        if (shShot.intersecta(alien)){
                            ImageIcon ii = 
                                new ImageIcon(getClass().getResource(strExpl));
                            alien.setImage(ii.getImage());
                            alien.setDying(true);
                            iDeaths++;
                            shShot.die();
                        }
                }
            }

            int y = shShot.getY();
            y -= 4;
            if (y < 0)
                shShot.die();
            else shShot.setY(y);
        }

        // arlAliens

         Iterator it1 = arlAliens.iterator();

         while (it1.hasNext()) {
             Alien a1 = (Alien) it1.next();
             int x = a1.getX();

             if (x  >= BOARD_WIDTH - BORDER_RIGHT && iDirection != -1) {
                 iDirection = -1;
                 Iterator i1 = arlAliens.iterator();
                 while (i1.hasNext()) {
                     Alien a2 = (Alien) i1.next();
                     a2.setY(a2.getY() + GO_DOWN);
                 }
             }

            if (x <= BORDER_LEFT && iDirection != 1) {
                iDirection = 1;

                Iterator i2 = arlAliens.iterator();
                while (i2.hasNext()) {
                    Alien a = (Alien)i2.next();
                    a.setY(a.getY() + GO_DOWN);
                }
            }
        }


        Iterator it = arlAliens.iterator();

        while (it.hasNext()) {
            Alien alien = (Alien) it.next();
            if (alien.isVisible()) {

                int y = alien.getY();

                if (y > GROUND - ALIEN_HEIGHT) {
                    bIngame = false;
                    strMessage = "Invasion!";
                }

                alien.act(iDirection);
            }
        }

        // bombs

        Iterator i3 = arlAliens.iterator();
        Random generator = new Random();

        while (i3.hasNext()) {
            int shShot = generator.nextInt(15);
            Alien a = (Alien) i3.next();
            Alien.Bomb b = a.getBomb();
            if (shShot == CHANCE && a.isVisible() && b.isDestroyed()) {

                b.setDestroyed(false);
                b.setX(a.getX());
                b.setY(a.getY());   
            }

            int bombX = b.getX();
            int bombY = b.getY();
            int plaPlayerX = plaPlayer.getX();
            int plaPlayerY = plaPlayer.getY();

            if (plaPlayer.isVisible() && !b.isDestroyed()) {
//                if ( bombX >= (plaPlayerX) && 
//                    bombX <= (plaPlayerX+PLAYER_WIDTH) &&
//                    bombY >= (plaPlayerY) && 
//                    bombY <= (plaPlayerY+PLAYER_HEIGHT) ) 
                if (plaPlayer.intersecta(b)) {
                        ImageIcon ii = 
                            new ImageIcon(this.getClass().getResource(strExpl));
                        plaPlayer.setImage(ii.getImage());
                        plaPlayer.setDying(true);
                        b.setDestroyed(true);;
                    }
            }

            if (!b.isDestroyed()) {
                b.setY(b.getY() + 1);   
                if (b.getY() >= GROUND - BOMB_HEIGHT) {
                    b.setDestroyed(true);
                }
            }
        }
    }

    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (bIngame) {
            if (!bPausa && !bInstruccion && !bAutores) {
            animationCycle();
            }
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) 
                sleep = 2;
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
            beforeTime = System.currentTimeMillis();
        }
        gameOver();
    }

    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            plaPlayer.keyReleased(e);
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_P) { //Pausa
            
            bPausa = !bPausa;
        }
          if (keyCode == KeyEvent.VK_I) { //Instrucciones
            
            bInstruccion = !bInstruccion;
        }
          if (keyCode == KeyEvent.VK_R) {
              bAutores = !bAutores;
          }
          else if (keyCode == KeyEvent.VK_G) {
            guardarJuego();
        } else if (keyCode == KeyEvent.VK_C) {
            cargarJuego();
        }
        }

        public void keyPressed(KeyEvent e) {

          plaPlayer.keyPressed(e);
          

          int x = plaPlayer.getX();
          int y = plaPlayer.getY();
          

          if (bIngame)
          {
            if (e.isAltDown()) {
                if (!shShot.isVisible())
                    shShot = new Shot(x, y);
            }
          }
        }
    }
}
