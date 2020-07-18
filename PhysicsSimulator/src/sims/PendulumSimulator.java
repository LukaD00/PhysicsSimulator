package sims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import constants.Physics;
import constants.PhysicsRatios;

import guiHelperClasses.InputLine;
import guiHelperClasses.ButtonControl;


/**
 * PendulumSimulator is a simple pendulum simulator. 
 * So far, you can choose 2 starting parameters - length and starting angle.
 * 
 * @author Luka
 *
 */
@SuppressWarnings("serial")
public class PendulumSimulator extends Simulator {

	private double length; 
	private final double DEFAULT_LENGTH = 5;
	
	private double angle;
	private final double DEFAULT_START_ANGLE = Math.PI/4;
	
	private double velocity;
	private final double START_VELOCITY = 0;
	
	public PendulumSimulator() {
		super();
		
		// default starting positions, can be changed via text fields and buttons
		velocity = START_VELOCITY;
		length = DEFAULT_LENGTH;
		angle = DEFAULT_START_ANGLE;
		
		
		// CENTER PANEL
		Pendulum pendulum = new Pendulum();
		this.add(pendulum, BorderLayout.CENTER);
		
		
		// SOUTH PANEL
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
		
		// length
		InputLine lengthInput = new InputLine("Pendulum length (m): ", DEFAULT_LENGTH);
		southPanel.add(lengthInput);

		// starting angle
		InputLine angleInput = new InputLine("Starting angle (deg): ", Math.toDegrees(DEFAULT_START_ANGLE)); 
		southPanel.add(angleInput);
		
		// control buttons
		ButtonControl buttons = new ButtonControl();
		buttons.addActionListener(e -> {
			if (e.getActionCommand()=="start") {
				/**
				 *  checks if inputs are valid - if so, starts the pendulum
				 *  with new values, else throws errors next to invalid inputs
				 */
				boolean validInput = true;	
				try {
					length = lengthInput.getValue();
					lengthInput.setErrorLabel("");
				} catch (NullPointerException | NumberFormatException ex) {
					lengthInput.setErrorLabel("Invalid input!");
					validInput = false;
				}
				
				try {
					angle = Math.toRadians(angleInput.getValue());
					angleInput.setErrorLabel("");
				} catch (NullPointerException | NumberFormatException ex) {
					angleInput.setErrorLabel("Invalid input!");
					validInput = false;
				}
				
				if (validInput) {
					velocity = 0;
					pendulum.start();
				}
				
			} else if (e.getActionCommand()=="resume") {
				pendulum.resume();
			} else if (e.getActionCommand()=="pause") {
				pendulum.pause();
			}
		});
		southPanel.add(buttons);
		

		this.add(southPanel, BorderLayout.SOUTH);
	}
	
	
	/**
	 *  Used just for testing this specific Simulator. Will be removed later.
	 */
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(() -> {
				PendulumSimulator window = new PendulumSimulator();
				window.setVisible(true);
				window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				window.setSize(1500,1000);
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This is the actual animation that the main frame of the PendulumSimulator uses.
	 * PendulumSimulator keeps the information about the pendulum's length, angle and velocity.
	 * Pendulum calculates the current acceleration, and updates the velocity and angle accordingly,
	 * and then it uses those values to draw a pendulum.
	 */
	private class Pendulum extends JPanel implements ActionListener {
		
		/*
		 *  Timer notifies its ActionListener, the Pendulum, every TIME_STEP milliseconds,
		 *  Pendulum's actionPerformed method triggers, which calls repaint, which
		 *  then calls paintComponent again.
		 *  
		 *  The timer can be controlled with the Pendulum's start, pause and resume methods.
		 */
		private final int TIME_STEP = 1;
		Timer timer = new Timer(TIME_STEP, this); 
		
		public void start() {
			timer.restart();
		}
		public void pause() {
			timer.stop();
		}
		public void resume() {
			timer.start();
		}

		/**
		 * The pendulum is painted here.
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
					
			//calculating new acceleration, velocity and angle
			double acceleration = -1 * Physics.G * Math.sin(angle) / length; // a = -g/l * sin(angle)
			velocity += acceleration * TIME_STEP/1000;
			angle += velocity * TIME_STEP/1000;
			
			//calculating new position (relative to top of pendulum)
			int x = (int) (length * PhysicsRatios.METER_TO_PIXEL * Math.sin(angle));
			int y = (int) (length * PhysicsRatios.METER_TO_PIXEL * Math.cos(angle));
			
			//calculating top of pendulum location
			int PendulumCenterX = this.getWidth() / 2; // centered in its panel
			int PendulumCenterY = 40; // offset from top 
			
			g.setColor(Color.BLACK);
			g.drawLine(PendulumCenterX, PendulumCenterY,
					PendulumCenterX+x, PendulumCenterY+y);	
		}

		/**
		 *  Timer triggers actionPerformed, which triggers a repaint of the pendulum.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			this.repaint();
		}
		
	}
	
}
