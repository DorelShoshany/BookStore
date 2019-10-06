package smartspace.plugins;

public class EchoResponse {
	private String result;
	
	public EchoResponse() {
	}
	
	public EchoResponse(String res) {
		super();
		this.setResult(res);
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
