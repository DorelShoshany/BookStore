package smartspace.plugins;

public class ReadBookCoverInput {
	private int markPage;
	private int size;
	private int page;
	
	public ReadBookCoverInput() {
		this.setSize(10);
		this.setPage(0);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getMarkPage() {
		return markPage;
	}

	public void setMarkPage(int markPage) {
		this.markPage = markPage;
	}

}
