/*A FAIRE
	-Corriger bug quand on pose les pions rapidement
	-Faire la stratégie miximisante (difficile) et random (moyen)
	-Faire passer le tour d'un joueur s'il ne peut pas poser de pions -> si les deux joueurs ne peuvent pas (plateau pas forcément rempli): la partie est finie (faire les deux fonctions)
	-Changer images!!!
	-Faire les points (nbr de couleurs pour chaque joueur en temps réel) + affichages (victoire, defaite, blocage...)
*/

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;
import java.io.File;
import java.text.SimpleDateFormat;

public class Plateau extends JPanel implements ActionListener{

    private JFrame frame = new JFrame();
	private Case[][] cases = new Case[T][T]; //cases du plateau
	private static final int T=8;     //T pour taille
	private boolean tour_joueur = true;
	private boolean bot_joue = false;
	private boolean mode_IA;
	private JLabel sn;
	private JLabel sb;
	private JLabel chl;
	private String diffi;
	private String coul;
	private String image_j1;
	private String image_j2;
	private boolean test=false; //pour que l'IA commence si joueur choisit le pion blanc
	private Date start = new Date();
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
	

    public Plateau(boolean ia, String dif, String cl){ //constructor
    	image_j1 = "noir.jpeg";
    	image_j2 = "blanc.jpeg";
    	diffi = dif;
    	mode_IA = ia;
    	coul = cl;
		if(ia && coul=="Blanc") test = true;
        frame.setLayout(new GridLayout(1,1));
        init(frame);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true); 
    }

    public void init(JFrame frame){ 
        JPanel panelButtons = new JPanel(new GridLayout(8, 8));
        Case c;
        for(int x=0; x<T; x++){
            for(int y=0; y<T; y++){
                if((x==4 && y==3)||(x==3 && y==4)) c = new Case(image_j1);
                else{
                    if((x==3 && y==3)||(x==4 && y==4)) c = new Case(image_j2);
                    else c = new Case("vide.jpeg");
                }
                cases[x][y] = c;
                c.getBtn().setBackground(Color.decode("#009f00"));
				c.getBtn().addActionListener(this);
				c.addAction(); 
                panelButtons.add(c.getBtn());
			}
        }
		Timer timer = new Timer();
		timer.schedule(
			new TimerTask(){
				public void run() {
					if(test){	//POUR QUE LE L'IA COMMENCE SI LE JOUEUR CHOISI LES NOIRS
						test=false;
						un_tour();
					}
				}
			},1000
		);
        JPanel panelAide = new JPanel(new GridLayout(5, 0));
        panelAide.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.setLocationRelativeTo(null);
		JButton aide = new JButton("aide");
		aide.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(!bot_joue) afficherAide();
	        	}
	        }
	    );
	    JButton quit = new JButton("Quitter la partie");
		quit.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
	        	}
	        }
	    );
	    JLabel jl = new JLabel("Mode "+(mode_IA ? "contre l'ordinateur ("+diffi+")" : "joueur vs joueur")+", le "+(mode_IA ? "joueur" : "joueur 1")+" joue les pions "+(coul == "Blanc" ? "blancs et joue donc en deuxième." : "noirs et joue donc en premier."));
	    sn = new JLabel("Score noir : "+getScoreNoir());
	    sb = new JLabel("Score blanc : "+getScoreBlanc());
	    chl = new JLabel("00:00");
	    JPanel panlab = new JPanel(new GridLayout(4, 0, 10, 10));
	    panlab.add(jl);
	    panlab.add(sn);
	    panlab.add(sb);
	    panlab.add(chl);
	    JPanel pantest = new JPanel(new GridLayout(2, 0, 10, 10));
	    pantest.add(aide);
	    pantest.add(quit);
	    panelAide.add(panlab);
	    panelAide.add(pantest, BorderLayout.SOUTH);
        frame.add(panelButtons, BorderLayout.WEST);
        frame.add(panelAide, BorderLayout.EAST);
		Timer chrono = new Timer();
		chrono.schedule(
			new TimerTask(){
				public void run() {
					updateChrono();
				}
			},0,1000
		);
    }

	public void actionPerformed(ActionEvent e){
        if(!partieFinie()){
			if(mode_IA) joue_IA();
			else {
				joue();
			}
		}
		else JOptionPane.showMessageDialog(frame, "La partie est finie avec une victoire des pions "+(getScoreBlanc() > getScoreNoir() ? "blancs." : "noirs" + "au bout de "+chl.getText()));
    }

	
	public int getScoreNoir(){
		int nb = 0;
		for(int x=0; x<T; x++){
            for(int y=0; y<T; y++){
            	if(cases[x][y].getEtat() == "image_j1") nb++;
            }
        }
        return nb;
	}
	
	public int getScoreBlanc(){
		int nb = 0;
		for(int x=0; x<T; x++){
            for(int y=0; y<T; y++){
            	if(cases[x][y].getEtat() == "image_j2") nb++;
            }
        }
        return nb;
	}
	
	public void updateScore(){
		sn.setText("Score noir : "+getScoreNoir());
		sb.setText("Score blanc : "+getScoreBlanc());
	}
	
	public void updateChrono(){
		Date now = new Date();
		chl.setText(sdf.format(new Date(now.getTime() - start.getTime())));
	}
	
	public boolean joue(){
		boolean a_pu_jouer = false;
		for(int x=0; x<T; x++){
            for(int y=0; y<T; y++){
				if(cases[x][y].getSelection()){
					if(tour_joueur) cases[x][y].setEtat(image_j1);
					else cases[x][y].setEtat(image_j2);
					if(coup_possible(x,y)){
						place_pion(x,y);
						updateScore();
						removeAide();
						tour_joueur = !tour_joueur;
						a_pu_jouer = true;
					}
					else cases[x][y].setEtat("vide.jpeg");
					cases[x][y].deselectionne();
					x=T;
					y=T;
				}
			}
		}
		return a_pu_jouer;
	}

	public void joue_IA(){
		bot_joue = true;
		for(int x=0; x<T; x++){
		    for(int y=0; y<T; y++){
				if(cases[x][y].getSelection()){
					if(tour_joueur) cases[x][y].setEtat(image_j1);
					else cases[x][y].setEtat(image_j2);
					if(coup_possible(x,y)){
						place_pion(x,y);
						tour_joueur = !tour_joueur;
						removeAide();
						Timer timer = new Timer();
						timer.schedule(
							new TimerTask(){
								public void run() {
									un_tour();
									bot_joue = false;
								}
							},1000
						);
					}
					else cases[x][y].setEtat("vide.jpeg");
					cases[x][y].deselectionne();
					x=T;
					y=T;
				}
			}
		}
		updateScore();
	}

	public void un_tour(){
		if(diffi == "Facile"){
			for(int x=0; x<T; x++){
				for(int y=0; y<T; y++){
					cases[x][y].verif_select();
					if(cases[x][y].getSelection()){
						if(tour_joueur) cases[x][y].setEtat(image_j1);
						else cases[x][y].setEtat(image_j2);
						if(joue()){
							x=T;
							y=T;
						}
					}
				}
			}
		}
		else {
			java.util.List<Integer> coords = new ArrayList<Integer>();
			afficherAide();
			for(int x=0; x<T; x++){
			    for(int y=0; y<T; y++){
					if(cases[x][y].getEtat() == "aide.jpeg"){
						coords.add(x);
						coords.add(y);
					}
				}
			}
			removeAide();
			if(coords.size()>0){
				if(diffi == "Moyen"){
					int r = new Random().nextInt(coords.size()/2);
					int x = coords.get(2*r);
					int y = coords.get((2*r)+1);
					if(tour_joueur) cases[x][y].setEtat(image_j1);
					else cases[x][y].setEtat(image_j2);
					place_pion(x,y);
					tour_joueur = !tour_joueur;
				}
				else {	// correspond à: if (diffi == "Difficile")
					int nb=0;
					int x = coords.get(0);
					int y = coords.get(1);
					Iterator<Integer> it = coords.iterator();
					while (it.hasNext()){
						int a = it.next();
						int b = it.next();
						int calcul = nbCoupsPossibles(a,b);
						if(calcul > nb){
							nb = calcul;
							x=a;
							y=b;
						}
					}
					if(tour_joueur) cases[x][y].setEtat(image_j1);
					else cases[x][y].setEtat(image_j2);
					place_pion(x,y);
					tour_joueur = !tour_joueur;						
				}
			}
		}
		updateScore();
	}

	public boolean posExiste(int x, int y){
    	return x>=0 && x<=7 && y>=0 && y<=7;
    }
    
    public boolean coup_possible(int x, int y){
   		if(!posExiste(x,y)) return false; 
    	Case c = cases[x][y];
    	if((posExiste(x-1,y) && c.estAdverse(cases[x-1][y])) ||
    	   (posExiste(x+1,y) && c.estAdverse(cases[x+1][y])) ||
    	   (posExiste(x,y-1) && c.estAdverse(cases[x][y-1])) ||
           (posExiste(x,y+1) && c.estAdverse(cases[x][y+1])) ||
           (posExiste(x+1,y+1) && c.estAdverse(cases[x+1][y+1])) ||
    	   (posExiste(x+1,y-1) && c.estAdverse(cases[x+1][y-1])) ||
    	   (posExiste(x-1,y-1) && c.estAdverse(cases[x-1][y-1])) ||
           (posExiste(x-1,y+1) && c.estAdverse(cases[x-1][y+1]))){
    		int x2 = x;
    		int y2 = y;
    		//VERTICALEMENT
    		if(posExiste(x-1,y) && c.estAdverse(cases[x-1][y])){
    			x2 = x-1;
    			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x-1][y])){
    				x2--;
    			}
    			if(posExiste(x2,y) && c.estAllie(cases[x2][y])){
    				return true;
    			}
    		}
    		if(posExiste(x+1,y) && c.estAdverse(cases[x+1][y])){
    			x2 = x+1;
    			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x+1][y])){
    				x2++;
    			}
    			if(posExiste(x2,y) && c.estAllie(cases[x2][y])){
    				return true;
    			}
    		}
    		//HORIZONTALEMENT
    		if(posExiste(x,y-1) && c.estAdverse(cases[x][y-1])){
    			y2 = y-1;
    			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y-1])){
    				y2--;
    			}
    			if(posExiste(x,y2) && c.estAllie(cases[x][y2])){
    				return true;
    			}
    		}
    		if(posExiste(x,y+1) && c.estAdverse(cases[x][y+1])){
    			y2 = y+1;
    			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y+1])){
    				y2++;
    			}
    			if(posExiste(x,y2) && c.estAllie(cases[x][y2])){
    				return true;
    			}
    		}
    		//DIAGONALE
    		if(posExiste(x+1,y+1) && c.estAdverse(cases[x+1][y+1])){
    			x2 = x+1;
    			y2 = y+1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x+1][y+1])){
    				x2++;
    				y2++;
    			}
    			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
    				return true;
    			}
    		}
    		if(posExiste(x+1,y-1) && c.estAdverse(cases[x+1][y-1])){
    			x2 = x+1;
    			y2 = y-1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x+1][y-1])){
    				x2++;
    				y2--;
    			}
    			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
    				return true;
    			}
    		}
    		if(posExiste(x-1,y-1) && c.estAdverse(cases[x-1][y-1])){
    			x2 = x-1;
    			y2 = y-1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x-1][y-1])){
    				x2--;
    				y2--;
    			}
    			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
    				return true;
    			}
    		}
    		if(posExiste(x-1,y+1) && c.estAdverse(cases[x-1][y+1])){
    			x2 = x-1;
    			y2 = y+1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x-1][y+1])){
    				x2--;
    				y2++;
    			}
    			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
    				return true;
    			}
    		}
    		//FIN DIAGONALE
    	}
    	return false;
    }  
	
    public int nbCoupsPossibles(int x, int y){
    	Case c = cases[x][y];
    	int nbPossible = 0, tmp = 0;
		int x2 = x;
		int y2 = y;
		//VERTICALEMENT
		if(posExiste(x-1,y) && c.estAdverse(cases[x-1][y])){
			x2 = x-1;
			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x-1][y])){
				x2--;
				tmp++;
			}
			if(posExiste(x2,y) && c.estAllie(cases[x2][y])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		if(posExiste(x+1,y) && c.estAdverse(cases[x+1][y])){
			x2 = x+1;
			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x+1][y])){
				x2++;
				tmp++;
			}
			if(posExiste(x2,y) && c.estAllie(cases[x2][y])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		//HORIZONTALEMENT
		if(posExiste(x,y-1) && c.estAdverse(cases[x][y-1])){
			y2 = y-1;
			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y-1])){
				y2--;
				tmp++;
			}
			if(posExiste(x,y2) && c.estAllie(cases[x][y2])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		if(posExiste(x,y+1) && c.estAdverse(cases[x][y+1])){
			y2 = y+1;
			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y+1])){
				y2++;
				tmp++;
			}
			if(posExiste(x,y2) && c.estAllie(cases[x][y2])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		//DIAGONALE
		if(posExiste(x+1,y+1) && c.estAdverse(cases[x+1][y+1])){
			x2 = x+1;
			y2 = y+1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x+1][y+1])){
				x2++;
				y2++;
				tmp++;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		if(posExiste(x+1,y-1) && c.estAdverse(cases[x+1][y-1])){
			x2 = x+1;
			y2 = y-1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x+1][y-1])){
				x2++;
				y2--;
				tmp++;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		if(posExiste(x-1,y-1) && c.estAdverse(cases[x-1][y-1])){
			x2 = x-1;
			y2 = y-1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x-1][y-1])){
				x2--;
				y2--;
				tmp++;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		if(posExiste(x-1,y+1) && c.estAdverse(cases[x-1][y+1])){
			x2 = x-1;
			y2 = y+1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x-1][y+1])){
				x2--;
				y2++;
				tmp++;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				nbPossible += tmp;
			}
			tmp = 0;
		}
		//FIN DIAGONALE
    	return nbPossible;
    }	
    
    public void place_pion(int x, int y){
    	Case c = cases[x][y];
		int x2 = x;
		int y2 = y;
		//VERTICALEMENT
		if(posExiste(x-1,y) && c.estAdverse(cases[x-1][y])){
			x2 = x-1;
			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x-1][y])){
				x2--;
			}
			if(posExiste(x2,y) && c.estAllie(cases[x2][y])){
				x2++;
				while(x2!=x){
					cases[x2][y].change();
					x2++;
				}
			}
		}
		if(posExiste(x+1,y) && c.estAdverse(cases[x+1][y])){
			x2 = x+1;
			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x+1][y])){
				x2++;
			}
			if(posExiste(x2,y) && c.estAllie(cases[x2][y])){
				x2--;
				while(x2!=x){
					cases[x2][y].change();
					x2--;
				}
			}
		}
		//HORIZONTALEMENT
		if(posExiste(x,y-1) && c.estAdverse(cases[x][y-1])){
			y2 = y-1;
			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y-1])){
				y2--;
			}
			if(posExiste(x,y2) && c.estAllie(cases[x][y2])){
				y2++;
				while(y2!=y){
					cases[x][y2].change();
					y2++;
				}
			}
		}
		if(posExiste(x,y+1) && c.estAdverse(cases[x][y+1])){
			y2 = y+1;
			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y+1])){
				y2++;
			}
			if(posExiste(x,y2) && c.estAllie(cases[x][y2])){
				y2--;
				while(y2!=y){
					cases[x][y2].change();
					y2--;
				}
			}
		}
		//DIAGONALE
		if(posExiste(x+1,y+1) && c.estAdverse(cases[x+1][y+1])){
			x2 = x+1;
			y2 = y+1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x+1][y+1])){
				x2++;
				y2++;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				x2--;
				y2--;
				while(x2!=x){ //on peut rajouter "y2!=y" avec && ou || je crois mais inutile LOGIQUEMENT
					cases[x2][y2].change();
					x2--;
					y2--;
				}
			}
		}
		if(posExiste(x+1,y-1) && c.estAdverse(cases[x+1][y-1])){
			x2 = x+1;
			y2 = y-1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x+1][y-1])){
				x2++;
				y2--;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				x2--;
				y2++;
				while(x2!=x){ //on peut rajouter "y2!=y" avec && ou || je crois mais inutile LOGIQUEMENT
					cases[x2][y2].change();
					x2--;
					y2++;
				}
			}
		}
		if(posExiste(x-1,y-1) && c.estAdverse(cases[x-1][y-1])){
			x2 = x-1;
			y2 = y-1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x-1][y-1])){
				x2--;
				y2--;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				x2++;
				y2++;
				while(x2!=x){ //on peut rajouter "x2!=x" avec && ou || je crois mais inutile LOGIQUEMENT
					cases[x2][y2].change();
					x2++;
					y2++;
				}
			}
		}
		if(posExiste(x-1,y+1) && c.estAdverse(cases[x-1][y+1])){
			x2 = x-1;
			y2 = y+1;
			while(posExiste(x2,y2) && cases[x2][y2].estAllie(cases[x-1][y+1])){
				x2--;
				y2++;
			}
			if(posExiste(x2,y2) && c.estAllie(cases[x2][y2])){
				x2++;
				y2--;
				while(y2!=y){ //on peut rajouter "x2!=x" avec && ou || je crois mais inutile LOGIQUEMENT
					cases[x2][y2].change();
					x2++;
					y2--;
				}
			}
		}
		//FIN DIAGONALE
    }	
   
	
	public boolean getAide(int x, int y){ //return s'il y a des aides
    	Case c = cases[x][y];
    	if((c.getEtat() == "vide.jpeg" || c.getEtat() == "aide.jpeg") && 
    	   ((posExiste(x-1,y) && cases[x-1][y].estAdverse(c)) ||
    	   (posExiste(x+1,y) && cases[x+1][y].estAdverse(c)) ||
    	   (posExiste(x,y-1) && cases[x][y-1].estAdverse(c)) ||
           (posExiste(x,y+1) && cases[x][y+1].estAdverse(c)) ||
           (posExiste(x+1,y+1) && cases[x+1][y+1].estAdverse(c)) ||
    	   (posExiste(x+1,y-1) && cases[x+1][y-1].estAdverse(c)) ||
    	   (posExiste(x-1,y-1) && cases[x-1][y-1].estAdverse(c)) ||
           (posExiste(x-1,y+1) && cases[x-1][y+1].estAdverse(c)))){
           	int x2 = x;
    		int y2 = y;
    		//VERTICALEMENT
			if(posExiste(x-1,y) && cases[x-1][y].estAdverse(c)){
    			x2 = x-1;
    			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x-1][y])){
    				x2--;
    			}
    			if(posExiste(x2,y) && cases[x2][y].estAllie(c)){
    				return true;
    			}
    		}
    		if(posExiste(x+1,y) && cases[x+1][y].estAdverse(c)){
    			x2 = x+1;
    			while(posExiste(x2,y) && cases[x2][y].estAllie(cases[x+1][y])){
    				x2++;
    			}
    			if(posExiste(x2,y) && cases[x2][y].estAllie(c)){
    				return true;
    			}
    		}
    		//HORIZONTALEMENT
    		if(posExiste(x,y-1) && cases[x][y-1].estAdverse(c)){
    			y2 = y-1;
    			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y-1])){
    				y2--;
    			}
    			if(posExiste(x,y2) && cases[x][y2].estAllie(c)){
    				return true;
    			}
    		}
    		if(posExiste(x,y+1) && cases[x][y+1].estAdverse(c)){
    			y2 = y+1;
    			while(posExiste(x,y2) && cases[x][y2].estAllie(cases[x][y+1])){
    				y2++;
    			}
    			if(posExiste(x,y2) && cases[x][y2].estAllie(c)){
    				return true;
    			}
    		}
    		//DIAGONALE
    		if(posExiste(x+1,y+1) && cases[x+1][y+1].estAdverse(c)){
    			x2 = x+1;
    			y2 = y+1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				x2++;
    				y2++;
    			}
    			if(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				return true;
    			}
    		}
    		if(posExiste(x+1,y-1) && cases[x+1][y-1].estAdverse(c)){
    			x2 = x+1;
    			y2 = y-1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				x2++;
    				y2--;
    			}
    			if(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				return true;
    			}
    		}
    		if(posExiste(x-1,y-1) && cases[x-1][y-1].estAdverse(c)){
    			x2 = x-1;
    			y2 = y-1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				x2--;
    				y2--;
    			}
    			if(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				return true;
    			}
    		}
    		if(posExiste(x-1,y+1) && cases[x-1][y+1].estAdverse(c)){
    			x2 = x-1;
    			y2 = y+1;
    			while(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				x2--;
    				y2++;
    			}
    			if(posExiste(x2,y2) && cases[x2][y2].estAllie(c)){
    				return true;
    			}
    		}
           } return false;
           
    }
    
	public void afficherAide(){ //afficher l'aide
        for(int x=0; x<8; x++){
        	for(int y=0; y<8; y++){
        		if(cases[x][y].getEtat() == "vide.jpeg"){
		    		if(tour_joueur) cases[x][y].setEtat(image_j1);
					else cases[x][y].setEtat(image_j2);
		    		if(coup_possible(x,y)) cases[x][y].setEtat("aide.jpeg");
		    		else cases[x][y].setEtat("vide.jpeg");
		    		cases[x][y].deselectionne();
        		}
        	}
        }
	}   
    
	public void removeAide(){ //met les cases d'aide en vide
        for(int a=0; a<8; a++){
            for(int b=0; b<8; b++){
            	if(cases[a][b].getEtat() == "aide.jpeg") cases[a][b].setEtat("vide.jpeg");
            }
        }
	}
	
	public boolean partieFinie(){
		return(getScoreBlanc() + getScoreNoir() == T*T);
	}
}
