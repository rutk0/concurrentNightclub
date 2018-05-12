import javax.swing.JFrame;

public class PodstawaOkna extends JFrame{

	private static final long serialVersionUID = 1L;

	//Tworzy okno JFrame, wstawia do niego panel OknoGlowne
	public PodstawaOkna(int szerokosc, int wysokosc)
	{
		//Tworzymy okno pocz¹tkowe
		super("Rutkowski KLUB");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Widocznosc
		setVisible(true);
		setSize(szerokosc, wysokosc);
		OknoGlowne projekt = new OknoGlowne(szerokosc, wysokosc);
		
		//Dodaj do widoku
		getContentPane().add(projekt);
		//Rozpoczecie symulacji
		projekt.start();	
	}
}
