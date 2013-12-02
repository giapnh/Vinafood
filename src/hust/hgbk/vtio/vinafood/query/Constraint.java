package hust.hgbk.vtio.vinafood.query;

import hust.hgbk.vtio.vinafood.ontology.simple.PropertyDataSimple;

public class Constraint {
	String subjectName;
	PropertyDataSimple predicate;
	String value;
	
	boolean isOptional = false;
	
	/**
	 * Get constrain string
	 * @return
	 */
	public String getConstrainsAsString(){
		String returnConstrain = "";
		if ((subjectName == null || subjectName.equals("") || predicate == null) && value != null){
			returnConstrain = value;
		}
		try {
			returnConstrain = "?"+subjectName + " <"+predicate.getUrl()+"> "+value+".";
			if (isOptional){
				returnConstrain = "\n OPTIONAL { "+returnConstrain+" } \n";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		return returnConstrain;
	}
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public PropertyDataSimple getPredicate() {
		return predicate;
	}
	public void setPredicate(PropertyDataSimple predicate) {
		this.predicate = predicate;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isOptional() {
		return isOptional;
	}
	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}
}
