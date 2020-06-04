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

public class Ruta implements RanParameterToXml {

	private String comment = null;
	
	private List<RanParametar> list;
	
	private RanParametar dest;
	private final String dest_str = "destIpAddr";
	
	private RanParametar gw;
	private final String gw_str = "gateway";

	private RanParametar netMaskLen;
	private final String netMaskLen_str = "destinationIpPrefixLength";
	
	private RanParametar preSrc;
	private final String preSrcIpv4Addr_str = "preSrcIpv4Addr";

	private RanParametar preference;
	private final String preference_str = "preference";
	
	
	public Ruta() {
	}
	
	public Ruta(String destination, String gateway, String netMaskLength) {
		list = new ArrayList<>();
		
		this.dest = new RanParametar(dest_str, destination);
		list.add(dest);
		
		this.gw = new RanParametar(gw_str, gateway);
		list.add(gw);
		
		this.netMaskLen = new RanParametar(netMaskLen_str, netMaskLength);
		list.add(netMaskLen);
		
		this.preSrc = new RanParametar(preSrcIpv4Addr_str, "0.0.0.0");
		list.add(preSrc);
		
		this.preference = new RanParametar(preference_str, "1");
		list.add(preference);
	}
	
	public Ruta(String comment, String destination, String gateway, String netMaskLength) {
		list = new ArrayList<>();
		
		this.comment = comment;
		
		this.dest = new RanParametar(dest_str, destination);
		list.add(dest);
		
		this.gw = new RanParametar(gw_str, gateway);
		list.add(gw);
		
		this.netMaskLen = new RanParametar(netMaskLen_str, netMaskLength);
		list.add(netMaskLen);
		
		this.preSrc = new RanParametar(preSrcIpv4Addr_str, "0.0.0.0");
		list.add(preSrc);
		
		this.preference = new RanParametar(preference_str, "1");
		list.add(preference);
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
		Node novired1 = d.createTextNode("\n");
		df.appendChild(novired1);
		
		// odavde krece korisno
		Element e = d.createElement(RAN_Dictionary.item_str);
		if (this.getComment() != null) {
			Node comm = d.createComment(this.getComment());
			e.appendChild(comm);
		}
		for (int i = 0; i < list.size(); i++) {
			RanParametar p = list.get(i);
			Node q = d.adoptNode(p.toXml());
			e.appendChild(q);
		}
		df.appendChild(e);
		Node novired2 = d.createTextNode("\n");
		df.appendChild(novired2);
		
		return df;
	}
	
	
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
