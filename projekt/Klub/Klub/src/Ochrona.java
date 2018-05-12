import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Ochrona extends Thread{
	Random generator = new Random();
	private double x;
	private double y;
	private int i;
	private ListaKlientow lista;
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	//W¹tek ochroniarza
	@Override
	public void run() 
	{	
		while(true)
		{			
			Klient doWyrzucenia = null;	
			//Jak któryœ klient jest pijany
			if((doWyrzucenia = lista.pobierzPijanegoKlienta()) != null)
			{				
				//Idz w jego stronê, dopóki ró¿nica w odleg³oœciach > 10
				while(Math.abs(doWyrzucenia.getX() - x) > 10
						|| Math.abs(doWyrzucenia.getY() - y) > 10)
				{			
					idzWKierunku(doWyrzucenia.getX(), doWyrzucenia.getY());
					opoznienie();
				}
				//Jak ju¿ podszed³ - wyproœ klienta
				doWyrzucenia.setWyproszony(true);	
				idzDo(210, 330+i*40); // Wroc do kañciapy
			}
		}
	}
	
	//Idzie w stronê celX, celY --- jeden krok
	public void idzWKierunku(double celX, double celY)
	{
		double roznicaX = celX - this.x;
		double roznicaY = celY - this.y;	
		
		//Klient siê przesunie maksymalnie o 3
		double iloscKrokow = Math.abs(roznicaX) > Math.abs(roznicaY) ? roznicaX / 4 : roznicaY / 4;		
		iloscKrokow = Math.abs(iloscKrokow);
		double krokX = roznicaX / iloscKrokow;
		double krokY = roznicaY / iloscKrokow;
				
		y += krokY;
		x += krokX;		
	}
	
	//Idzie w stronê celX, celY ----- dopóki nie dojdzie	
	public void idzDo(int celX, int celY)
	{
		//Oblicza ró¿nice miedzy obecnym x, a docelowym
		double roznicaX = celX - this.x;
		double roznicaY = celY - this.y;	
		
		//Klient siê przesunie maksymalnie o 3 - oblicza ile musi minac iteracji zanim uda mu sie dojsc
		double iloscKrokow = Math.abs(roznicaX) > Math.abs(roznicaY) ? roznicaX / 4 : roznicaY / 4;		
		iloscKrokow = Math.abs(iloscKrokow);
		
		//Oblicza dlugosc kroku
		double krokX = roznicaX / iloscKrokow;
		double krokY = roznicaY / iloscKrokow;
				
		while(iloscKrokow > 0)
		{
			y += krokY;
			x += krokX;
			iloscKrokow--;
			opoznienie();
		}
		
		/* jezeli cel oddalony jest o x=15, y=10 to liczba krokow to: 15/3 = 5
		 * dlugosc kroku: 3-x, 2-y
		 * co iteracje zwiekszane sa wspolrzedne o te wartosci
		 */
	}
	
	public void opoznienie()
	{
		try {
			sleep(Main.odswiezanie);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public Ochrona(ListaKlientow l, int i)
	{
		this.lista = l;
		x = 210;
		y = 330+i*40;
		this.i = i;
	}
	
	public void rysuj(Graphics g)
	{
		try {
	    	
			BufferedImage im = ImageIO.read(new File("Klub/Resources/ochroniarz.png"));
				g.drawImage(im, (int) x, (int) y, 40, 40, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}