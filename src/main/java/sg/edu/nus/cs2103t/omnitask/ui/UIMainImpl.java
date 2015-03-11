package sg.edu.nus.cs2103t.omnitask.ui;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.model.Task;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

@SuppressWarnings("serial")
public class UIMainImpl extends JFrame implements UI {
	
	private static Image image = Toolkit.getDefaultToolkit().getImage("tray.png");

	private static TrayIcon trayIcon = new TrayIcon(image, "OmniTask");
	
	private Controller controller;
	
	private JTable taskTable;
	
	private JTextField outputField;
	
	private JTextField inputField;
	
	private ArrayList<Task> tasks;
	
	public UIMainImpl(Controller controller) {
		this.controller = controller;
		tasks = new ArrayList<Task>();
	}

	@Override
	public void showMessage(String msg) {
		outputField.setText(msg);
	}

	@Override
	public void showError(String msg) {
		outputField.setText("Error: " + msg);
	}

	@Override
	public void start() {
		setupUI();
		
		setupTray();
		
		setupHotkeys();
        
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                showWindow();
            }
        });
		
		controller.processUserInput("display");
		showMessage("Welcome to OmniTask. Type 'help' to get help.");
	}
	
	private void setupHotkeys() {
		Provider provider = Provider.getCurrentProvider(false);
		provider.register(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.META_DOWN_MASK), showHideHotkeyListener);
	}
	
	private HotKeyListener showHideHotkeyListener = new HotKeyListener() {
		@Override
		public void onHotKey(HotKey arg0) {
			if (isShowing()) {
				hideWindow();
			} else {
				showWindow();
			}
		}
	};
	
	private void setupTray() {
		if (SystemTray.isSupported()) {
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showWindow();
				}
			});
			trayIcon.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					showWindow();
					showMessage("To exit OmniTask, type \"exit\" below.");
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}
	}
	
	private void showTray() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();

			try {
				if (tray.getTrayIcons().length == 0) {
					tray.add(trayIcon);
					trayIcon.displayMessage("OmniTask", "Click here or press Win + Shift + O to show OmniTask", TrayIcon.MessageType.INFO);
				}
			} catch (AWTException e) {
				System.err.println("TrayIcon could not be added.");
			}
		}
	}
	
	private void hideTray() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			
			tray.remove(trayIcon);
		}
	}
	
	private void setupUI() {
		setTitle("OmniTask");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        addComponentListener(new ComponentAdapter() {
        	public void componentHidden(ComponentEvent e) {
        		hideWindow();
        		
        		showTray();
        	}
        	
        	public void componentShown(ComponentEvent e) {
        		showWindow();
        		
        		hideTray();
        		
        		// Make sure input is focused
        		inputField.grabFocus();
        	}
        });
        
        taskTable = new JTable(new TaskTableModel());
        
        // TODO: Make code more decoupled?
        TableColumn column = null;
        for (int i = 0; i < 4; i++) {
            column = taskTable.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(15);
            } else if (i == 1) {
                column.setPreferredWidth(15);
            } else if (i == 3) {
                column.setPreferredWidth(200);
            } else {
                column.setPreferredWidth(400);
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(taskTable);
        taskTable.setFillsViewportHeight(true);
        
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
        
        outputField = new JTextField();
        outputField.setEditable(false);
        
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createParallelGroup()
                .addComponent(scrollPane)
                .addComponent(inputField)
                .addComponent(outputField)
        );

        gl.setVerticalGroup(gl.createSequentialGroup()
        		.addComponent(scrollPane, 400, 400, 1000)
        		.addGap(10)
        		.addComponent(outputField, 40, 40, 40)
                .addComponent(inputField, 40, 40, 40)
        );
    }

	@Override
	public void exit() {
		System.exit(0);
	}

	private void hideWindow() {
		if (isVisible()) {
			setVisible(false);
		}
	}
	
	private void showWindow() {
		if (!isVisible()) {
			setVisible(true);
		}
	}
	
	@Override
	public void updateTaskListings(List<Task> tasks) {
		this.tasks.clear();
		this.tasks.addAll(tasks);
		taskTable.updateUI();
	}
	
	private class TaskTableModel extends AbstractTableModel {
		// TODO: Decouple this line somehow?
		private String[] columnNames = new String[]{"id", "priority", "name", "deadline"};

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		
		public String getColumnName(int col) {
	        return columnNames[col];
	    }

		@Override
		public int getRowCount() {
			return tasks.size();
		}

		@Override
		public Object getValueAt(int row, int col) {
			if (col == 0) {
				return tasks.get(row).getId();
			} else if (col == 1) {
				return tasks.get(row).getPriority()+1;
			} else if (col == 2) {
				return tasks.get(row).getName();
			} else if (col == 3) {
				if (tasks.get(row).getEndDate() != null) {
					return tasks.get(row).getEndDate().toString();
				} else {
					return "";
				}
			} else {
				new Exception("Not implemented.").printStackTrace();
				return null;
			}
		}
		
	}

}
