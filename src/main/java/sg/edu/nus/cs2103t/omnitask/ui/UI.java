package sg.edu.nus.cs2103t.omnitask.ui;

public interface UI {
	public void showMessage(String msg);
	
	public void showError(String msg);
	
	public void start();
	
	public void exit();
}
