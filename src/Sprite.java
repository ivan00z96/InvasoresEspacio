/**
 * Sprite
 *
 * Modela la definición de todos los objetos de tipo
 * <code>Sprite</code>
 *
 * @author XXXXXXX
 * @version 99 
 * @date 99/XXX/99
 */


import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;

/**
 *
 * @author http://zetcode.com/
 */
public class Sprite {

        private boolean visible;
        protected Image imaImagen;
        protected int iX;
        protected int iY;
        protected boolean dying;
        protected int dx;
        public int iAncho; //ancho del objeto
        public int iAlto; //largo del objeto

        public Sprite() {
            visible = true;
        }

        public void die() {
            visible = false;
        }

        public boolean isVisible() {
            return visible;
        }

        protected void setVisible(boolean visible) {
            this.visible = visible;
        }

        public void setImage(Image imaImagen) {
            this.imaImagen = imaImagen;
        }

        public Image getImage() {
            return imaImagen;
        }

        public void setX(int iX) {
            this.iX = iX;
        }

        public void setY(int y) {
            this.iY = y;
        }
        public int getY() {
            return iY;
        }

        public int getX() {
            return iX;
        }

        public void setDying(boolean dying) {
            this.dying = dying;
        }

        public boolean isDying() {
            return this.dying;
        }
        
        /**
     * getAncho
     * 
     * Metodo de acceso que regresa el ancho del icono 
     * 
     * @return un <code>entero</code> que es el ancho de la imagen.
     * 
     */
    public int getAncho() {
        ImageIcon imiIcon = new ImageIcon();
        imiIcon.setImage(imaImagen);
        return imiIcon.getIconWidth();
        
    }

    /**
     * getAlto
     * 
     * Metodo que  da el alto del icono 
     * 
     * @return un <code>entero</code> que es el alto de la imagen.
     * 
     */
    public int getAlto() {
        ImageIcon imiIcon = new ImageIcon();
        imiIcon.setImage(imaImagen);
        return imiIcon.getIconHeight();
    }
    
        
     /**
     * intersecta
     * 
     * Método que sirve para saber si un objeto del mismo tipo colisiono
     * con el objeto que invoca el método
     * 
     * @param objSprite
     * @return True si hubo colision, false en otro caso
     */
    public boolean intersecta(Object objSprite) {
        //valido si el objeto es de tipo personaje
        if(objSprite instanceof Sprite) {
            
            //Creo los rectangulos de ambos objetos
            Rectangle rctEste = new Rectangle(this.getX(), this.getY(), 
                    this.getAncho(), this.getAlto());
            Sprite sprMaloTemp = (Sprite) objSprite;
            Rectangle rctOtro = new Rectangle(sprMaloTemp.getX(), 
                    sprMaloTemp.getY(), sprMaloTemp.getAncho(), 
                    sprMaloTemp.getAlto());
            
            //Regreso el valor de la funcion intersects, true si colisionaron,
                    //false en otro caso
            return rctEste.intersects(rctOtro);
        }
        
        //Regreso false directamente si el objeto no era instancia de Personaje
        return false;
    }

}
