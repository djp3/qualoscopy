package net.djp3.qualoscopy.events;

public enum QEventType {

	VOID, CHECK_VERSION,INITIATE_SESSION;

	public static String toString(QEventType x) {
		switch (x) {
		case CHECK_VERSION:
			return "CHECK_VERSION";
		case INITIATE_SESSION:
			return "INITIATE_SESSION";
		default:
			return "VOID";
		}
	}

	static public QEventType fromString(String x) {
		switch (x) {
		case "CHECK_VERSION":
			return CHECK_VERSION;
		case "INITIATE_SESSION":
			return INITIATE_SESSION;
		default:
			return VOID;
		}
	}

}
