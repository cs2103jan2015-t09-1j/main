package sg.edu.nus.cs2103t.omnitask;

import javafx.application.Application;
import javafx.stage.Stage;
import sg.edu.nus.cs2103t.omnitask.controller.Controller;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UIMainImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UIPrototypeImpl;

public class Main extends Application {
	
	private static UI ui;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		boolean forceCommandLine = false;
		for (String arg : getParameters().getRaw()) {
			if (arg.toLowerCase().equals("-no-ui")) {
				forceCommandLine = true;
			}
		}
		
		Controller controller = Controller.GetSingleton();
		controller.start(getParameters().getRaw().toArray(new String[]{}));
		
		if (forceCommandLine) {
			ui = new UIPrototypeImpl();
		} else {
			ui = new UIMainImpl(primaryStage);
		}
		
		controller.setUi(ui);
		ui.start();
	}

	public static void Exit() {
		ui.exit();
		System.exit(0);
	}
}
