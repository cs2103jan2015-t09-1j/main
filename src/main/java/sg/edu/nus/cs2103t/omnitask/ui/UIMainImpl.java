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
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.joda.time.DateTime;

import sg.edu.nus.cs2103t.omnitask.Controller;
import sg.edu.nus.cs2103t.omnitask.Logger;
import sg.edu.nus.cs2103t.omnitask.logic.Data.DataUpdatedListener;
import sg.edu.nus.cs2103t.omnitask.logic.DataImpl;
import sg.edu.nus.cs2103t.omnitask.model.Task;
import sg.edu.nus.cs2103t.omnitask.ui.MainViewController.ViewMode;
import sg.edu.nus.cs2103t.omnitasks.command.CommandDisplayImpl;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

public class UIMainImpl extends UI {

	private static double WINDOW_HEIGHT = 680;

	private static double WINDOW_WIDTH = 960;

	private DataUpdatedListener dataUpdatedListener = new DataUpdatedListener() {

		public void dataUpdated(
				ArrayList<Task> tasks,
				javafx.collections.ListChangeListener.Change<? extends Task> changes) {
			viewController.updateAllTasks(tasks, changes);
		}

	};

	private Stage helpStage;

	private HelpViewController helpViewController;

	private Image image = Toolkit.getDefaultToolkit().getImage(
			getClass().getResource("/tray.png"));

	private Stage miniHelpStage;

	private HelpViewController miniHelpViewController;

	private Stage primaryStage;

	private HotKeyListener showHideHotkeyListener = new HotKeyListener() {
		// @Override
		public void onHotKey(HotKey arg0) {
			Platform.runLater(new Runnable() {
				// @Override
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

	private TrayIcon trayIcon = new TrayIcon(image, "OmniTask");

	private MainViewController viewController;

	public UIMainImpl(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void closeHelp() {
		helpStage.hide();
	}

	@Override
	public void closeMiniHelp() {
		miniHelpStage.hide();
	}

	// @Override
	public void exit() {
		Provider provider = Provider.getCurrentProvider(false);
		provider.reset();
		provider.stop();

		Platform.exit();
	}

	@Override
	public void redraw() {
		final double width = primaryStage.getWidth();
		primaryStage.setWidth(width - 1);

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				primaryStage.setWidth(width + 1);
			}

		});
	}

	@Override
	public void scrollDown() {
		viewController.scrollDown();
	}

	@Override
	public void scrollUp() {
		viewController.scrollUp();
	}
	
	@Override
	public void showSection(String section) {
		viewController.scrollToSection(section);
	}
	
	@Override
	public void showSection(DateTime endDate) {
		viewController.scrollToSection(endDate);
	}
	
	@Override
	public void showSection(DateTime startDate, DateTime endDate) {
		viewController.scrollToSection(startDate, endDate);
	}

	public void showAllTasks() {
		viewController.setViewMode(ViewMode.ALL);
		showMessage("Showing All Tasks");
	}

	public void showError(String msg) {
		if (viewController != null) {
			viewController.showError(msg);
		} else {
			printError("Error: " + msg);
		}
	}

	@Override
	public void showHelp(String msg) {
		helpViewController.setContent(msg);
		helpStage.show();
	}

	public void showMessage(String msg) {
		viewController.showMessage(msg);
	}

	@Override
	public void showMiniHelp(String msg) {
		miniHelpViewController.setContent(msg);
		miniHelpStage.show();
	}

	@Override
	public void showAlternateList(ViewMode viewMode, String title, ArrayList<Task> tasks) {
		viewController.setAlternateTasks(title, tasks);
		viewController.setViewMode(viewMode);
	}

	// @Override
	public void start() {
		// Subscribe to Data changes
		DataImpl.GetSingleton().addDataUpdatedListener(dataUpdatedListener);

		setupUI();

		SwingUtilities.invokeLater(new Runnable() {
			// @Override
			public void run() {
				setupTray();
			}
		});

		setupHotkeys();

		primaryStage.show();
		invokeCommandReceivedListener(CommandDisplayImpl.COMMAND_ALIASES_DISPLAY[0] + " all");
		showMessage("Welcome to OmniTask. Type 'help' to get help.");
	}

	private void hideTray() {
		SwingUtilities.invokeLater(new Runnable() {
			// @Override
			public void run() {
				if (SystemTray.isSupported()) {
					SystemTray tray = SystemTray.getSystemTray();

					tray.remove(trayIcon);
				}
			}
		});
	}

	private void hideWindow() {
		primaryStage.hide();
	}

	private void printDebug(String msg) {
		System.out.println(DateTime.now() + ": " + msg);
		Logger.writeDebug(msg);
	}

	private void printError(String msg) {
		System.err.println(DateTime.now() + ": " + msg);
		Logger.writeError(msg);
	}

	private void repositionAndResizeMiniHelpWindow() {
		miniHelpStage.setWidth(primaryStage.getWidth());
		miniHelpStage.setX(primaryStage.getX());

		Rectangle2D screen = Screen.getPrimary().getVisualBounds();
		if (primaryStage.getY() + primaryStage.getHeight()
				+ miniHelpStage.getHeight() > screen.getHeight()) {
			miniHelpStage.setY(primaryStage.getY() + primaryStage.getHeight()
					- (miniHelpStage.getHeight() + 80));
		} else {
			miniHelpStage.setY(primaryStage.getY() + primaryStage.getHeight());
		}
	}

	private void setupHelpWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"helpLayout.fxml"));
			Parent root = (Parent) loader.load();
			helpViewController = (HelpViewController) loader.getController();
			helpViewController.setUI(this);

			helpStage = new Stage();
			helpStage.initStyle(StageStyle.UNDECORATED);
			helpStage.setScene(new Scene(root, 600, 600));

			helpStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent event) {
					if (event.getCode() == KeyCode.ESCAPE) {
						closeHelp();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void setupHotkeys() {
		Provider provider = Provider.getCurrentProvider(false);
		provider.register(
				KeyStroke.getKeyStroke('O', InputEvent.SHIFT_DOWN_MASK
						| InputEvent.CTRL_DOWN_MASK), showHideHotkeyListener);
	}

	private void setupMiniHelpWindow() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"miniHelpLayout.fxml"));
			Parent root = (Parent) loader.load();
			miniHelpViewController = (HelpViewController) loader
					.getController();
			miniHelpViewController.setUI(this);

			miniHelpStage = new Stage();
			miniHelpStage.initStyle(StageStyle.UNDECORATED);
			miniHelpStage.setScene(new Scene(root, WINDOW_WIDTH, 400));
			miniHelpStage.setAlwaysOnTop(true);
			miniHelpStage.focusedProperty().addListener(
					new ChangeListener<Boolean>() {

						@Override
						public void changed(
								ObservableValue<? extends Boolean> arg0,
								Boolean oldValue, Boolean newValue) {
							if (newValue) {
								primaryStage.requestFocus();
							}
						}

					});
			miniHelpStage.setOnShown(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent event) {
					repositionAndResizeMiniHelpWindow();
				}

			});
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void setupTray() {
		if (SystemTray.isSupported()) {
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					printDebug("Tray actionPerformed event triggered.");
					Platform.runLater(new Runnable() {
						// @Override
						public void run() {
							showWindow();
						}
					});
				}
			});
			trayIcon.addMouseListener(new MouseListener() {

				// @Override
				public void mouseClicked(MouseEvent e) {
					printDebug("Tray mouseClicked event triggered.");
					Platform.runLater(new Runnable() {
						// @Override
						public void run() {
							showWindow();
						}
					});
				}

				// @Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				// @Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				// @Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				// @Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});

			printDebug("Tray Setup Complete.");
		}
	}

	private void setupUI() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"mainLayout.fxml"));
			Parent root = (Parent) loader.load();
			viewController = (MainViewController) loader.getController();
			viewController.setUI(this);

			Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
			scene.getStylesheets().add(
					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);

			primaryStage.setMinWidth(WINDOW_WIDTH);
			primaryStage.setMinHeight(WINDOW_HEIGHT);
			primaryStage.setTitle("OmniTask");
			primaryStage.getIcons().add(
					new javafx.scene.image.Image("tray.png"));

			primaryStage.iconifiedProperty().addListener(
					new ChangeListener<Boolean>() {

						public void changed(
								ObservableValue<? extends Boolean> prop,
								Boolean oldValue, Boolean newValue) {
							// newValue is true if window is minimized
							if (newValue) {
								primaryStage.hide();
							}
						}

					});

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				// @Override
				public void handle(WindowEvent arg0) {
					Controller.Exit();
				}
			});

			primaryStage.setOnHidden(new EventHandler<WindowEvent>() {

				// @Override
				public void handle(WindowEvent event) {
					printDebug("OmniTask Window Hidden");

					hideWindow();

					showTray();
				}

			});

			primaryStage.setOnShown(new EventHandler<WindowEvent>() {

				// @Override
				public void handle(WindowEvent event) {
					printDebug("OmniTask Window Shown");

					showWindow();

					hideTray();

					// Make sure input is focused
					viewController.focusOmniBar();
				}

			});

			ChangeListener<Number> resizeAndMoveChangeListener = new ChangeListener<Number>() {

				@Override
				public void changed(
						ObservableValue<? extends Number> observable,
						Number oldValue, Number newValue) {
					repositionAndResizeMiniHelpWindow();
				}

			};

			primaryStage.widthProperty().addListener(
					resizeAndMoveChangeListener);
			primaryStage.heightProperty().addListener(
					resizeAndMoveChangeListener);
			primaryStage.xProperty().addListener(resizeAndMoveChangeListener);
			primaryStage.yProperty().addListener(resizeAndMoveChangeListener);

			setupHelpWindow();
			setupMiniHelpWindow();

			primaryStage.getScene().setOnKeyPressed(
					new EventHandler<KeyEvent>() {
						public void handle(KeyEvent event) {
							if (event.getCode() == KeyCode.ESCAPE) {
								closeHelp();
								closeMiniHelp();
							}
						}
					});

			Platform.setImplicitExit(false);

			printDebug("UI Setup Complete.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void showTray() {
		SwingUtilities.invokeLater(new Runnable() {
			// @Override
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
						printError("TrayIcon could not be added.");
					}
				}
			}
		});
	}

	private void showWindow() {
		primaryStage.show();
		primaryStage.setIconified(false);
	}

}
