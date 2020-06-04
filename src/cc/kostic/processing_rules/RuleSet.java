package cc.kostic.processing_rules;

import cc.kostic.xmlmechanic.ExcelMechanic_core;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.*;

public class RuleSet extends Rule {

	
	// TODO ograniciti ovo citanje na prvih 20ak redova
	
	public List<Rule> ranParameterRules;
	public List<Rule> ranAtributeRules;
	public List<Rule> distNameRules;
	
	private String keyColumn_name = "?";
	private int keyColumn_index = -1;
	private String keyColumn_excelAddr = "?";
	
//	private Map<String, String> distNameSegmenti;
//
//	private Map<String, String> ranAtributi;
	
	
	
	public RuleSet() {
		ranParameterRules = new ArrayList<>();
		ranAtributeRules = new ArrayList<>();
		distNameRules = new ArrayList<>();
	}
	
	public int getKeyColumn_index() {
		return keyColumn_index;
	}
	
	public void setKeyColumn_index(int keyColumn_index) {
		this.keyColumn_index = keyColumn_index;
	}
	
	public String getKeyColumn_name() {
		return keyColumn_name;
	}
	
	public void setKeyColumn_name(String keyColumn_name) {
		this.keyColumn_name = keyColumn_name;
	}
	
	
	public String getKeyColumn_excelAddr() {
		return keyColumn_excelAddr;
	}
	
	public void setKeyColumn_excelAddr(String keyColumn_excelAddr) {
		this.keyColumn_excelAddr = keyColumn_excelAddr;
	}
	
	
//	/**
//	 * proizvoljan broj parametara u vidu excel kolona x ide u SBTS, y ide u LNBTS, kolona z ide u LNCEL
//	 *
//	 * @param distNameColumn excel kolona koja sadrzi vrednost
//	 * @param distNameSeg u koji segment ide: distName="SBTS-23/LNBTS-344/LNCEL-7678"
//	 */
//	public void addDistNameSegment(String distNameColumn, String distNameSeg) {
//		if (this.distNameSegmenti == null) {
//			this.distNameSegmenti = new HashMap<>();
//		}
//		// proizvoljan broj parametara u vidu
//		// excel kolona x ide u SBTS
//		// excel kolona y ide u LNBTS
//		// excel kolona z ide u LNCEL
//		this.distNameSegmenti.put(distNameColumn, distNameSeg);
//	}
//

	
	
//	public Map<String, String> getRanAtributi() {
//		return ranAtributi;
//	}
//
//	public void addRanAttrib(String atr, String val) {
//		if (ranAtributi == null) {
//			ranAtributi = new HashMap<>();
//		}
//		ranAtributi.put(atr, val);
//	}
//
//
//	public int getLength() {
//		return ranParameterRules.size();
//	}
//
//	public List<Rule> getRanParameterRules() {
//		return ranParameterRules;
//	}
//

	
	

	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
//		sb.append("dN seg: " + distNameSegmenti);
//		sb.append("\n");
		sb.append("header: " + getKeyColumn_name() + " in: " + getKeyColumn_excelAddr());
		
		for (int i = 0; i < ranParameterRules.size(); i++) {
			sb.append("\n");
			sb.append(ranParameterRules.get(i).toString());
		}
		return sb.toString();
	}
	
}
