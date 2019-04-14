import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Main extends JPanel implements ActionListener {
	private static String iaString = "Contre l'ordinateur";
	private static String jcjString = "Joueur contre Joueur";
    private static String facileString = "Facile";
    private static String moyenString = "Moyen";
    private static String difficileString = "Difficile";
	private static String noirString = "Noir";
	private static String blancString = "Blanc";
	private static String j1 = "noir.jpeg";
	private static String j2 = "blanc.jpeg";
    private String diffi = "Facile";
    private boolean mode_ia = true;
    private String coul = "Noir";
 
    public Main() {
        super(new BorderLayout());
 
        //Create the radio buttons.
        JRadioButton iaButton = new JRadioButton(iaString);
        iaButton.setActionCommand(iaString);
        iaButton.setSelected(true);
        
        JRadioButton jcjButton = new JRadioButton(jcjString);
        jcjButton.setActionCommand(iaString);
        jcjButton.setSelected(true);
        
        JRadioButton facileButton = new JRadioButton(facileString);
        facileButton.setActionCommand(facileString);
        facileButton.setSelected(true);
 
        JRadioButton moyenButton = new JRadioButton(moyenString);
        moyenButton.setActionCommand(moyenString);
        moyenButton.setSelected(true);
 
        JRadioButton difficileButton = new JRadioButton(difficileString);
        difficileButton.setActionCommand(difficileString);
        difficileButton.setSelected(true);
        
        JRadioButton j1Button = new JRadioButton(noirString);
        j1Button.setActionCommand(noirString);
        j1Button.setSelected(true);
        
        JRadioButton j2Button = new JRadioButton(blancString);
        j2Button.setActionCommand(blancString);
        j2Button.setSelected(true);
        
        j1Button.addActionListener(
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				coul = "Noir";
        	}
        });
        j2Button.addActionListener(
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				coul = "Blanc";
        	}
        });
 
        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(iaButton);
        group.add(jcjButton);
        
        ButtonGroup group2 = new ButtonGroup();
        group2.add(facileButton);
        group2.add(moyenButton);
        group2.add(difficileButton);
        
        ButtonGroup group3 = new ButtonGroup();
        group3.add(j1Button);
        group3.add(j2Button);
 
        //Register a listener for the radio buttons.
        iaButton.addActionListener(
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode_ia = true;
        	}
        });
        jcjButton.addActionListener(
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mode_ia = false;
        	}
        });
        
        facileButton.addActionListener(act_diffi);
        moyenButton.addActionListener(act_diffi);
        difficileButton.addActionListener(act_diffi);
        
        j1Button.addActionListener(act_coul);
        j2Button.addActionListener(act_coul);
 
 
        //Put the radio buttons in a column in a panel.
        JPanel panel = new JPanel(new GridLayout(1, 0));
        panel.add(new JLabel("Mode de jeu : "));
        panel.add(iaButton);
        panel.add(jcjButton);
        
        JPanel panel2 = new JPanel(new GridLayout(1, 0));
        panel2.add(new JLabel("Difficulté (de l'ordinateur) : "));
        panel2.add(facileButton);
        panel2.add(moyenButton);
        panel2.add(difficileButton);
        
        JPanel panel3 = new JPanel(new GridLayout(1, 0));
        panel3.add(new JLabel("Couleur du joueur 1 : "));
        panel3.add(j1Button);
        panel3.add(j2Button);
        
        JPanel panel4 = new JPanel(new GridLayout(1, 0));
        JButton valider = new JButton("Valider");
        valider.addActionListener(
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Plateau p = new Plateau(mode_ia, diffi,coul);
				System.out.println(mode_ia+", "+diffi+", "+coul);
        	}
        });
        JButton quitter = new JButton("Quitter");
        quitter.addActionListener(
		new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
        	}
        });
        
        panel4.add(valider);
        panel4.add(quitter);
        
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 1));
        buttonsPanel.add(panel, BorderLayout.NORTH);
        buttonsPanel.add(panel2, BorderLayout.CENTER);
        buttonsPanel.add(panel3, BorderLayout.SOUTH);
        
 		add(panel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);
        add(panel4, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        
    }
 
    /** Listens to the radio buttons. */
    public void actionPerformed(ActionEvent e) {
        //choix = e.getActionCommand();
    }
    
    ActionListener act_diffi = new ActionListener() {
									public void actionPerformed(ActionEvent e) {
									   		diffi = e.getActionCommand();
		    						   }
        						   };
        						   
    ActionListener act_coul = new ActionListener() {
									   public void actionPerformed(ActionEvent e) {
									   		coul = e.getActionCommand();
		    						   }
        						   };
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new Main();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
