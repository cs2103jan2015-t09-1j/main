package sg.edu.nus.cs2103t.omnitask.ui;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.model.Task;

public class UIMainImpl extends JFrame implements UI {
	
	private Controller controller;
	
	private JTextArea outputField;
	
	private JTextField inputField;
	
	public UIMainImpl(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void showMessage(String msg) {
		outputField.setText(outputField.getText() + msg + "\n");
	}

	@Override
	public void showError(String msg) {
		outputField.setText(outputField.getText() + "Error: " + msg + "\n");
	}

	@Override
	public void start() {
		setupUI();
        
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIMainImpl.this.setVisible(true);
            }
        });
		
		showMessage("Welcome to OmniTask.");
	}
	
	private void setupUI() {
		setTitle("OmniTask");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        outputField = new JTextArea();
        outputField.setEditable(false);
        
        inputField = new JTextField();
        inputField.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Process user input by passing it to controller
				if (!inputField.getText().trim().equals("")) {
					controller.processUserInput(inputField.getText());
					inputField.setText("");
				}
			}
        	
        });
        
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addComponent(outputField)
                .addComponent(inputField)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
        		.addComponent(outputField, 400, 400, 1000)
        		.addGap(10)
                .addComponent(inputField, 40, 40, 40)
        );
    }

	@Override
	public void exit() {

	}

	@Override
	public void updateTaskListings(List<Task> tasks) {
		// TODO: Show UI in table form
		showMessage("List of tasks: ");
		
		for (Task task : tasks) {
			showMessage(task.getId() + " - " + task.getName());
		}
	}

}
