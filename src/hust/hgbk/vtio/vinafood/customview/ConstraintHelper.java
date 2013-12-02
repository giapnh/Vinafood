package hust.hgbk.vtio.vinafood.customview;

import hust.hgbk.vtio.vinafood.config.ServerConfig;
import hust.hgbk.vtio.vinafood.constant.LanguageCode;
import hust.hgbk.vtio.vinafood.query.Constraint;

public class ConstraintHelper {

	public static final int HAS_NAME = 0;
	public static final int HAS_LOCATION = 1;
	public static final int NEAR_BY = 2;
	public static final int WEL_KNOWN = 3;
	public static final int HAS_CUISINE_STYLE = 4;
	public static final int HAS_RATING = 5;

	public static Constraint getConstraint(int constrainType, String keyWord) {
		Constraint constrain = new Constraint();
		switch (constrainType) {
		case HAS_NAME:
			if (keyWord.length() > 0) {
				String s = "";
				s = s
						+ "?search fti:match '"
						+ keyWord
						+ "'. "
						+ "?search <http://www.w3.org/2000/01/rdf-schema#label>  ?str. ";

				s = s + " FILTER(regex(STR(?str), " + "\"" + keyWord
						+ "\" , \"i\" )) ";
				constrain.setValue(s);
			} else {
				constrain.setValue("");
			}
			break;
		case HAS_LOCATION:
			if (keyWord.length() > 0) {
				String s = "{"
						+ "{"
						+ "?search <http://hust.se.vtio.owl#hasLocation> ?add_0 . "
						+ "?add_0 fti:match '"
						+ keyWord
						+ "'. "
						+ "?add_0 vtio:isPartOf <http://hust.se.vtio.owl#hanoi-city>."
						+ "}"
						+ "UNION"
						+ "{"
						+ "?search <http://hust.se.vtio.owl#hasLocation> ?add_0 . "
						+ "?add_0 <http://hust.se.vtio.owl#isPartOf> ?add_1."
						+ "?add_1 fti:match '" + keyWord + "'. " + "}}";
				constrain.setValue(s);
			} else {
				constrain.setValue("");
			}
			break;
		case NEAR_BY:
			if (keyWord.length() > 0) {
				String s = "";
				if (keyWord.toLowerCase().equals("atm")) {
					s = "{" + "?search vtio:nearBy ?place1. "
							+ "?place1 rdf:type vtio:ATM." + "}";
				} else
					s = "{" + "{" + "?search vtio:nearBy ?place1."
							+ "?place1 fti:match '"
							+ keyWord
							+ "'. "
							+ "}"
							+ "UNION {"
							+ "?search vtio:nearBy ?place1. "
							+ "?place1 vtio:hasLocation ?loc1."
							+ "{{?loc1 fti:match '"
							+ keyWord
							+ "'.}"
							+ "UNION {?loc1 vtio:isPartOf ?loc2. ?loc2 fti:match '"
							+ keyWord
							+ "'.   }"
							+ "}"
							+ "}"
							+ "UNION {"
							+ "?search vtio:nearBy ?place1. "
							+ "?place1 rdf:type ?class."
							+ "?class fti:match '"
							+ keyWord + "'. " + "}" + "}";
				constrain.setValue(s);
			} else {
				constrain.setValue("");
			}
			break;
		case WEL_KNOWN:
			constrain
					.setValue("?search <http://hust.se.vtio.owl#isWellKnown> true . ");
			break;
		case HAS_CUISINE_STYLE:
			if (keyWord.length() > 0) {
				String s = "?search vtio:hasCuisineStyle ?cuisine. {"
						+ " ?cuisine rdf:type ?class. "
						+ " ?class rdfs:subClassOf vtio:Cuisine-Style. "
						+ " ?class fti:match '" + keyWord + "'. } ";

				constrain.setValue(s);
			} else {
				constrain.setValue("");
			}
			break;
		case HAS_RATING:
			String ratingNumber = "";
			switch (Integer.parseInt(keyWord)) {
			case 1:
				if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
					ratingNumber = "One";
				else if (ServerConfig.LANGUAGE_CODE
						.equals(LanguageCode.VIETNAMESE))
					ratingNumber = "một";
				break;
			case 2:
				if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
					ratingNumber = "Two";
				else if (ServerConfig.LANGUAGE_CODE
						.equals(LanguageCode.VIETNAMESE))
					ratingNumber = "Hai";
				break;
			case 3:
				if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
					ratingNumber = "Three";
				else if (ServerConfig.LANGUAGE_CODE
						.equals(LanguageCode.VIETNAMESE))
					ratingNumber = "Ba";
				break;
			case 4:
				if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
					ratingNumber = "Four";
				else if (ServerConfig.LANGUAGE_CODE
						.equals(LanguageCode.VIETNAMESE))
					ratingNumber = "Bốn";
				break;
			case 5:
				if (ServerConfig.LANGUAGE_CODE.equals(LanguageCode.ENGLISH))
					ratingNumber = "Five";
				else if (ServerConfig.LANGUAGE_CODE
						.equals(LanguageCode.VIETNAMESE))
					ratingNumber = "Năm";
				break;
			}

			if (ratingNumber == "") {
				constrain.setValue("");
			} else {
				String s = "?search vtio:hasRating ?rating. ?rating rdf:type vtio:Rating. "
						+ "?rating rdfs:label ?labelrating. "
						+ " ?rating fti:match '"
						+ ratingNumber
						+ "'. "
						+ " FILTER(regex(STR(?labelrating), \""
						+ ratingNumber
						+ "\", \"i\"))  ";
				constrain.setValue(s);
			}
			break;
		}

		return constrain;
	}

}
