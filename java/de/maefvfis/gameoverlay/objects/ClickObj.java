package de.maefvfis.gameoverlay.objects;

public class ClickObj {
	private int key;
	private int[] action;
	public ClickObj(int sKey, int[] sAction) {
		this.key = sKey;
		this.action = sAction;
	}
	public int getKey() {
		return this.key;
	}
	public int[] getAction() {
		return this.action;
	}
}