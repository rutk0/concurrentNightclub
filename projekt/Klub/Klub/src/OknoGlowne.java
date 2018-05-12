import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class OknoGlowne extends JPanel{
	private static final long serialVersionUID = 1L;
	
	Bar bar; 
	//Ile osób wejdzie do klubu - dlugosc 10
	Semaphore pojemnoscKlubu = new Semaphore(10);
	//Kolejka wejœcia do klubu - d³ugoœæ  5
	Semaphore[] kolejkaDoKlubu = new Semaphore[5];
	//Przejœcie - w¹skie gard³o
	Semaphore wejscie = new Semaphore(1);
	ListaKlientow lista = new ListaKlientow();
    ArrayList<Ochrona> ochrona = new ArrayList<Ochrona>();
	
	private Timer zegar; // Popycha symulacje do przodu

	public OknoGlowne(int szerokosc, int wysokosc)
	{
		bar = new Bar();
		setSize(szerokosc, wysokosc);
		//Tworze semafory z kolejki
		for(int i = 0; i < kolejkaDoKlubu.length ; i++)
			kolejkaDoKlubu[i] = new Semaphore(1);
		
		//Tworze 3 ochroniarzy
		for(int i = 0; i < 3; i++)
		{
			Ochrona o = new Ochrona(lista,i);
			ochrona.add(o);
			o.start(); // Rozpocznij w¹tek ochroniarza
		}
	}
		
	//Rozpoczêcie symulacji
	public void start()
	{					
		zegar = new Timer();
		//Wywo³uj funkcjê glownaPetla co odswiezanie milisekund
		zegar.schedule(new glownaPetla(), 0, Main.odswiezanie);
	}
	
    class glownaPetla extends TimerTask 
    {
        public void run() {       
        	Random ran = new Random();
        	
        	// < 2 <-- szansa na pojawienie sie alkoholu w danej klatce
        	if(ran.nextInt(100) < 2)
        		bar.DodajAlkohol();
        	
        	// < 5 <-- szansa na pojawienie sie klienta ORAZ
        	// Jeœli ostatnie miejsce w kolejce do klubu jest wolne
        	if(ran.nextInt(100) < 5 && kolejkaDoKlubu[kolejkaDoKlubu.length - 1].availablePermits() == 1)
        	{
        		Klient nowy = new Klient(bar, kolejkaDoKlubu, wejscie, pojemnoscKlubu);
        		lista.dodajKlienta(nowy);  
        		nowy.start(); //Rozpocznij w¹tek klienta
        	}
        	
        	//Usuwa klientów, którzy wyszli z klubu
        	lista.usunKlientow();
        	
        	repaint(); // Wywo³uje metodê paint()
        }
    }
    
    //Malowanie
  	public void paint(Graphics g)
  	{	
  		//super.paint(g); 
  		
  		g.setColor(new Color(50,201,30));
  		g.fillRect(0, 0, 800, 500);
  		g.setColor(new Color(170,170,145));
  		g.fillRect(0, 210, 200, 60);
  		try {
	    	
			BufferedImage im = ImageIO.read(new File("Klub/Resources/podloga.png"));
				g.drawImage(im, 200,10, 550, 440, null);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		//Rysuj ochroniarzy
  		for(Ochrona o : ochrona)
  			o.rysuj(g); 
 
  		rysujKlub(g);
  		//Rysuj alko
  		bar.rysuj(g);
  		//Rysuj klientów
  		lista.rysuj(g);
  	}
  	
  	public void rysujKlub(Graphics g)
  	{
  		try {
	    	for(int i =0; i<2; i++){
	    		BufferedImage im = ImageIO.read(new File("Klub/Resources/krzak.png"));
				g.drawImage(im, 100,20+80*i, 80, 80, null);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		try {
	    	
			BufferedImage im = ImageIO.read(new File("Klub/Resources/szyld.png"));
				g.drawImage(im, 125,70, 80, 100, null);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
  		try {
	    	for(int i =0; i<2; i++){
	    		BufferedImage im = ImageIO.read(new File("Klub/Resources/krzak.png"));
				g.drawImage(im, 100,260+80*i, 80, 80, null);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		//Rysowanie otoczenia 		
  		//Bar
  		g.setColor(new Color(90,120,130));
  		g.fillRect(700, 10, 50, 150);   		
  		
  		//budynek
  		//Góra
  		g.setColor(new Color(242,85,85));
  		g.fillRect(200, 10, 550, 5);
  		//Dol
  		g.fillRect(200, 450, 550, 5); 	
  		//Bok- prawy
  		g.fillRect(750, 10, 5, 445);
  		//Bok - lewy
  		g.fillRect(200, 10, 5, 200); 		
  		g.fillRect(200, 270, 5, 185);
  		
  		
  	}
}
