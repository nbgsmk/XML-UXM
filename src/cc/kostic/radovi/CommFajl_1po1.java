package cc.kostic.radovi;

import cc.kostic.processing_rules.Rule;
import cc.kostic.xmlmechanic.ExcelMechanic_core;
import org.apache.poi.ss.usermodel.*;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.kostic.datamodel.RanManagedObject;
import cc.kostic.processing_rules.RuleSet;
import cc.kostic.processing_rules.RulesetLoader_CSV;
import cc.kostic.radioxm.Stoperica;
import cc.kostic.xmlmechanic.MechanicRadio2;
import cc.kostic.xmlmechanic.Mechanic_distName;
import cc.kostic.xmlmechanic.XMLMechanicCore;

public class CommFajl_1po1 {


    private static final String RULESET_csv = "pravila_one_time.csv";
    private static final String DATAFILL_single = "datafill_single.xls";
    private static final String TEMPLATE_xml = "Commissioning_BGO796_v02.xml";


    public void udri(String ulazni_xml, List<String> siteList) throws IOException {

        MechanicRadio2 xr = new MechanicRadio2();
        Mechanic_distName md = new Mechanic_distName();

        ulazni_xml = TEMPLATE_xml;

        Stoperica st = new Stoperica();
        st.run();
        System.out.println("Processing " + ulazni_xml + " started");
        Document doc = XMLMechanicCore.xmlCitac_OK(ulazni_xml);
        System.out.println("Processing " + ulazni_xml + " completed in " + st.getCurrentLapTime() + " seconds");

        List<RanManagedObject> sviObj = xr.getRanManagedObject(doc, "");
	
	    ExcelMechanic_core exc = new ExcelMechanic_core();
	    Workbook excelFajl = exc.readExcelFile(DATAFILL_single);
	    Sheet dataFill = excelFajl.getSheetAt(0);
	
	    RulesetLoader_CSV rsl = new RulesetLoader_CSV();
        RuleSet rset = rsl.loadRulesFromCSV(RULESET_csv);
        rset = rsl.loadValuesFromExcel(dataFill, rset);
        System.out.println(rset);
        
        
        List<String> sajtovi = new ArrayList<>();
        sajtovi.add("KSO57");
        sajtovi.add("BGO863");
        String sajtNejm = sajtovi.get(0);
	
 
        // u excel datafill-u, vertikalno u koloni sajtNejmIndex, nadji red koji opisuje zadatu stanicu
	    int sajtNejmIndex = rset.getKeyColumn_index();
	    Row datafillRow = exc.getRowOf(dataFill, sajtNejm, sajtNejmIndex);
	    
	    int tmp;
	    
	    // iz kolone _index uzmi naziv sajta
	    tmp = rset.getKeyColumn_index();
	    String newSiteName = datafillRow.getCell(tmp).getStringCellValue();
	
	
	    RanManagedObject mo = sviObj.get(0);
	
	
	    // koje od segmenata SBTS, LNBTS, LNCEL itd..zelim da menjam
	    // distName="SBTS-5/LNBTS-2092/LNCEL-20925"
	    for (int i = 0; i < rset.distNameRules.size(); i++) {
		    Rule r = rset.distNameRules.get(i);
		    int colIndex = r.getSourceExcelColumn_Index();
		    Cell c = datafillRow.getCell(colIndex);
		    // buduci dist name broj tj brojcani deo u nekom od ovih distName="SBTS-5/LNBTS-2092/LNCEL-20925"
		    String new_number = exc.getStringFromNumericCell(c);
		    String fullDistName_old = mo.getDn();
		    String fullDistName_new = md.modifyDistName(fullDistName_old, r.getDestPath(), "*", new_number);
		    mo.setDn(fullDistName_new);
	    }
	    
	    
	    for (int i = 0; i < rset.ranParameterRules.size(); i++) {
	    	Rule r = rset.ranParameterRules.get(0);
		    int colIndex = r.getSourceExcelColumn_Index();
		    Cell c = datafillRow.getCell(colIndex);
		    String newval = exc.getStringFromNumericCell(c);
		    String dstPar = r.getDestPath();
		    mo.setParamValue(dstPar, newval);
	    }
	    
	
	    XMLMechanicCore.xmlPisac_OK(doc, "ha.xml", false);
	    
	    System.out.println("heap    : " + Runtime.getRuntime().totalMemory() / 1000000 + "MB");
	    System.out.println("Program zavrsen za " + st.stop() + " sekundi");
    }
    
    


}
