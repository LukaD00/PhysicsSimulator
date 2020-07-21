package guiClasses;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import constants.GUI;

/**
 * InputLine is a helper class that wraps up a custom label and text field
 * together for the south (input) panel of all simulations.
 * 
 */
@SuppressWarnings("serial")
public class InputLine extends JPanel {
	
	private JLabel label;
	private JTextField textField;
	
	/**
	 * By default, errorLabel is empty. It is used to show any errors with input, if needed.
	 * Should be controlled from the outside with setErrorLabel method.
	 */
	private JLabel errorLabel; 
	
	/**
	 * 
	 * Initializes a new InputLine:
	 * RIGID - LABEL - RIGID - TEXTFIELD - RIGID - ERRORLABEL - RIGID
	 * 
	 * @param labelText - label's text
	 * @param defaultValue - first shown value in the text field
	 */
	public InputLine(String labelText, double defaultValue) {
		label = new JLabel(labelText);
		textField = new JTextField(Double.toString(defaultValue));
		errorLabel = new JLabel();
		
		label.setPreferredSize(GUI.PREFFERED_SIZE);
		label.setMaximumSize(GUI.MAXIMUM_SIZE);
		textField.setPreferredSize(GUI.PREFFERED_SIZE);
		textField.setMaximumSize(GUI.MAXIMUM_SIZE);
		errorLabel.setPreferredSize(GUI.PREFFERED_SIZE);
		errorLabel.setMaximumSize(GUI.MAXIMUM_SIZE);
		errorLabel.setForeground(Color.RED);
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(label); 
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(textField);
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(errorLabel);
		this.add(Box.createRigidArea(GUI.RIGID));
	}

	/**
	 * Sets the error label, next to the text field.
	 * 
	 * @param text text to be set as error label, in red
	 */
	public void setErrorLabel(String text) {
		this.errorLabel.setText(text);;
	}
	
	/**
	 * Returns the value inside the text field as a double.
	 * 
	 * @return Text field's label.
	 * @throws NullPointerException if text field is empty
	 * @throws NumberFormatException if content of text field is not a number
	 */
	public double getValue() throws NullPointerException, NumberFormatException {
		return Double.parseDouble(textField.getText());
	}
}
