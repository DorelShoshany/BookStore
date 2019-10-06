package smartspace.layout;

public class Latlng {
	private double lat;
	private double lng;
	
	public Latlng(double lat, double lng) {
		this.setLat(lat);
		this.setLng(lng);
	}

	public Latlng() {
	}
	
	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
}
