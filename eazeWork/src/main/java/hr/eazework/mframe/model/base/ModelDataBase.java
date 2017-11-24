package hr.eazework.mframe.model.base;

public class ModelDataBase {
	protected String validateString(String value){
		if(value!=null)
			return value;
		else
			return "";
	}
}
