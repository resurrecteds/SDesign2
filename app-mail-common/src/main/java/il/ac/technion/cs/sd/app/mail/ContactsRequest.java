package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

class ContactsRequest {
	JSONObject json;
	ContactsRequest(String sender) {
		json = new JSONObject();
		json.put("type", RequestType.REQUEST_CONTACTS.ordinal());
		json.put("from", sender);
	}
	
	JSONObject getJsonObject() {
		return json;
	}
}
