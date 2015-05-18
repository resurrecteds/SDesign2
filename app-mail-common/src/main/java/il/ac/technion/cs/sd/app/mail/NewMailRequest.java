package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

class NewMailRequest {
	JSONObject json;
	NewMailRequest(String sender) {
		json = new JSONObject();
		json.put("type", RequestType.REQUEST_GET_NEW_MAIL.ordinal());
		json.put("from", sender);
	}
	
	JSONObject getJsonObject() {
		return json;
	}
}
