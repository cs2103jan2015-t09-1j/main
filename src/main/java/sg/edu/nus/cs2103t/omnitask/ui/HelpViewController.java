package sg.edu.nus.cs2103t.omnitask.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.web.WebView;

//@author A0111795A
public class HelpViewController {

	private Ui ui;

	@FXML
	private WebView webView;

	public HelpViewController() {
	}

	public void setContent(String text) {
		//webView.setZoom(javafx.stage.Screen.getPrimary().getDpi() / 96);
		webView.getEngine().loadContent(text);
	}

	public void setUI(Ui ui) {
		this.ui = ui;
	}

	@FXML
	protected void initialize() {
	}

	@FXML
	protected void onDoneClicked(ActionEvent event) {
		ui.closeHelp();
	}

}
