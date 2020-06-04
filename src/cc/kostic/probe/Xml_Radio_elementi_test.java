package cc.kostic.probe;

import cc.kostic.datamodel.IPRT;
import cc.kostic.datamodel.RanAtribut;
import cc.kostic.datamodel.RanParametar;
import cc.kostic.datamodel.Ruta;
import cc.kostic.radioxm.RAN_Dictionary;
import cc.kostic.xmlmechanic.Mechanic_distName;
import cc.kostic.xmlmechanic.XMLMechanicCore;
import cc.kostic.xmlmechanic.XMLMechanicRadio;
import org.w3c.dom.*;

import java.util.ArrayList;
import java.util.List;



public class Xml_Radio_elementi_test {

	public static void udri() {
		
		Mechanic_distName mechanicDN = new Mechanic_distName();
		XMLMechanicRadio xmr = new XMLMechanicRadio();
		Document doc  = XMLMechanicCore.xmlCitac_OK("input2.xml");
		Element mojRoot = (Element) doc.getElementsByTagName(RAN_Dictionary.cmdata_str).item(0);
		
		
		
		List<RanAtribut> atrFilter = new ArrayList<>();
//		atrFilter.add(new RanAtribut("class", "SBTS"));
		atrFilter.add(new RanAtribut("class", "ANTL"));
//		atrFilter.add(new RanAtribut("class", "IPRT"));
		atrFilter.add(new RanAtribut("operation", "create"));
//		atrFilter.add(new RanAtribut("version", "SBTS17"));
		
		String atrToGet = null;
//		atrToGet = "version";
		
		
		NodeList nList = null;
		
		
		// RADI !! //
		nList = xmr.getMOAtributes_RADI(doc, atrFilter, atrToGet);
		System.out.println("elemenata: " + nList.getLength());
		for (int i = 0; i < nList.getLength(); i++) {
			Node n = nList.item(i);
			System.out.println(n.getNodeName() + "\t" + n.getTextContent());
		}
		
		
		
		
		List<String> paramSegments = new ArrayList<>();
//		paramSegments.add("frRef");
//		paramSegments.add("sbtsLocation");
		paramSegments.add("gnf");
		paramSegments.add("visina");
		
		String najniziParam = null;
//		najniziParam = "visina";
//		najniziParam = "brzina";
		
		String valueToGet = null;
//		valueToGet = "a3";
		
		// RADI !! //
		nList = xmr.getMoParameters(doc, null, paramSegments);
//		System.out.println("elemenata: " + nList_2.getLength());
		for (int i = 0; i < nList.getLength(); i++) {
			Node n = nList.item(i);
			System.out.println(n.getTextContent());
		}
		
		
		
		
		// RADI //
		nList = xmr.getMoParameters(doc, atrFilter, null);
		System.out.println("elemenata: " + nList.getLength());
		for (int i = 0; i < nList.getLength(); i++) {
			Node n = nList.item(i);
			System.out.println(n.getNodeName() + "\t" + n.getTextContent());
		}
		
		
		// RADI //
		Node klon = XMLMechanicCore.cloneManagedObject(doc, nList.item(0), true);
		String stari = klon.getAttributes().getNamedItem(RAN_Dictionary.distName_str).getTextContent();
		String novi = mechanicDN.modifyDistName(stari, "SBTS", "*", "klon");
		klon.getAttributes().getNamedItem(RAN_Dictionary.distName_str).setTextContent(novi);
		
		// RADI //
		xmr.createManagedObject_OK(doc, "IPRT", null);
//		XMLMechanicRadio.createManagedObject_OK(doc, "IPRT", null);
//		XMLMechanicRadio.createManagedObject_OK(doc, "IPRT", null);
		
		
		RanParametar pa1 = new RanParametar("ab", "cd");
		pa1.setComment("vrlo vazan parametar");
		Node pa1n = doc.importNode(pa1.toXml(),true);
		mojRoot.appendChild(pa1n);
		
		Ruta r1 = new Ruta("ruta levo", "qa", "qz", "cc");
		Ruta r2 = new Ruta("ruta desno", "xx", "yy", "zz");
		Node r1n = doc.importNode(r1.toXml(),true);
		mojRoot.appendChild(r1n);

		IPRT iprt = new IPRT();
		iprt.add(r1);
		iprt.add(r2);
		iprt.setKomentar("sve moguce rute za nedodjiju");

		Node ipr = doc.importNode(iprt.toXml(), true);
		mojRoot.appendChild(ipr);
		

		RanParametar RP = new RanParametar("DODZAJA", "DZAJA DZAJA");
		DocumentFragment ddf = RP.toXml();
		doc.adoptNode(ddf);
		mojRoot.appendChild(ddf);
		
		//		q = doc.importNode(r2.toXml(),true);
//		mojRoot.appendChild(q);
		
//		XMLMechanicRadio.createList(doc, "nesto", null);
//		Element e2 = pa.toXml();
//		Element r1e = r1.toXml();
//		Element r2e = r2.toXml();
		
//		Element rut = doc.getDocumentElement();
		
		
//		rut.adoptNode(e1);
//		rut.appendChild(e1);
//		doc.adoptNode(r1e);
//		doc.appendChild(r1e);
//		doc.adoptNode(r2e);
//		doc.appendChild(r2e);
		
		boolean uspeh = XMLMechanicCore.xmlPisac_OK(doc, "data_new.xml", false);
//		System.out.println(uspeh);
	
	
	
	
	}
	
	


}

