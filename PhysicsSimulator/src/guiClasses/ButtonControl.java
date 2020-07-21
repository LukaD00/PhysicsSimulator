package guiClasses;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import constants.GUI;

/**
 * ButtonControl is JPanel consisting of three side-by-side buttons for
 * starting, pausing or resuming the simulation.
 * 
 * Depending on the button pressed, fires one of the three ActionEvents,
 * "start", "pause" or "resume".
 * 
 * @author Luka
 *
 */
@SuppressWarnings("serial")
public class ButtonControl extends JPanel {
	
	private JButton startButton;
	private JButton resumeButton;
	private JButton pauseButton;
	
	private List<ActionListener> listeners;
	
	/**
	 * Initializes the ButtonControl, always the same.
	 */
	public ButtonControl() {
		listeners = new ArrayList<ActionListener>();
		startButton = new JButton("Start");
		resumeButton = new JButton("Resume");
		pauseButton = new JButton("Pause");
		
		startButton.setPreferredSize(GUI.PREFFERED_SIZE);
		startButton.setMaximumSize(GUI.MAXIMUM_SIZE);
		resumeButton.setPreferredSize(GUI.PREFFERED_SIZE);
		resumeButton.setMaximumSize(GUI.MAXIMUM_SIZE);
		pauseButton.setPreferredSize(GUI.PREFFERED_SIZE);
		pauseButton.setMaximumSize(GUI.MAXIMUM_SIZE);
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(startButton); 
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(resumeButton);
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(pauseButton);
		this.add(Box.createRigidArea(GUI.RIGID));
		
		startButton.addActionListener(e -> {
			for (ActionListener listener : listeners) {
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
						"start");
				listener.actionPerformed(event);
			}
		});
		resumeButton.addActionListener(e -> {
			for (ActionListener listener : listeners) {
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
						"resume");
				listener.actionPerformed(event);
			}
		});
		pauseButton.addActionListener(e -> {
			for (ActionListener listener : listeners) {
				ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
						"pause");
				listener.actionPerformed(event);
			}
		});
	} 
	
	/**
	 * Registers an action listener to listen to all three buttons of the ButtonControl.
	 * Three different ActionEvents can be fired: "start", "pause" or "resume".
	 * 
	 * @param listener listener to be added
	 */
	public void addActionListener(ActionListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

}
