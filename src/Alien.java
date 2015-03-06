
import java.awt.Image;
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
public class Alien extends Sprite {

    private Bomb bomb;
    private final String shot = "alien1.jpg";

    public Alien(int x, int y) {
        this.iX = x;
        this.iY = y;

        bomb = new Bomb(x, y);
        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
        setImage(ii.getImage());

    }
    /**
     * Alien
     * 
     * Metodo constructor usado para crear el objeto animal
     * creando el icono a partir de una imagen
     * 
     * @param iX es la <code>posicion en x</code> del objeto.
     * @param iY es la <code>posicion en y</code> del objeto.
     * @param iAncho es el <code>ancho</code> del objeto.
     * @param iAlto es el <code>Largo</code> del objeto.
     * @param imaImagen es la <code>imagen</code> del objeto.
     * 
//     */
//    public Alien(int iX, int iY , int iAncho, int iAlto,Image imaImagen) {
//        this.iX = iX;
//        this.iY = iY;
//        this.iAncho = iAncho;
//        this.iAlto = iAlto;
//        this.imaImagen = imaImagen;
//        bomb = new Bomb(iX, iY);
//        ImageIcon ii = new ImageIcon(this.getClass().getResource(shot));
//        setImage(ii.getImage());
//    }

    public void act(int direction) {
        this.iX += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private final String bomb = "bomb.jpg";
        private boolean destroyed;

        public Bomb(int iX, int iY) {
            setDestroyed(true);
            this.iX = iX;
            this.iY = iY;
            ImageIcon ii = new ImageIcon(this.getClass().getResource(bomb));
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {
            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {
            return destroyed;
        }
    }
}
