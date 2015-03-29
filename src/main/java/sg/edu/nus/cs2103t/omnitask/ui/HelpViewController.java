package sg.edu.nus.cs2103t.omnitask.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

public class HelpViewController {
	
	private UI ui;
	
	@FXML private WebView webView;
	
	public HelpViewController() {
	}
	
	@FXML
    protected void initialize() {
	}

	public void setUI(UI ui) {
		this.ui = ui;
	}
	
	public void setContent(String text) {
		webView.getEngine().loadContent(text);
	}
	
	@FXML protected void onDoneClicked(ActionEvent event) {
		ui.closeHelp();
    }

}
