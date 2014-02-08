package hust.hgbk.vtio.vinafood.entities;

public class Topic {

	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================
//	public int type = 0;
	public String imgLink = "";
	public String title = "";
	public String description = "";
	public String content = "";

	// ===========================================================
	// Constructors
	// ===========================================================
	public Topic() {
	}

	public Topic(String imgLink, String title, String description,
			String content) {
		this.imgLink = imgLink;
		this.title = title;
		this.description = description;
		this.content = content;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
