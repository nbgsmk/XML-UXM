package cc.kostic.datamodel;

import cc.kostic.radioxm.RAN_Dictionary;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RanParametar extends Par implements RanParameterToXml {
	
	private RanParametar() {
		// no instance
	}
	
	public RanParametar(String naziv, String vrednost) {
		super(naziv, vrednost);
	}
	
	public RanParametar(String naziv, String vrednost, String komentar) {
		super(naziv, vrednost, komentar);
	}
	
	
	@Override
	public DocumentFragment toXml() {
		
		// kreiram document fragment
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		Document d = documentBuilder.newDocument();
		DocumentFragment df = d.createDocumentFragment();
		
		// jedan prazan red - pretty print
		Node novired = d.createTextNode("\n");
//		df.appendChild(novired);        // pretty print
		
		// odavde krece korisno
		Element e = d.createElement(RAN_Dictionary.p_str);
		e.setAttribute(RAN_Dictionary.name_str, this.getName());
		e.setTextContent(this.getValue());
		

		if (this.getComment() != null) {
			Node nr1 = d.createTextNode("\n");
			df.appendChild(nr1);        // pretty print
			Node n = d.createComment(this.getComment());
			df.appendChild(n);
		}

		df.appendChild(e);
		Node novired2 = d.createTextNode("\n");
		df.appendChild(novired2);
		return df;
	}
	
	@Override
	public String toString() {
		return this.getValue();
	}
}
