import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

public class Bar {
	//maksymalna ilosc alkoholu na barze
	private int maksIloscAlkoholu = 5;
	//lista alkoholi
	ArrayList<Alkohol> alkohol = new ArrayList<Alkohol>();
	Semaphore s = new Semaphore(0); // wielkoœæ == iloœci alko na barze
	//semafor dostepu do baru
	Semaphore dostepDoListy = new Semaphore(1); //Dostêp do baru
	
	public void DodajAlkohol()
	{
		if(alkohol.size() < maksIloscAlkoholu)
		{
			try 
			{
				dostepDoListy.acquire();
				alkohol.add(new Alkohol(705, alkohol.size() * 20 + 30));
				dostepDoListy.release();
				s.release();	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//metoda sprawdza czy alkohol istnieje - jezeli wielkosc listy >0 zwraca true
	public boolean czyJestAlkohol()
	{
		return alkohol.size() > 0 ? true : false;
	}
	
	//pobieranie alkoholu z baru przez klienta
	public Alkohol pobierzAlkohol()
	{
		try {
			//sprobuj pobrac alkohol, jezeli nie uda sie wyjdz
			if(s.tryAcquire(1, 0, TimeUnit.MICROSECONDS))
			{
				dostepDoListy.acquire();
				//pobieranie alkoholu z listy
				Alkohol al = alkohol.remove(alkohol.size() - 1);
				dostepDoListy.release();
				return al;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void rysuj(Graphics g)
	{	
		try {
			dostepDoListy.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//rysowanie alkoholu
	    try {
			BufferedImage im = ImageIO.read(new File("Klub/Resources/beer.png"));
			for(Alkohol a : alkohol)
				g.drawImage(im, (int) a.getX(), (int) a.getY(), 40, 40, null);	
		} catch (IOException e) {
			e.printStackTrace();
		}	
		dostepDoListy.release();
	}
}
