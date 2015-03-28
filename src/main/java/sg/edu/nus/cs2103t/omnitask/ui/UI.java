package sg.edu.nus.cs2103t.omnitask.ui;


public abstract class UI {
	
	protected CommandReceivedListener commandReceivedListener;
	
	public static interface CommandReceivedListener {
		void onCommandReceived(String userInput);
	}

	public void setCommandReceivedListener(CommandReceivedListener commandReceivedListener) {
		this.commandReceivedListener = commandReceivedListener;
	}
	
	protected void invokeCommandReceivedListener(String userInput) {
		if (commandReceivedListener != null) {
			commandReceivedListener.onCommandReceived(userInput);
		}
	}
	
	public abstract void showError(String msg);

	public abstract void showMessage(String msg);

	public abstract void start();
	
	public abstract void exit();
	
}
