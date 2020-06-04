package cc.kostic.xmlmechanic;

import cc.kostic.radioxm.RAN_Dictionary;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


import java.io.File;

/**
 * Funkcije koje razumeju xml strukturu ali ne znaju nista o radio parametrima
 */
public class XMLMechanicCore {

	/*
	ODLICNI XPath tutoriali sa primerima
	1)
	https://www.guru99.com/xpath-selenium.html

	2)
	https://doc.scrapy.org/en/xpath-tutorial/topics/xpath-tutorial.html

	 */
	
	public static Document xmlCitac_OK(String inputFile) {
		Document d = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setFeature("http://xml.org/sax/features/namespaces", false);
			dbFactory.setFeature("http://xml.org/sax/features/validation", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
			dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			dbFactory.setValidating(false);
			dbFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			d = doc;
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		return d;
	}
	
	public static Document createEmptyDoc() {
		Document doc = null;
		try {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
//			documentFactory.setValidating(true);
//			documentFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			doc = documentBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	
	public static boolean xmlPisac_OK(Document doc, String ime_fajla, boolean indenting) {
		boolean rezultat = false;
		try {
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			if (indenting) {
				xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			}
			xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(ime_fajla));
			xformer.transform(source, result);
			
			rezultat = true;
		} catch ( TransformerException e ) {
			e.printStackTrace();
			rezultat = false;
		}
		return rezultat;
	}
	
	
	/**
	 *
	 * @param doc XML document
	 * @param node  jedan <managedObject ...> koji je child od <cmData>
	 * @param deepClone true: kloniraju se svi parametri tj child elementi u managedObjectu; false: klonira se samo definicija <managedObject> i NISTA UNUTAR NJE
	 * @return <managedObject...> i sve/nista unutar njega, zavisno od deepClone </managedObject>
	 */
	public static Node cloneManagedObject(Document doc, Node node, boolean deepClone) {
		Node newNode = node.cloneNode(deepClone);
		NodeList ppp = doc.getElementsByTagName(RAN_Dictionary.cmdata_str);
		Node n = ppp.item(0);   // UVEK ima samo jedan CMDATA
		n.appendChild(newNode);         // njemu dodajemo novi MO
		return newNode;
	}
	
	/**
	 *
	 * @param doc XML document
	 * @param id    redni broj <managedObjekta> unutar <cmData> </cmData>. Moze postojati SAMO JEDAN cmData
	 * @param deepClone true: kloniraju se svi parametri tj child elementi u managedObjectu; false: klonira se samo definicija <managedObject> i NISTA UNUTAR NJE
	 * @return <managedObject...> i sve/nista unutar njega, zavisno od deepClone </managedObject>
	 */
	private static Document cloneMObyId(Document doc, int id, boolean deepClone) {
		NodeList nl = doc.getElementsByTagName(RAN_Dictionary.managedObject_str);
		Node mo2 = nl.item(id).cloneNode(deepClone);
		doc.getDocumentElement().appendChild(mo2);
		return doc;
	}
	
	
	
	
	
	
	
	
	
}
