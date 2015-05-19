package il.ac.technion.cs.sd.app.mail;

enum RequestType {
//	final String TYPE = "type";?
	
	// 0
	SEND_MAIL {
		public String toString() {
			return "SEND_MAIL";
		}
	},
	
	// 1
	REQUEST_GET_NEW_MAIL {
		public String toString() {
			return "GET_NEW_MAIL";
		}
	},
	
	// 2
	REQUEST_GET_ALL_MAIL {
		public String toString() {
			return "GET_ALL_MAIL";
		}
	},
	
	//3
	REQUEST_CONTACTS {
		public String toString() {
			return "GET_CONTACTS";
		}
	},
	
	//4
	REQUEST_GET_CORRESPONDENCES {
		public String toString() {
			return "GET_CORRESPONDENCES";
		}
	}
	
}
