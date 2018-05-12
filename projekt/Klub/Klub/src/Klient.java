import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

public class Klient extends Thread{
	
	BufferedImage im;
	double x = -5;
	double y = -5;	
	int celX;
	int celY;
	int ileWypil = 0;
	private boolean wyproszony = false;
	Bar bar;
	
	Semaphore[] kolejka;
	Semaphore pojemnoscKlubu;
	Semaphore przejscie;

	public Klient(Bar bar, Semaphore[] kolejka, Semaphore przejscie, Semaphore pojemnoscKlubu)
	{
		Random r = new Random();
		this.x = r.nextInt(30) - 100;
		this.y = r.nextInt(500);		
		this.pojemnoscKlubu = pojemnoscKlubu;
		this.kolejka = kolejka;
		this.przejscie = przejscie;
		this.bar = bar;
	}
	
	public void setWyproszony(boolean wyp)
	{
		this.wyproszony = wyp;
	}
	
	public boolean getWyproszony()
	{
		return this.wyproszony;
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	//W¹tek klienta
	@Override
	public void run() 
	{		
		wejdzDoKlubu();
		while(!wyproszony) // Dopoki nie zostanie wyproszony
			bawSie();
		wyjdzZKlubu();
	}
	
	public void wyjdzZKlubu()
	{
		try {
			przejscie.acquire(); // Opusc semafor przejscia
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		idzDo(260, 250);
		idzDo(260, 230);
		idzDo(200, 230);
		//Wyjdz z klubu, podnies semafory
		przejscie.release();
		pojemnoscKlubu.release();
		//Wyjdz poza plansze
		idzDo(-40, -40);		
	}
	//gdy klient jest w klubie
	public void bawSie()
	{
		Random r = new Random();
		//podejdz do losowego x,y
		idzDo(r.nextInt(400) + 250, 20 + r.nextInt(400));

		if(bar.czyJestAlkohol()) // jezeli w barze jest alkohol
		{
			//jezeli jest
			idzDo(680, 30); //- podejdz do baru 
			//jezeli udalo sie pobrac alkohol z baru ileWypil++
			if(bar.pobierzAlkohol() != null) 
				ileWypil++; 
			//podejdz do losowego x,y
			idzDo(r.nextInt(400) + 250, 20 + r.nextInt(400));	
		}				
	}
	
	public void wejdzDoKlubu()
	{
		//Zajmij ostatnie miejsce w kolejce - jezeli dlugosc kolejki = 5 to zajmij semafor kolejka[4]
		try {
			kolejka[kolejka.length - 1].acquire();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for(int i = kolejka.length - 1; i >= 0 ; i--){
			//podejdz do danego miejsca w kolejce
			idzDo(120 - i * 30, 220);
			//jezeli klient nie jest pierwszy w kolejce
			if(i != 0){
				try{
					//zajmij kolejne miejsce w kolejce
					kolejka[i - 1].acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//zwolnij swoje miejsce w kolejce
				kolejka[i].release();
			}
		}
		
		//gdy klient pierwszy w kolejce
		try{
			pojemnoscKlubu.acquire(); // Zajmij miejsce w klubie
			przejscie.acquire(); // Zajmij przejœcie
			kolejka[0].release(); //Zwolnij pierwsze miejsce w kolejce
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//Wejdz do klubu, zwolnij przejscie
		idzDo(260, 230);
		przejscie.release();		
	}
	
	
	public void idzDo(int celX, int celY)
	{
		//Oblicza ró¿nice miedzy obecnym x, a docelowym
		double roznicaX = celX - this.x;
		double roznicaY = celY - this.y;	
		
		//Klient siê przesunie maksymalnie o 3 - oblicza ile musi minac iteracji zanim uda mu sie dojsc
		double iloscKrokow = Math.abs(roznicaX) > Math.abs(roznicaY) ? roznicaX / 3 : roznicaY / 3;		
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
	
	//Opóznienie zgodne z glowna petla programu
	public void opoznienie()
	{
		try {
			sleep(Main.odswiezanie);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void rysuj(Graphics g)
	{
		if(ileWypil == 0){
			try {	
				BufferedImage im = ImageIO.read(new File("Klub/Resources/trzezwy.png"));
				g.drawImage(im, (int) x, (int) y, 40, 40, null);
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
		if(ileWypil == 1){
			try {	
				BufferedImage im = ImageIO.read(new File("Klub/Resources/1.png"));
				g.drawImage(im, (int) x, (int) y, 40, 40, null);
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
		if(ileWypil == 2){
			try {	
				BufferedImage im = ImageIO.read(new File("Klub/Resources/2.png"));
				g.drawImage(im, (int) x, (int) y, 40, 40, null);
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
		if(ileWypil >= 3){
			try {	
				BufferedImage im = ImageIO.read(new File("Klub/Resources/3.png"));
				g.drawImage(im, (int) x, (int) y, 40, 40, null);
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
		if(wyproszony){
			try {	
				BufferedImage im = ImageIO.read(new File("Klub/Resources/wyproszony.png"));
				g.drawImage(im, (int) x, (int) y, 40, 40, null);
			} catch (IOException e) {
			e.printStackTrace();
			}
		}
	}
}
