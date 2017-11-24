package hr.eazework.selfcare.communication;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class AppRequestString {
	String XMLReq = null;
	String TAG = AppRequestString.class.getName();
	final String XML_VERSION = "<?xml version='1.0' encoding='UTF-8'?>";
	final String TAG_APP_DATA_START = "<mAppData>";
	final String TAG_APP_DATA_END = "</mAppData>";
	final String TAG_USER_APP_DATA_START = "<userOperationData>";
	final String TAG_USER_APP_DATA_END = "</userOperationData>";
	private static AppRequestString appRequestString;
	
	public static AppRequestString getInstance(){
		if(appRequestString==null){
			appRequestString = new AppRequestString();
		}
		return appRequestString;
	}
	
	private AppRequestString(){
		
	}


//	public String getRequestXMLForLogin(String UserName, String Password) {
//		String xml = "";
//		xml = "<?xml version='1.0' encoding='UTF-8'?><mAppData><userOperationData><userId>"
//				+ UserName
//				+ "</userId><password>"
//				+ Password
//				+ "</password></userOperationData></mAppData>";
//		return xml;
//	}

	public String getRequestXML(HashMap<String, String> map) {

		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "mAppData");
			serializer.startTag("", "userOperationData");

			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				if (pairs.getKey() != null && pairs.getValue() != null) {
					serializer.startTag("", pairs.getKey().toString());
					serializer.text(pairs.getValue().toString());
					serializer.endTag("", pairs.getKey().toString());
				}
				it.remove(); // avoids a ConcurrentModificationException
			}
			serializer.endTag("", "userOperationData");
			serializer.endTag("", "mAppData");
			serializer.endDocument();
			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}