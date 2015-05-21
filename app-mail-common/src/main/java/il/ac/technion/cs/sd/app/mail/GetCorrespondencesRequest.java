package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

class GetCorrespondencesRequest {
	private JSONObject json;
	
	static class JSONKey {
		static final String USER = "user";
		static final String CORRESPONDER = "corresponder";
	}
	
	GetCorrespondencesRequest(String user, String corresponder, int howMany) {
		json = new JSONObject();
		json.put("type", RequestType.REQUEST_GET_CORRESPONDENCES.ordinal());
		json.put(JSONKey.USER, user);
		json.put(JSONKey.CORRESPONDER, corresponder);
		json.put("howMany", howMany);
	}
	
	JSONObject getJsonObject() {
		return json;
	}
}


