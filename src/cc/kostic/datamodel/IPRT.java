package cc.kostic.datamodel;

import cc.kostic.radioxm.RAN_Dictionary;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.List;

public class IPRT implements RanParameterToXml {
	
	private String komentar = null;
	private List<Ruta> list;
	
	public IPRT() {
		list = new ArrayList<>();
	}
	
	public void add(Ruta ruta) {
		this.list.add(ruta);
	}
	
	
	public String getKomentar() {
		return komentar;
	}
	
	public void setKomentar(String komentar) {
		this.komentar = komentar;
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
//		Node novired = d.createTextNode("\n");
//		df.appendChild(novired);
		
		// odavde krece korisno
		Element e = d.createElement(RAN_Dictionary.list_str);
		e.setAttribute(RAN_Dictionary.name_str, RAN_Dictionary.staticRoutes_str);
		for (int i = 0; i < list.size(); i++) {
			Ruta r = list.get(i);
			Node q = d.adoptNode(r.toXml());
			e.appendChild(q);
		}
		
		if (this.getKomentar() != null) {
			Node novired = d.createTextNode("\n");
			df.appendChild(novired);        // pretty print
			Node n = d.createComment(this.getKomentar());
			df.appendChild(n);
			Node novired2 = d.createTextNode("\n");
			df.appendChild(novired2);
		}
		df.appendChild(e);
		Node novired2 = d.createTextNode("\n");
		df.appendChild(novired2);        // pretty print
		return df;
	}
	
	
	
	
	
}
