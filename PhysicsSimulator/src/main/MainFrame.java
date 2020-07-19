package main;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import sims.PendulumSimulator;
import sims.Simulator;

/**
 * The main menu for the Physics Simulator - consists of a button for every
 * available simulation.
 * 
 * @author Luka
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private Simulator[] sims = {new PendulumSimulator()};
	
	public MainFrame() {
		
		this.setLayout(new GridLayout(0,1));
		this.setTitle("Physics Simulator");
		
		for (Simulator sim : sims) {
			JButton btn = new JButton(sim.getName());
			btn.addActionListener(e -> {
				sim.launch();
			});
			this.add(btn);
		}
		
	}
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(() -> {
				MainFrame window = new MainFrame();
				window.setSize(new Dimension(300,75*window.sims.length));
				window.setVisible(true);
				window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
