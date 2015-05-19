package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

class GetCorrespondecesRequest {
	private JSONObject json;
	GetCorrespondecesRequest(String user, String corresponder, int howMany) {
		json = new JSONObject();
		json.put("type", RequestType.REQUEST_GET_CORRESPONDENCES.ordinal());
		json.put("user", user);
		json.put("corresponder", corresponder);
		json.put("howMany", howMany);
	}
	
	JSONObject getJsonObject() {
		return json;
	}
}
