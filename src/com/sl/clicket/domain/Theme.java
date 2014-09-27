package com.sl.clicket.domain;


public class Theme {

	private int backgroundColor;
	private int unfocusedButtonId;
	private int focusedButtonId;
	private int alertBackgroundId;
	
	public Theme(int backgroundColor, int unfocusedButtonId, int focusedButtonId, int alertBackgroundId) {
		super();
		this.backgroundColor = backgroundColor;
		this.unfocusedButtonId = unfocusedButtonId;
		this.focusedButtonId = focusedButtonId;
		this.alertBackgroundId = alertBackgroundId;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getUnfocusedButtonId() {
		return unfocusedButtonId;
	}

	public void setUnfocusedButtonId(int unfocusedButtonId) {
		this.unfocusedButtonId = unfocusedButtonId;
	}

	public int getFocusedButtonId() {
		return focusedButtonId;
	}

	public void setFocusedButtonId(int focusedButtonId) {
		this.focusedButtonId = focusedButtonId;
	}

	public int getAlertBackgroundId() {
		return alertBackgroundId;
	}

	public void setAlertBackgroundId(int alertBackgroundId) {
		this.alertBackgroundId = alertBackgroundId;
	}
}