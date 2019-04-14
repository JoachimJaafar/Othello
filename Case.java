import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Case extends JPanel implements ActionListener{
    private JButton btn;
    private String etat;
	private boolean est_selectionne = false;

    public Case(String c){
        etat = c;
        btn = new JButton(); //creates new button
        btn.setSize(75,69); //inutile!
        btn.setIcon(new ImageIcon(etat));
    }

    public JButton getBtn(){
        return btn;
    }

	public void addAction(){
		btn.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e){
	    verif_select();
    }

	public void verif_select(){
		if(etat == "vide.jpeg" || etat == "aide.jpeg") selectionne();
	}
    
    public String getEtat(){
        return etat;
    }

	public boolean getSelection(){
		return est_selectionne;
	}

	public void deselectionne(){
		est_selectionne = false;
	}

	public void selectionne(){
		est_selectionne = true;
	}

	public void setEtat(String e){
		etat = e;
		if(e == "aide.jpeg") this.getBtn().setBackground(Color.decode("#0096e3"));
		else this.getBtn().setBackground(Color.decode("#009f00"));
		btn.setIcon(new ImageIcon(etat));
	}
    
    public boolean estAdverse(Case c){
        return (this.getEtat() == "noir.jpeg" && c.getEtat() == "blanc.jpeg") || (this.getEtat() == "blanc.jpeg" && c.getEtat() == "noir.jpeg");
    }
    
    public boolean estAllie(Case c){
        return (this.getEtat() == c.getEtat() && (this.getEtat() != "vide.jpeg" || this.getEtat() != "aide.jpeg"));
    }

	public void change(){
		setEtat(etat=="noir.jpeg" ? "blanc.jpeg" : "noir.jpeg");
	}

}
