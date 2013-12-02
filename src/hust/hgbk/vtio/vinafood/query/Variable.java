package hust.hgbk.vtio.vinafood.query;

public class Variable {
	String label;
	String name;
	String classURI;
	boolean showInResult = false;
	public Variable() {
		// TODO Auto-generated constructor stub
	}
	
	public Variable(String label, String name, String classURI,
			boolean showInResult) {
		this.label = label;
		this.name = name;
		this.classURI = classURI;
		this.showInResult = showInResult;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassURI() {
		return classURI;
	}
	public void setClassURI(String classURI) {
		this.classURI = classURI;
	}
	public boolean isShowInResult() {
		return showInResult;
	}
	public void setShowInResult(boolean showInResult) {
		this.showInResult = showInResult;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
}
