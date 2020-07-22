package sims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.Timer;

import constants.GUI;
import constants.Physics;
import constants.PhysicsRatios;

import guiClasses.ButtonControl;
import guiClasses.CheckBoxLine;
import guiClasses.InputLine;
import guiClasses.Vector;


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
	
	private boolean showVelocity;
	
	public String getName() {
		return "Pendulum";
	}
	
	public PendulumSimulator() {
		super();
		
		// default starting positions, can be changed via text fields and buttons
		velocity = START_VELOCITY;
		length = DEFAULT_LENGTH;
		angle = DEFAULT_START_ANGLE;
		showVelocity = false;
		
		
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
		
		// velocity vector option
		CheckBoxLine velocityCheck = new CheckBoxLine("Velocity vector?");
		velocityCheck.addActionListener(e -> {
			if (velocityCheck.isSelected()) showVelocity = true;
			else showVelocity = false;
		});
		southPanel.add(velocityCheck);
		

		
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
					velocity = START_VELOCITY;
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
	 *  just for testing
	 */
	public static void main(String[] args) {
		PendulumSimulator pendulum = new PendulumSimulator();
		pendulum.launch();
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
			
			// calculating top of pendulum location
			int PendulumCenterX = this.getWidth() / 2; // centered in its panel
			int PendulumCenterY = 40; // offset from top 
					
			// calculating new acceleration, velocity and angle
			double acceleration = -1 * Physics.G * Math.sin(angle) / length; // a = -g/l * sin(angle)
			velocity += acceleration * TIME_STEP/1000;
			angle += velocity * TIME_STEP/1000;
			
			// calculating new position 
			int x = (int) (length * Math.sin(angle) * PhysicsRatios.METER_TO_PIXEL) + PendulumCenterX;
			int y = (int) (length * Math.cos(angle) * PhysicsRatios.METER_TO_PIXEL) + PendulumCenterY;
			
			// drawing pendulum
			g.setColor(Color.BLACK);
			g.drawLine(PendulumCenterX, PendulumCenterY, x, y);	
			
			
			// drawing velocity vector
			if (showVelocity) {
				int x2 = (int) (velocity * Math.cos(angle) * PhysicsRatios.VELOCITY_SCALING) + x;
				int y2 = (int) -(velocity * Math.sin(angle) * PhysicsRatios.VELOCITY_SCALING) + y;
				g.setColor(Color.RED);
				Vector.drawVector(g, x, y, x2, y2);
			}
			
			
			
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
