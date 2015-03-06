/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan & Fred
 */

import java.awt.Image;
import java.util.ArrayList;

/**
	La clase Animacion maneja una serie de imÃ¡genes (cuadros)
	y la cantidad de tiempo que se muestra cada cuadro.
*/

public class Animacion{
	
	private ArrayList cuadros;
	private int indiceCuadroActual;
	private long tiempoDeAnimacion;
	private long duracionTotal;
	
	/**
		Crea una nueva Animacion vacÃ­a
	*/
	public Animacion(){
		cuadros = new ArrayList();
		duracionTotal = 0;
		iniciar();
	}
	
	/**
		AÃ±ade una cuadro a la animaciÃ³n con la duraciÃ³n
		indicada (tiempo que se muestra la imagen).
	*/	
	public synchronized void sumaCuadro(Image imagen, long duracion){
		duracionTotal += duracion;
		cuadros.add(new cuadroDeAnimacion(imagen, duracionTotal));
	}
	
	// Inicializa la animaciÃ³n desde el principio. 
	public synchronized void iniciar(){
		tiempoDeAnimacion = 0;
		indiceCuadroActual = 0;
	}
	
	/**
		Actualiza la imagen (cuadro) actual de la animaciÃ³n,
		si es necesario.
	*/
	public synchronized void actualiza(long tiempoTranscurrido){
		if (cuadros.size() > 1){
			tiempoDeAnimacion += tiempoTranscurrido;
			
			if (tiempoDeAnimacion >= duracionTotal){
				tiempoDeAnimacion = tiempoDeAnimacion % duracionTotal;
				indiceCuadroActual = 0; 
			}
			
			while (tiempoDeAnimacion > getCuadro(indiceCuadroActual).tiempoFinal){
				indiceCuadroActual++;
			}
		}
	}
	
	/**
		Captura la imagen actual de la animaciÃ³n. Regeresa null
		si la animaciÃ³n no tiene imÃ¡genes.
	*/
	public synchronized Image getImagen(){
		if (cuadros.size() == 0){
			return null;
		}
		else {
			return getCuadro(indiceCuadroActual).imagen;
		}
	}
	
	private cuadroDeAnimacion getCuadro(int i){
		return (cuadroDeAnimacion)cuadros.get(i);
	}
	
	public class cuadroDeAnimacion{
		
		Image imagen;
		long tiempoFinal;
		
		public cuadroDeAnimacion(){
			this.imagen = null;
			this.tiempoFinal = 0;
		}
		
		public cuadroDeAnimacion(Image imagen, long tiempoFinal){
			this.imagen = imagen;
			this.tiempoFinal = tiempoFinal;
		}
		
		public Image getImagen(){
			return imagen;
		}
		
		public long getTiempoFinal(){
			return tiempoFinal;
		}
		
		public void setImagen (Image imagen){
			this.imagen = imagen;
		}
		
		public void setTiempoFinal(long tiempoFinal){
			this.tiempoFinal = tiempoFinal;
		}
	}
		
}