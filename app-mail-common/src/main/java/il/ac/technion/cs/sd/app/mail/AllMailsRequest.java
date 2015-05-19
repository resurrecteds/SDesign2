package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

class AllMailsRequest {
	private JSONObject json;
	private int amountToRetrieve;
	AllMailsRequest(String sender, int amountToRetrieve) {
		this.amountToRetrieve = amountToRetrieve;
		
		json = new JSONObject();
		json.put("type", RequestType.REQUEST_GET_ALL_MAIL.ordinal());
		json.put("from", sender);
	}
	
	JSONObject getJsonObject() {
		return json;
	}
	
	
}
