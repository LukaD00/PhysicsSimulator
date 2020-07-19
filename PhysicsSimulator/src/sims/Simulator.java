package sims;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * 
 * Simulator is an abstract class for all simulations.
 * Mostly important just to define an identical launch method for every simulator for the main frame to use.
 * 
 * @author Luka
 *
 */
@SuppressWarnings("serial")
public abstract class Simulator extends JFrame {
	
	/**
	 * Common elements of every simulator's window should go here.
	 */
	public Simulator() {
		this.setLayout(new BorderLayout());
	}
	
	/**
	 * Gets the name of the simulator. Useful for the Main Frame to name simulator buttons, 
	 * and for the launch method to name the window.
	 */
	public abstract String getName();
	
	/**
	 * A workaround to get a reference to the subclass inside the launch method.
	 * 
	 * @return reference to the subclass
	 */
	private JFrame getWindow() {
		return this;
	}
	
	/**
	 * Sets the parameters for the simulation's window.
	 */
	public final void launch() {
		SwingUtilities.invokeLater(() -> {
			JFrame window = getWindow();
			window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
			window.setSize(new Dimension(1000, 750));
			window.setTitle(this.getName());
			window.setVisible(true);
		});
	}
	
}
