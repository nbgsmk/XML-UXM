package cc.kostic.xmlmechanic;

import cc.kostic.datamodel.RanAtribut;
import cc.kostic.datamodel.RanManagedObject;
import cc.kostic.datamodel.RanParametar;
import cc.kostic.radioxm.RAN_Dictionary;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;


/**
 * Funkcije koje razumeju xml strukturu i radio mrezu i parametre
 */
public class XMLMechanicRadio {
	

	/**
	 * PREPISANO
	 */
	public List<RanManagedObject> getRMO(Object doc, List<RanAtribut> atributFilters) {
		NodeList nl = getAttrs_MULTIFUNC(doc, atributFilters, null, true);
		List<RanManagedObject> lista = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			System.out.println("odozgo   : " + nl.item(i).hashCode());
			lista.add(new RanManagedObject(nl.item(i)));
		}
		return lista;
	}
	
	/**
	 * PREPISANO
	 */
	public List<RanManagedObject> getRMO(Object doc, String moClass) {
		List<RanAtribut> atr = new ArrayList<>();
		atr.add(new RanAtribut(RAN_Dictionary.class_str, moClass));
		NodeList nl = getAttrs_MULTIFUNC(doc, atr, null, true);
		List<RanManagedObject> lista = new ArrayList<>();
		for (int i = 0; i < nl.getLength(); i++) {
			lista.add(new RanManagedObject(nl.item(i)));
		}
		return lista;
	}
	
	/**
	 * PREPISANO
	 */
	public NodeList getMOAtributes_RADI(Object doc, List<RanAtribut> atributFilters, String atributToGet) {
		return getAttrs_MULTIFUNC(doc, atributFilters, atributToGet, false);
	}
	
	
	/**
	 * PREPISANO
	 * <managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">
	 * <p name="antennaConnector">a1</p>
	 * <p name="frRef">FR-1</p>
	 * <p name="ulDelay">u1</p>
	 * </managedObject>
	 *
	 * @param doc				XML document to process
	 * @param atributFilters	Map of key-value search terms, such as {[distName, LNCEL-xyz], [operation, create]...}
	 * @param atributToGet 		Return only this attribute for search term in atributFilters. If null, then ignore this parameter
	 * @param returnParent      false: return all matchin attributes; false: return parent object of each match
	 */
	private NodeList getAttrs_MULTIFUNC(Object doc, List<RanAtribut> atributFilters, String atributToGet, boolean returnParent) {
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
		
		String xPathExpression = RAN_Dictionary.xPathBase;
		xPathExpression = xPathExpression + RAN_Dictionary.managedObject_str;       // '//managedObject'
		if (atributFilters != null) {
			if (atributFilters.size() > 0) {
				StringJoiner sj = new StringJoiner(" and ", "[", "]");
				for (int i = 0; i < atributFilters.size(); i++) {
					RanAtribut a = atributFilters.get(i);
					String attrEQvalue = "@" + a.getName() + "='" + a.getValue() + "'";
					sj.add(attrEQvalue);
				}
			
			
/*
			for ( Map.Entry<String, String> entry : atributFilters.entrySet() ) {
				String attrEQvalue = "@" + entry.getKey() + "='" + entry.getValue() + "'";
				sj.add(attrEQvalue);
			}
*/
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
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		System.out.print(xPathExpression);
		System.out.println("\tfound " + nodeList.getLength());
		
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
	
	
	/**
	 * <managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">
	 *
	 * @param doc   XML Document
	 * @param moClass   "LNCEL"
	 * @param atributi    List< [diztname, SBTS...], [operation, create], [version, FL16] >
	 * @return  creates empty managed object without any children
	 *          <managedObject class="LNCEL" distName="SBTS-2092/LNBTS-443/LNCEL-20925" operation="create" version="FL16">
	 *          </managedObject>
	 */
	public Element createManagedObject_OK(Document doc, String moClass, List<RanAtribut> atributi) {
		if (atributi == null) {
			atributi = new ArrayList<>();
			atributi.add(new RanAtribut(RAN_Dictionary.distName_str, "BLA-1"));
			atributi.add(new RanAtribut(RAN_Dictionary.version_str, "FL21"));
		}
		
		Element el = doc.createElement(RAN_Dictionary.managedObject_str);
		
		// mora da ima class=NESTO to je najbitnije
		el.setAttribute(RAN_Dictionary.class_str, moClass);
		
		// a zatim i ostale atribute
		for (int i = 0; i < atributi.size(); i++) {
			String k = atributi.get(i).getName();
			String v = atributi.get(i).getValue();
			el.setAttribute(k, v);
		}
		
		NodeList cm = doc.getElementsByTagName(RAN_Dictionary.cmdata_str);
		Node r  = cm.item(0);
		r.appendChild(el);
		return el;
	}
	
	
	/**
	 *
	 * @param doc XML Document
	 * @param par Ran parameter (btsProfile, LG_42)
	 * @return <p name="btsProfile">LG_42</p>
	 */
	public Element createParameter(Document doc, RanParametar par) {
		Element e = doc.createElement(RAN_Dictionary.p_str);
		e.setAttribute(RAN_Dictionary.name_str, par.getName());
		e.setTextContent(par.getValue());
		return e;
	}
	
	/**
	 *
	 * @param doc XML Document
	 * @param parametri List< [btsProfile, LG_42]...[sbtsName, Baric_2] >
	 * @return    <item>
	 *              <p name="btsProfile">LG_42</p>  <p name="sbtsName">Baric_2</p>
	 *            </item>
	 */
	public Element createItem(Document doc, List<RanParametar> parametri) {
		Element ajtem = doc.createElement(RAN_Dictionary.item_str);
		for (int i = 0; i < parametri.size(); i++) {
			Element pnamejednako = createParameter(doc, parametri.get(i));
			ajtem.appendChild(pnamejednako);
		}
		return ajtem;
	}
	
	/**
	 *
	 * @param doc XML Document
	 * @param listName listName
	 * @param ajtemi List< Item > @see createItem()
	 * @return <list name="sbtsLocation">
	 *              <item>
	 *                  <p name="btsProfile">LG_42</p>  <p name="sbtsName">Baric_2</p>
	 * 	            </item>
	 *              <item>
	 *                  <p name="brzina">11</p>  <p name="visina">77</p>
	 * 	            </item>
	 * 	       </list>
	 */
	public Element createList(Document doc, String listName, List<Element> ajtemi) {
		Element lista = doc.createElement(RAN_Dictionary.list_str);
		lista.setAttribute(RAN_Dictionary.name_str, listName);
		for (int i = 0; i < ajtemi.size(); i++) {
			lista.appendChild(ajtemi.get(i));
		}
		return lista;
	}
	
}
