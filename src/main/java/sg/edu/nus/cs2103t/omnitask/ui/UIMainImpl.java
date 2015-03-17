package sg.edu.nus.cs2103t.omnitask.ui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.model.CommandInput;
import sg.edu.nus.cs2103t.omnitask.model.Task;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class UIMainImpl implements UI {

	private static double WINDOW_WIDTH = 800;

	private static double WINDOW_HEIGHT = 600;

	private Stage primaryStage;

	private ViewController viewController;

	private static Image image = Toolkit.getDefaultToolkit().getImage(
			"src/main/resources/tray.png");

	private static TrayIcon trayIcon = new TrayIcon(image, "OmniTask");

	private Controller controller;

	public UIMainImpl(Controller controller, Stage primaryStage) {
		this.controller = controller;
		this.primaryStage = primaryStage;
	}

	@Override
	public void showMessage(String msg) {
		viewController.showMessage(msg);
	}

	@Override
	public void showError(String msg) {
		if (viewController != null) {
			viewController.showError(msg);
		} else {
			System.out.println("Error: " + msg);
		}
	}

	@Override
	public void start() {
		setupUI();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setupTray();
			}
		});

		setupHotkeys();

		primaryStage.show();

		controller.processUserInput(CommandInput.COMMAND_DISPLAY);
		showMessage("Welcome to OmniTask. Type 'help' to get help.");
	}

	private void setupUI() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"layout.fxml"));
			Parent root = (Parent) loader.load();
			viewController = (ViewController) loader.getController();
			viewController.setController(controller);

			Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);

			primaryStage.setMinWidth(WINDOW_WIDTH);
			primaryStage.setMinHeight(WINDOW_HEIGHT);
			primaryStage.setTitle("OmniTask");

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent arg0) {
					primaryStage.hide();
				}
			});

			primaryStage.setOnHidden(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					hideWindow();

					showTray();
				}

			});

			primaryStage.setOnShown(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					showWindow();

					hideTray();

					// Make sure input is focused
					viewController.focusOmniBar();
				}

			});
			
			Platform.setImplicitExit(false);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void setupHotkeys() {
		Provider provider = Provider.getCurrentProvider(false);
		provider.register(KeyStroke.getKeyStroke('O', InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK), showHideHotkeyListener);
	}

	private HotKeyListener showHideHotkeyListener = new HotKeyListener() {
		@Override
		public void onHotKey(HotKey arg0) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (primaryStage.isShowing()) {
						hideWindow();
					} else {
						showWindow();
					}
				}
			});
		}
	};

	private void setupTray() {
		if (SystemTray.isSupported()) {
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							showWindow();
						}
					});
				}
			});
			trayIcon.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							showWindow();
						}
					});
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
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (SystemTray.isSupported()) {
					SystemTray tray = SystemTray.getSystemTray();

					try {
						if (tray.getTrayIcons().length == 0) {
							tray.add(trayIcon);
							trayIcon.displayMessage(
									"OmniTask",
									"Click here or press Ctrl + Shift + O to show OmniTask",
									TrayIcon.MessageType.INFO);
						}
					} catch (AWTException e) {
						System.err.println("TrayIcon could not be added.");
					}
				}
			}
		});
	}

	private void hideTray() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (SystemTray.isSupported()) {
					SystemTray tray = SystemTray.getSystemTray();

					tray.remove(trayIcon);
				}
			}
		});
	}

	@Override
	public void exit() {
		Provider provider = Provider.getCurrentProvider(false);
		provider.reset();
		provider.stop();
		
		Platform.exit();
		System.exit(0);
	}

	private void hideWindow() {
		primaryStage.hide();
	}

	private void showWindow() {
		primaryStage.show();
	}

	@Override
	public void updateTaskListings(List<Task> tasks) {
		viewController.updateListView(tasks);
	}

}
