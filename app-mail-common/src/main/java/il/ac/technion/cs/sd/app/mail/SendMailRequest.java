package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

class SendMailRequest {
	private JSONObject json;
	SendMailRequest(String from, String to, String message) {
		json = new JSONObject();
		json.put("type", RequestType.SEND_MAIL.ordinal());
		json.put("from", from);
		json.put("to", to);
		json.put("message", message);
	}
	
	JSONObject getJsonObject() {
		return json;
	}
}
