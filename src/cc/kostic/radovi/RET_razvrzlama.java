package cc.kostic.radovi;

import cc.kostic.datamodel.RanAtribut;
import cc.kostic.datamodel.RanManagedObject;
import cc.kostic.radioxm.RAN_Dictionary;
import cc.kostic.radioxm.Stoperica;
import cc.kostic.xmlmechanic.MechanicRadio2;
import cc.kostic.xmlmechanic.Mechanic_distName;
import cc.kostic.xmlmechanic.XMLMechanicCore;
import cc.kostic.xmlmechanic.XMLMechanicRadio;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class RET_razvrzlama {

	
	public static void udri(String ulazni_xml, List<String> siteList) {
		
		MechanicRadio2 xr = new MechanicRadio2();
		Mechanic_distName md = new Mechanic_distName();
		
		ulazni_xml = "LTE_export_parc2.xml";
//		ulazni_xml = "LTE_export_parc5.xml";
//		ulazni_xml = "LTE_export_parc10.xml";
//		ulazni_xml = "LTE_export_partial.xml";
//		ulazni_xml = "LTE_export_full.xml";
//		ulazni_xml = "LTE_to_SRAN_76_cluster_INT_201811106.xml";
		
		Stoperica st = new Stoperica();
		st.run();
		System.out.println("Processing " + ulazni_xml + " started");
		Document doc  = XMLMechanicCore.xmlCitac_OK(ulazni_xml);
		System.out.println("Processing " + ulazni_xml + " completed in " + st.getCurrentLapTime() + " seconds");
		
		List<RanManagedObject> sviRetu = xr.getRanManagedObject(doc, "RETU");
		List<RanManagedObject> sviALD = xr.getRanManagedObject(doc, "ALD");
		List<RanManagedObject> sviCREL= xr.getRanManagedObject(doc, "CREL");
		
		System.out.println("Lista objekata zavrsena za " + st.getCurrentLapTime() + " seconds");
		
		
		for (int k = 0; k < sviALD.size(); k++) {
			RanManagedObject ald = sviALD.get(k);
			String sn = ald.getParamValue("serialNumber");
			String dn =  ald.getDn();
			
			String retuSerial;
			retuSerial = "dN: " + dn + "\t" + "sn: " + sn;
//			System.out.println("dN: " + md.removePLMN(ald.getDn()) + "\t" + "sn: " + serial);
//			System.out.println("dN: " + md.getBaseDN(ald.getDn()) + "\t" + "sn: " + serial);
//			System.out.println(retuSerial);
		
			/////////////////////
			/////////////////////
			/////////////////////
			
			StringBuilder sb;
			for (int i = 0; i < sviRetu.size(); i++) {
				sb = new StringBuilder();
//				System.out.println("heap    : " + Runtime.getRuntime().totalMemory() / 1000000 + "MB");
				RanManagedObject retu = sviRetu.get(i);
				sb
						.append("\tDN: ").append("\t").append(retu.getDn())
						.append("\tantlDNList: ").append("\t").append(retu.getListAsString("antlDNList"))
						.append("\tbaseStationID: ").append("\t").append(retu.getParamValue("baseStationID"))
						.append("\tmechanicalAngle: ").append("\t").append(retu.getParamValue("mechanicalAngle"))
						.append("\tel.angle: ").append("\t").append(retu.getParamValue("angle"))
						.append("\n");
				
				System.out.println("-> " + dn);
//				System.out.println(sb.toString());
//				retu.setParamValue("mechanicalAngle", "do dzaja" + String.valueOf(i));
				
				if (retu.getDn().startsWith(ald.getDn() + "/")) {
					System.out.println(retuSerial + sb.toString());
					
					/////////////////////
					/////////////////////
					/////////////////////
					
					for (int j = 0; j < sviCREL.size(); j++) {
						RanManagedObject crel = sviCREL.get(j);
						if (crel.getDn().startsWith(dn + "/")) {
							System.out.println("crel dn: "+ crel.getDn());
							System.out.println("target: "+ crel.getParamValue("targetCellDN"));
							crel.setParamValue("targetCellDN", "zotja");
						}
					}
					/////////////////////
					/////////////////////
					/////////////////////
				}
				
				
				
				
				
			}
			
			
		
			System.out.println();
			System.out.println("-----");
			System.out.println();
		}
		
		
		



		XMLMechanicCore.xmlPisac_OK(doc, "ha.xml", false);
		
		
		
		
		System.out.println("heap    : " + Runtime.getRuntime().totalMemory() / 1000000 + "MB");
		System.out.println("Program zavrsen za " + st.stop() + " sekundi");
	}
}
