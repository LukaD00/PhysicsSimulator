package sims;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;


import constants.Physics;
import constants.PhysicsRatios;


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
		// pendulum setup 
		Pendulum pendulum = new Pendulum();
		this.add(pendulum, BorderLayout.CENTER);
		
		
		// SOUTH PANEL
		// initializing all components
		JLabel lengthLabel = new JLabel("Length (m): ");
		JTextField lengthField = new JTextField(Double.toString(DEFAULT_LENGTH));
		JLabel angleLabel = new JLabel("Starting angle (deg): ");
		JTextField angleField = new JTextField(Double.toString(Math.toDegrees(DEFAULT_START_ANGLE)));
		JButton startButton = new JButton("Start");
		JButton resumeButton = new JButton("Resume");
		JButton pauseButton = new JButton("Pause");
		ArrayList<Component> southPanelComponents = new ArrayList<Component>(); // easier to set preffered and maximum dimension
		southPanelComponents.add(lengthLabel);									// when all the components are in the same collection
		southPanelComponents.add(lengthField);
		southPanelComponents.add(angleLabel);
		southPanelComponents.add(angleField);
		southPanelComponents.add(startButton);
		southPanelComponents.add(resumeButton);
		southPanelComponents.add(pauseButton);
		
		// setting up layout
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.PAGE_AXIS));
		
		final Dimension RIGID = new Dimension(10,20); // dimension of spaces between and to the side of all components
		
		JPanel line1 = new JPanel(); 
		line1.setLayout(new BoxLayout(line1, BoxLayout.LINE_AXIS));
		line1.add(Box.createRigidArea(RIGID));
		line1.add(lengthLabel); 
		line1.add(Box.createRigidArea(RIGID));
		line1.add(lengthField);
		line1.setAlignmentX(LEFT_ALIGNMENT);
		southPanel.add(line1);
		
		JPanel line2 = new JPanel(); 
		line2.setLayout(new BoxLayout(line2, BoxLayout.LINE_AXIS));
		line2.add(Box.createRigidArea(RIGID));
		line2.add(angleLabel); 
		line2.add(Box.createRigidArea(RIGID));
		line2.add(angleField);
		line2.setAlignmentX(LEFT_ALIGNMENT);
		southPanel.add(line2);
		
		JPanel line3 = new JPanel(); 
		line3.setLayout(new BoxLayout(line3, BoxLayout.LINE_AXIS));
		line3.add(Box.createRigidArea(RIGID));
		line3.add(startButton); 
		line3.add(Box.createRigidArea(RIGID));
		line3.add(resumeButton);
		line3.add(Box.createRigidArea(RIGID));
		line3.add(pauseButton);
		line3.setAlignmentX(LEFT_ALIGNMENT);
		southPanel.add(line3);
		
		final Dimension PREFFERED_SIZE = new Dimension(150, 20);
		final Dimension MAXIMUM_SIZE = new Dimension(150,20);
		for (Component c : southPanelComponents) {
			c.setPreferredSize(PREFFERED_SIZE);
			c.setMaximumSize(MAXIMUM_SIZE);
		}
		
		this.add(southPanel, BorderLayout.SOUTH);
		
		// action listeners for buttons
		startButton.addActionListener(e -> {
			try {
				velocity = START_VELOCITY;
				length = Double.parseDouble(lengthField.getText());
				angle = Math.toRadians(Double.parseDouble(angleField.getText()));
				pendulum.start();
			} catch (Exception ex) {
				// TODO write read error exceptions? 
				// right now nothing happens with invalid inputs, could display an error message somewhere?
			}
		});
		resumeButton.addActionListener(e -> {
			pendulum.resume();
		});
		pauseButton.addActionListener(e -> {
			pendulum.pause();
		});
		
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

		@Override
		public void actionPerformed(ActionEvent e) {
			this.repaint();
		}
		
	}
	
}
