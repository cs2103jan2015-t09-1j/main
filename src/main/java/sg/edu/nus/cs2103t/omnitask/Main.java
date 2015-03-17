package sg.edu.nus.cs2103t.omnitask;

import javafx.application.Application;
import javafx.stage.Stage;
import sg.edu.nus.cs2103t.omnitask.controller.ControllerMainImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UI;
import sg.edu.nus.cs2103t.omnitask.ui.UIMainImpl;
import sg.edu.nus.cs2103t.omnitask.ui.UIPrototypeImpl;

public class Main extends Application {
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
		
		ControllerMainImpl controller = new ControllerMainImpl();
		
		if (forceCommandLine) {
			controller.setUi(new UIPrototypeImpl(controller));
		} else {
			controller.setUi(new UIMainImpl(controller, primaryStage));
		}
		
		controller.start(getParameters().getRaw().toArray(new String[]{}));
	}

}
