package hust.hgbk.vtio.vinafood.query;

import android.util.Log;
import hust.hgbk.vtio.vinafood.config.ServerConfig;

import java.util.ArrayList;

public class Query {
	ArrayList<Variable> arrayVariable= new ArrayList<Variable>();
	ArrayList<Constraint> arrayConstraint = new ArrayList<Constraint>();
	ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
	
	public ArrayList<Variable> getArrayVariable() {
		return arrayVariable;
	}
	public void setArrayVariable(ArrayList<Variable> arrayVariable) {
		this.arrayVariable = arrayVariable;
	}
	public ArrayList<Constraint> getArrayConstraint() {
		return arrayConstraint;
	}
	public void setArrayConstraint(ArrayList<Constraint> arrayConstraint) {
		this.arrayConstraint = arrayConstraint;
	}
	public ArrayList<ArrayList<String>> getResult() {
		return result;
	}
	public void setResult(ArrayList<ArrayList<String>> result) {
		this.result = result;
	}
	
	public String getQueryString(){
		StringBuffer queryString = new StringBuffer("");
		// modified by dungct
		StringBuffer optionalQueryString = new StringBuffer("");
		queryString.append("SELECT DISTINCT ");
		for (int i=0; i<arrayVariable.size(); i++){
			if (arrayVariable.get(i).showInResult){
				queryString.append(" ?"+arrayVariable.get(i).getName()+" ");
			}
		}
		queryString.append("\n{ \n");
		for (int i=0; i<arrayVariable.size(); i++){
			Variable var = arrayVariable.get(i);
			if (!var.getClassURI().equals("xsd:string") && !var.getClassURI().equals("xsd:number")){
				// Neu la bien literal thi ko chen triple rdf:type vao
				queryString.append("?"+var.getName()+" rdf:type <"+var.getClassURI()+">.\n");
			}
			
		}
		// modified by dungct 29-04-2012
		// day cac constraint co OPTIONAL xuong cuoi cung de tang toc do truy van
		for (int i=0; i<arrayConstraint.size(); i++){
			Constraint constraint = arrayConstraint.get(i);
			if (constraint.isOptional()== false)
				queryString.append("\n "+constraint.getConstrainsAsString()+" \n");
			else 
				optionalQueryString.append("\n "+constraint.getConstrainsAsString()+" \n");	
		}
		
		
		queryString.append(optionalQueryString);
		queryString.append("}");
		Log.d("GEN QUERY ", "QUERY STRING: "+queryString.toString());
		return queryString.toString();
	}
	public String getQueryStringWithCity(){
		StringBuffer queryString = new StringBuffer("");
		// modified by dungct
		StringBuffer optionalQueryString = new StringBuffer("");
		queryString.append("SELECT DISTINCT ");
		for (int i=0; i<arrayVariable.size(); i++){
			if (arrayVariable.get(i).showInResult){
				queryString.append(" ?"+arrayVariable.get(i).getName()+" ");
			}
		}
		queryString.append("\n{ \n");
		for (int i=0; i<arrayVariable.size(); i++){
			Variable var = arrayVariable.get(i);
			if (!var.getClassURI().equals("xsd:string") && !var.getClassURI().equals("xsd:number")){
				// Neu la bien literal thi ko chen triple rdf:type vao
				queryString.append("?"+var.getName()+" rdf:type <"+var.getClassURI()+">.\n");
			}
			
		}
		
		// modified by dungct 29-04-2012
		// day cac constraint co OPTIONAL xuong cuoi cung de tang toc do truy van
		for (int i=0; i<arrayConstraint.size(); i++){
			Constraint constraint = arrayConstraint.get(i);
			if (constraint.isOptional()== false)
				queryString.append("\n "+constraint.getConstrainsAsString()+" \n");
			else 
				optionalQueryString.append("\n "+constraint.getConstrainsAsString()+" \n");	
		}
				
		if (arrayVariable.size() > 0){
			queryString.append("?" + arrayVariable.get(0).getName() +" vtio:hasLocation ?add_0 .   ?add_0 vtio:isPartOf <" + ServerConfig.currentCityUri + ">.");
		}
		// Them OPTIONAL luon vao cuoi cung.
		queryString.append(optionalQueryString);
		queryString.append("}");
		Log.d("GEN QUERY", "QUERY STRING:"+queryString.toString());
		return queryString.toString();
	}
}
