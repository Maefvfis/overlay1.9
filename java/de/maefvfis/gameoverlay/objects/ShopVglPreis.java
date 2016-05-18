package de.maefvfis.gameoverlay.objects;

public class ShopVglPreis {	
	public String item;
	public float VKpreis;
	public float AKpreis;
	
	ShopVglPreis(String item, float VKpreis, float AKpreis) {
		this.item = item;
		this.VKpreis = VKpreis;
		this.AKpreis = AKpreis;
	}
	@Override
    public boolean equals(Object object) {

        if (object != null && object instanceof String) {
                return item.equalsIgnoreCase((String) object);
        }
        return false;
    }
	
	
}