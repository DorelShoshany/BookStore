package smartspace.plugins;

//import java.util.List;

public class ReadBookCoverResponse {
	
	private String response;
	
	public ReadBookCoverResponse() {
	}
	
	public ReadBookCoverResponse(String response) {
		this.setResponse(response);
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
