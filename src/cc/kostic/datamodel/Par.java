package cc.kostic.datamodel;

public abstract class Par implements RanParameterToXml {
	
	private String name = "dummy name to throw error";
	private String value = "dummy value to throw error";
	private String comment = null;
	
	
	public Par() {
	}
	
	public Par(String naziv, String vrednost) {
		this.name = naziv;
		this.value = vrednost;
	}

	public Par(String naziv, String vrednost, String komentar) {
		this.name = naziv;
		this.value = vrednost;
		this.comment = komentar;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String vrednost) {
		this.value = vrednost;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
}
