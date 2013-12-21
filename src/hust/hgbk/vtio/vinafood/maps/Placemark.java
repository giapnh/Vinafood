package hust.hgbk.vtio.vinafood.maps;


public class Placemark {

	String title;
	String description;
	String coordinates;
	String address;

	public String getTitle() {
	    return title;
	}
	public void setTitle(String title) {
	    this.title = title;
	}
	public String getDescription() {
	    return description;
	}
	public void setDescription(String description) {
	    this.description = description;
	}
	public String getCoordinates() {
	    return coordinates;
	}
	public void setCoordinates(String coordinates) {
	    this.coordinates = coordinates;
	}
	public String getAddress() {
	    return address;
	}
	public void setAddress(String address) {
	    this.address = address;
	}
	public String getDistance(){
		int endPos = description.indexOf("<br/>");
		return description.substring(0, endPos).replace("Distance:", "-");
	}

	}