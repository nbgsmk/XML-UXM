package cc.kostic.xmlmechanic;

import cc.kostic.datamodel.RanAtribut;
import cc.kostic.datamodel.RanManagedObject;
import cc.kostic.radioxm.RAN_Dictionary;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.rmi.MarshalledObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MechanicRadio2 {
	
	/**
	 *
	 * @param doc DOM document
	 * @param moClass Ran attribute class=[ANTL, LNCEL, SBTS...]
	 * @return List of <managedOBjects filtered by class="ANTL" and wrapped into RanManagedObject.class
	 */
	public List<RanManagedObject> getRanManagedObject(Document doc, String moClass) {
		List<RanAtribut> atr = new ArrayList<>();
		if ( ! moClass.isEmpty()) {
			atr = new ArrayList<>();
			atr.add(new RanAtribut(RAN_Dictionary.class_str, moClass));
		}
		return getRanManagedObject(doc, atr);
	}
	
	/**
	 *
	 * @param doc DOM document
	 * @param moAttributes Map<String, String> of {"class", "ANTL"; "version", "FL16" ...}
	 * @return List of <managedOBjects filtered by class=[], version=[]... and wrapped into RanManagedObject.class
	 */
	public List<RanManagedObject> getRanManagedObject(Document doc, Map<String, String> moAttributes) {
		List<RanAtribut> atrs = new ArrayList<>();
		for (Map.Entry<String, String> entry : moAttributes.entrySet()) {
			RanAtribut atribut = new RanAtribut(entry.getKey(), entry.getValue());
			atrs.add(atribut);
		}
		return getRanManagedObject(doc, atrs);
	}
	
	/**
	 *
	 * @param doc DOM document
	 * @param moAttributes Map<String, String> of {"class", "ANTL"; "version", "FL16" ...}
	 * @return List of <managedOBjects filtered by class=... and wrapped into RanManagedObject.class
	 */
	public List<RanManagedObject> getRanManagedObject(Document doc, List<RanAtribut> moAttributes) {
		NodeList nl = getAttrs_MULTIFUNC(doc, moAttributes, null, true);
		List<RanManagedObject> lista = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			lista.add(new RanManagedObject(nl.item(i)));
		}
		return lista;
	}
	
	
	
	/**
	 * <managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">
	 * <p name="antennaConnector">a1</p>
	 * <p name="frRef">FR-1</p>
	 * <p name="ulDelay">u1</p>
	 * </managedObject>
	 *
	 * @param doc				XML document to process
	 * @param atributFilters	Search terms, such as {[distName, LNCEL-xyz], [operation, create]...}
	 * @param atributToGet 		Return only this attribute for search term in atributFilters. If null, then ignore this parameter
	 * @param returnParent      false: return all matching attributes; false: return parent object of each match
	 */
	private NodeList getAttrs_MULTIFUNC(Document doc, List<RanAtribut> atributFilters, String atributToGet, boolean returnParent) {

		String xPathExpression = RAN_Dictionary.xPathBase + RAN_Dictionary.managedObject_str;       // '//managedObject'

		/*
								1)	doc, 			"LNCEL", 		"distName",			 null, 			        null
								2)	doc, 			"LNCEL", 		"distName",	 "SBTS-2092/LNBTS-443", "SBTS-2092/LNBTS-66"

								<managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">


				XPATH tutorial i primeri
				https://doc.scrapy.org/en/xpath-tutorial/topics/xpath-tutorial.html
				https://www.guru99.com/xpath-selenium.html

				XPath testbed
				http://www.whitebeam.org/library/guide/TechNotes/xpathtestbed.rhtm


				//managedObject[@class='ANTL']
				//managedObject[@class='ANTL' and @operation='create']
				//managedObject[@class='ANTL' and contains(@distName, 'ANTL-1')]
				daje nodeset koji sadrzi SVE PARAMETRE svakog od zeljenih objekata (<p name="frRef" >FR-2</p> <p name="ulDelay" >u2</p> ...)

				//managedObject[@class='ANTL']/@
				daje nodeset koji sadrzi SVE ATRIBUTE zeljenih objekata(class=ANTL, distName=..., operation=... version..)

				//managedObject[@version and @operation='create']/@
				daje nodeset koji sadrzi OBAVEZAN ATRIBUT VERSION=<BILO STA> i operation=create i sve njihove atribute

		*/
		
		
		if (atributFilters != null) {
			if (atributFilters.size() > 0) {
				StringJoiner sj = new StringJoiner(" and ", "[", "]");
				for (int i = 0; i < atributFilters.size(); i++) {
					RanAtribut a = atributFilters.get(i);
					String attrEQvalue = "@" + a.getName() + "='" + a.getValue() + "'";
					sj.add(attrEQvalue);
				}
				xPathExpression = xPathExpression + sj.toString();    // dobije se "  //managedObject[@class='ANTL' and @operation='create']  "
			}
		}
		
		if ( (atributToGet==null) || (atributToGet.equals("*")) ) {
			xPathExpression = xPathExpression + "/@*";		//svi atributi su managedObject[@class='ANTL' and @operation='create']/@*
		} else {
			xPathExpression = xPathExpression + "/@" + atributToGet;	// odredjeni atribut //managedObject[@class='ANTL' and @operation='create']/@version
		}
		
		if (returnParent == true) {
			// return any kind of parent
			xPathExpression = xPathExpression + "/parent::*";
			
			// return exactly the parent managedObject
//        	xPathExpression = xPathExpression + "/parent::" + RAN_Dictionary.managedObject_str;
		}
		
		NodeList nodeList = null;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			nodeList = (NodeList) xPath.compile(xPathExpression).evaluate(doc, XPathConstants.NODESET);
			System.out.println(xPathExpression + "\tfound " + nodeList.getLength());
		} catch ( Exception e ) {
			System.err.println("Error in XPath expression " + xPathExpression + " in " + this.getClass().getCanonicalName());
			e.printStackTrace();
		}
		return nodeList;
	}
	
	
	
	
	/**
	 * <managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">
	 * <p name="antennaConnector">a1</p>
	 * <p name="frRef">FR-1</p>
	 * <p name="ulDelay">u1</p>
	 * </managedObject>
	 *  @param doc              XML document to process
	 * @param atributFilters    Map of key-value search terms, such as {[distName, LNCEL-xyz], [operation, create]...}
	 * @param parameterSegments
	 */
	public NodeList getMoParameters(Object doc, List<RanAtribut> atributFilters, List<String> parameterSegments) {
		/*
								1)	doc, 			"LNCEL", 		"distName",			 "*", 			"SBTS-2092/LNBTS-77"
								2)	doc, 			"LNCEL", 		"distName",	 "SBTS-2092/LNBTS-443", "SBTS-2092/LNBTS-66"

								<managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">

				XPATH tutorial i primeri
				https://doc.scrapy.org/en/xpath-tutorial/topics/xpath-tutorial.html
				https://www.guru99.com/xpath-selenium.html

				XPath testbed
				http://www.whitebeam.org/library/guide/TechNotes/xpathtestbed.rhtm

				//managedObject[@class='ANTL']
				//managedObject[@class='ANTL' and @operation='create']
				//managedObject[@class='ANTL' and contains(@distName, 'ANTL-1')]
				daje nodeset koji sadrzi SVE PARAMETRE svakog od zeljenih objekata (<p name="frRef" >FR-2</p> <p name="ulDelay" >u2</p> ...)

				//managedObject[@class='ANTL']/@
				daje nodeset koji sadrzi SVE ATRIBUTE zeljenih objekata(class=ANTL, distName=..., operation=... version..)

				//managedObject[@version and @operation='create']/@
				daje nodeset koji sadrzi OBAVEZAN ATRIBUT VERSION=<BILO STA> i operation=create i sve njihove atribute

		*/
		
		
		String temp = "";
		
		if (atributFilters != null) {
			if (atributFilters.size() > 0) {
				StringJoiner sj = new StringJoiner(" and ", "[", "]");
				for (int i = 0; i < atributFilters.size(); i++) {
					RanAtribut a = atributFilters.get(i);
					String attrEQvalue = "@" + a.getName() + "='" + a.getValue() + "'";
					sj.add(attrEQvalue);                                // [@class='ANTL' and @operation='create' and <...> ]
				}
				
				temp = temp + sj.toString();
			}
		}
		if (parameterSegments != null) {
			if (parameterSegments.size() > 0) {
//				StringJoiner sj = new StringJoiner("']//*[@name='", "//*[@name='", "']");
				StringJoiner sj = new StringJoiner("']//*[@name='", "*[@name='", "']");
				for (int i = 0; i < parameterSegments.size(); i++) {
					sj.add(parameterSegments.get(i));
				};
				temp = temp + sj.toString();
			}
		} else {
			// vrati sve parametre
			temp = temp + "/*";		//svi parametri unutar managedObject[@class='ANTL' and @operation='create']/*
		}
		
		
		String xPathExpression = "";
		
		// RADI za bilo koji path/subpath
//		temp = temp  + "//p[@name='" + bottomLevelParameter_DO_NOT_USE + "']";      //managedObject[@class='ANTL' and @operation='create']//p[@name='antennaConnector']
		//managedObject[@class='SBTS' and @operation='create']
		//*[@name='sbtsLocation']//
		//*[@name='sbtsLocation']//*[@name='address']//
		
		// UVEK IDE OD VRHA celog xml dokumenta, cak i ako mu predam samo jeadan Node
		// xPathExpression = RAN_Dictionary.xPathBase + RAN_Dictionary.managedObject_str;       // '//managedObject'
		
		// Pocinje od tekuceg noda
		xPathExpression = ".//" + temp;
		
		NodeList nodeList = null;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			nodeList = (NodeList) xPath.compile(xPathExpression).evaluate(doc, XPathConstants.NODESET);
//			System.out.println(xPathExpression + "\tfound: " + nodeList.getLength());
		
		} catch ( Exception e ) {
			e.printStackTrace();
			System.out.println("Error, nothing found !");
		}
		
		return nodeList;
		
	}
	
	
	
}
