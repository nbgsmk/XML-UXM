package cc.kostic.processing_rules;

public class Rule {
	
	private String sourceExcelColumn_Name;
	private int sourceExcelColumn_Index;
	private String destPath;
	private String adresa = "?";
	
	public Rule() {
	}
	
	
	public String getSourceExcelColumn_Name() {
		return sourceExcelColumn_Name;
	}
	
	public void setSourceExcelColumn_Name(String sourceExcelColumn_Name) {
		this.sourceExcelColumn_Name = sourceExcelColumn_Name;
	}
	
	public int getSourceExcelColumn_Index() {
		return sourceExcelColumn_Index;
	}
	
	public void setSourceExcelColumn_Index(int sourceExcelColumn_Index) {
		this.sourceExcelColumn_Index = sourceExcelColumn_Index;
	}
	
	public String getDestPath() {
		return destPath;
	}
	
	public void setDestPath(String destRanParameter) {
		this.destPath = destRanParameter;
	}
	
	public String getKeyColumn_excelAddr() {
		return adresa;
	}
	
	public void setKeyColumn_excelAddr(String keyColumn_excelAddr) {
		this.adresa = keyColumn_excelAddr;
	}
	
	@Override
	public String toString() {
		String s = "header: " + getSourceExcelColumn_Name() + " in: " + getKeyColumn_excelAddr() + "; destination ran path: " + getDestPath();
		return s;
	}
}
