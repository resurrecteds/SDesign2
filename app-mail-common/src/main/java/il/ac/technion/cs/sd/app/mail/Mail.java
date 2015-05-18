package il.ac.technion.cs.sd.app.mail;

import org.json.JSONObject;

public class Mail {
	public final String from;
	public final String to;
	public final String content;

	public Mail(String from, String to, String content) {
		super();
		this.from = from;
		this.to = to;
		this.content = content;
	}
	
	Mail(JSONObject json) {
		this(json.getString("from"), json.getString("to"), json.getString("content"));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mail other = (Mail)obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	}
	
	public String toString() {
//		return "from:" + from + "%" + "to:" + to + "%" + "content:"+ content;
		return from + "%" + to + "%" + content;
	}
	
	JSONObject jsonValue() {
		JSONObject json = new JSONObject();
		json.put("from", from);
		json.put("to", to);
		json.put("content", content);
		return json;
	}
	
	
}
