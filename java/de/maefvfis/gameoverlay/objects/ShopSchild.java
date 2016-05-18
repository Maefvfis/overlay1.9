package de.maefvfis.gameoverlay.objects;

public class ShopSchild {

	public static int INDShopInhaber = 0;
	public static int INDAnzahl = 1;
	public static int INDAnkauf = 2;
	public static int INDVerkauf = 3;
	public static int INDItem = 4;
	public static int INDX = 5;
	public static int INDY = 6;
	public static int INDZ = 7;
	public static int INDWarp = 8;
	public static int INDServer = 9;
	
	private String ShopInhaber;
	private int Anzahl;
	private float Ankauf;
	private float Verkauf;
	private String Item;
	private String Warp;
	private String Server;
	private int X;
	private int Y;
	private int Z;

	public ShopSchild(String ShopInhaber, int Anzahl, float Ankauf, float Verkauf, String Item, int X, int Y, int Z, String Warp, String Server) {
		super();
		this.ShopInhaber = ShopInhaber;
		this.Anzahl = Anzahl;
		this.Ankauf = Ankauf;
		this.Verkauf = Verkauf;
		this.Item = Item;
		this.Warp = Warp;
		this.Server = Server;
		this.X = X;
		this.Y = Y;
		this.Z = Z;
	}
	
	

	
	
	public boolean isequal(ShopSchild schild) {
		
		if(this.getX() != schild.getX() || this.getY() != schild.getY() || this.getZ() != schild.getZ()) 
			return false;
		
		
		return true;
	}
	

	public String getWarp() {
		return Warp;
	}

	public void setWarp(String Warp) {
		this.Warp = Warp;
	}
	
	public String getServer() {
		return Server;
	}

	public void setServer(String Server) {
		this.Server = Server;
	}
	
	
	
	public String getShopInhaber() {
		return ShopInhaber;
	}

	public void setShopInhaber(String ShopInhaber) {
		this.ShopInhaber = ShopInhaber;
	}
	
	public String getItem() {
		return Item;
	}

	public void setItem(String Item) {
		this.Item = Item;
	}
	
	public int getAnzahl() {
		return Anzahl;
	}

	public void setAnzahl(int Anzahl) {
		this.Anzahl = Anzahl;
	}
	
	public int getX() {
		return X;
	}

	public void setX(int X) {
		this.X = X;
	}
	
	public int getY() {
		return Y;
	}

	public void setY(int Y) {
		this.Y = Y;
	}
	
	public int getZ() {
		return Z;
	}

	public void setZ(int Z) {
		this.Z = Z;
	}
	
	public float getAnkauf() {
		return Ankauf;
	}

	public void setAnkauf(float Ankauf) {
		this.Ankauf = Ankauf;
	}
	
	public float getVerkauf() {
		return Verkauf;
	}

	public void setVerkauf(float Verkauf) {
		this.Verkauf = Verkauf;
	}
	
	@Override
	public String toString() {
		return "SchopSchild [ShopInhaber=" + ShopInhaber + ", Anzahl=" + Anzahl
				+ ", Ankauf=" + Ankauf + ", Verkauf=" + Verkauf + ", Item="
				+ Item + "]";
	}
	
}
