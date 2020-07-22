package guiClasses;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import constants.GUI;

@SuppressWarnings("serial")
public class CheckBoxLine extends JPanel {
	
	private JCheckBox check;
	
	private List<ActionListener> listeners;
	
	public CheckBoxLine(String label) {
		listeners = new ArrayList<ActionListener>();
		
		check = new JCheckBox(label);
		
		check.setMaximumSize(GUI.MAXIMUM_SIZE);
		check.setPreferredSize(GUI.PREFFERED_SIZE);
		
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setAlignmentX(LEFT_ALIGNMENT);
		this.add(Box.createRigidArea(GUI.RIGID));
		this.add(check);
		
		check.addActionListener(e -> {
			for (ActionListener listener : listeners) {
				listener.actionPerformed(e);
			}
		});
	}
	
	public void addActionListener(ActionListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public boolean isSelected() {
		return check.isSelected();
	}
}
