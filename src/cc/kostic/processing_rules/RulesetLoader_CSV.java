package cc.kostic.processing_rules;

import cc.kostic.radioxm.RAN_Dictionary;
import cc.kostic.xmlmechanic.ExcelMechanic_core;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.*;
import java.util.Map;
import java.util.StringJoiner;

public class RulesetLoader_CSV {
	
	private String filename;
	private String delimiter = ";";
	
	public RulesetLoader_CSV() {
	}
	
	public String getFilename() {
		return filename;
	}
	
	private void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getDelimiter() {
		return delimiter;
	}
	
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
	
	
	public RuleSet loadRulesFromCSV(String csvFile) {
		setFilename(csvFile);
		RuleSet ruleSet = citacCSV(getFilename());
		return ruleSet;
	}
	
	
	
	private RuleSet citacCSV(String filename) {
		RuleSet rs = new RuleSet();
		File input = new File(filename);
		try (BufferedReader br = new BufferedReader(new FileReader(input))) {
			String line;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if ( (line.isEmpty()) || (line.trim().startsWith("//")) ) {
					// preskoci prazne i linije sa komentarima
					continue;
				}
				String[] delovi = line.split(delimiter);
				for (int i = 0; i < delovi.length; i++) {
					delovi[i] = delovi[i].trim();
				}
				
				Rule r = new Rule();
				
				switch (delovi.length) {
					case 1:
						// 1 csv polje -> excel kolona "NAZIV_LOKACIJE" sadrzi naziv sajta. to je kljucna excel kolona po kojoj pretrazujem excel redove
						// siteNameColumn=NAZIV_LOKACIJE <- naziv_lokacije
						String a = delovi[0];
						if (a.startsWith(RAN_Dictionary.siteNameColumn_str)) {
							String b = a.replace(RAN_Dictionary.siteNameColumn_str, "").trim();
							b = b.replaceFirst("(:=)|(=:)|(:)|(=)|( )", "").trim();
							rs.setKeyColumn_name(b);
						}
						break;
						
					case 2:
						// 2 csv polja -> koja excel kolona ide u koji ran atribut
						// kolona_abc ; operation
						// kolonaxyz ; version
						r.setSourceExcelColumn_Name(delovi[0]);
						r.setDestPath(delovi[1]);
						rs.ranAtributeRules.add(r);
						break;
					
					case 3:
						// 3 csv polja -> koja excel kolona ide u koji deo distName-a
						// excel_kolona_a ; distName ; SBTS
						// excel_kolona_b ; distName ; LNBTS
						// distName="SBTS-5/LNBTS-2092/LNCEL-20925"
						if (delovi[1].equalsIgnoreCase(RAN_Dictionary.distName_str)) {
							r.setSourceExcelColumn_Name(delovi[0]);
							r.setDestPath(delovi[2]);
							rs.distNameRules.add(r);
						}
						break;
						
					default:
						// 4 csv polja i vise -> kolona "OPIS_LOK" ide u putanju class=SBTS, parametar <p name="sbtsDescription">_vrednost_bla_</p>
						// putanja DestPath
						// OPIS_LOK ; class ; SBTS ; sbtsDescription
						r.setSourceExcelColumn_Name(delovi[0]);
						StringJoiner sj = new StringJoiner(delimiter);
						for (int i = 1; i < delovi.length; i++) {
							sj.add(delovi[i].trim());
						}
						r.setDestPath(sj.toString());
						rs.ranParameterRules.add(r);
						break;
						
						
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	
	
	
	public RuleSet loadValuesFromExcel(Sheet sheet, RuleSet ruleSet) {
		ExcelMechanic_core exc = new ExcelMechanic_core();
		Cell temp = exc.getCellContainingText(sheet, ruleSet.getKeyColumn_name());
		ruleSet.setKeyColumn_index(temp.getColumnIndex());
		ruleSet.setKeyColumn_excelAddr(temp.getAddress().formatAsString());
		
		for (int i = 0; i < ruleSet.ranParameterRules.size(); i++) {
			Rule r = ruleSet.ranParameterRules.get(i);
			Cell c = exc.getCellContainingText(sheet, r.getSourceExcelColumn_Name());
			ruleSet.ranParameterRules.get(i).setSourceExcelColumn_Index(c.getColumnIndex());
			ruleSet.ranParameterRules.get(i).setKeyColumn_excelAddr(c.getAddress().formatAsString());
		}
		
		for (int i = 0; i < ruleSet.distNameRules.size(); i++) {
			Rule r = ruleSet.distNameRules.get(i);
			Cell c = exc.getCellContainingText(sheet, r.getSourceExcelColumn_Name());
			ruleSet.distNameRules.get(i).setSourceExcelColumn_Index(c.getColumnIndex());
			ruleSet.distNameRules.get(i).setKeyColumn_excelAddr(c.getAddress().formatAsString());
		}
		
		for (int i = 0; i < ruleSet.ranAtributeRules.size(); i++) {
			Rule r = ruleSet.ranAtributeRules.get(i);
			Cell c = exc.getCellContainingText(sheet, r.getSourceExcelColumn_Name());
			ruleSet.ranAtributeRules.get(i).setSourceExcelColumn_Index(c.getColumnIndex());
			ruleSet.ranAtributeRules.get(i).setKeyColumn_excelAddr(c.getAddress().formatAsString());
		}
	
		return ruleSet;
	}
	
	
}
