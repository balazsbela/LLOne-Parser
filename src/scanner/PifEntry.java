package scanner;

public class PifEntry {
	int code;
	int position;
	
	public PifEntry(int code,int position) {
		this.code = code;
		this.position = position;
	}
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}		
	
}
