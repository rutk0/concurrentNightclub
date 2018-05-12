import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class ListaKlientow {
	//listy klientow oraz pijanych klientow
    ArrayList<Klient> klienci = new ArrayList<Klient>();
    ArrayList<Klient> pijaniKlienci = new ArrayList<Klient>();
    //semafor dostepu do listy
    Semaphore dostepDoListy = new Semaphore(1);
    
    //Ochrona interweniuje w sprawie pijanego klienta
    public Klient pobierzPijanegoKlienta()
    {
    	try {
			dostepDoListy.acquire(); // Opusc semafor dostepu do listy
			
	    	for(int i = 0; i < klienci.size(); i++)
	    		//jezeli klient wypi³ co najmniej 3 
	    		if(klienci.get(i).ileWypil == 3) 
	    		{
	    			Klient k = klienci.remove(i); // Usuñ go ze zwyk³ej listy
	    			pijaniKlienci.add(k); // Dodaj do listy - pijani klienci
	    			dostepDoListy.release(); //Podnies semafor dostepu do listy
	    			return k; // zwroc pijanego klienta
	    		}
	    	dostepDoListy.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public void dodajKlienta(Klient k)
    {
		try {
			dostepDoListy.acquire(); // Opusc semafor dostepu do listy		
			klienci.add(k);			 //dodaj klienta do listy
			dostepDoListy.release(); // podnies semafor dostepu do listy				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}    	
    }
    
    //Jeœli klient zosta³ wyproszony i jego wspó³rzêdna X jest mniejsza od 0 (klient poza plansz¹)
    //To go usuñ
    public void usunKlientow()
    {
		try {
			// Opusc semafor dostepu do listy
			dostepDoListy.acquire();		
			
			for(int i = 0; i < pijaniKlienci.size(); i++)
	  			if(pijaniKlienci.get(i).getWyproszony() && pijaniKlienci.get(i).getX() < 0)
	  				pijaniKlienci.remove(i);
			
			dostepDoListy.release();			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        	
    }

	public void rysuj(Graphics g)
    {
		try {
			dostepDoListy.acquire();
			for(Klient k : klienci)
	  			k.rysuj(g);
			
			for(Klient k : pijaniKlienci)
	  			k.rysuj(g);		
			
			dostepDoListy.release();			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
