package cc.kostic.datamodel;

import cc.kostic.radioxm.RAN_Dictionary;
import cc.kostic.xmlmechanic.MechanicRadio2;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class RanManagedObject {
	
	private Node domNode;
	private MechanicRadio2 xp = new MechanicRadio2();
	
	private RanManagedObject() {
		// no instance
	}
	
	public RanManagedObject(Node domNode) {
		this.domNode = domNode;
	}
	

	public String getDn() {
		return this.getAttribute(RAN_Dictionary.distName_str);
	}
	
	public String getVersion() {
		return this.getAttribute(RAN_Dictionary.version_str);
	}
	
	public String getOperation() {
		return this.getAttribute(RAN_Dictionary.version_str);
	}
	
	
	public String getAttribute(String atributName) {
		return domNode.getAttributes().getNamedItem(atributName).getTextContent();
	}
	
	/**
	 *
	 * @return Class of the Managed object, one of SBTS, ANTL, LNCEL...
	 */
	public String getMoClass(){
		return this.getAttribute(RAN_Dictionary.class_str);
	}
	
	public void setAttribute(String parameterName, String parameterValue) {
		this.domNode.getAttributes().getNamedItem(parameterName).setTextContent(parameterValue);
	}
	
	public void setDn(String newDn) {
		this.setAttribute(RAN_Dictionary.distName_str, newDn);
	}
	
	public String getParamValue(String parameterName) {
		List<String> path = new ArrayList<>();
		path.add(parameterName);
		NodeList nl = xp.getMoParameters(this.domNode, null, path);
		String v = "";
		for (int i = 0; i < nl.getLength(); i++) {
			v = v + nl.item(i).getTextContent();
		}
		return v;
	}
	
	public void setParamValue(String paramName, String paramValue) {
		List<String> pathToParam = new ArrayList<>();
		pathToParam.add(paramName);
		xp.getMoParameters(this.domNode, null, pathToParam).item(0).setTextContent(paramValue);
	}
	
	
	public RanParametar getParametar(String parameterName) {
		List<String> path = new ArrayList<>();
		path.add(parameterName);
		NodeList nl = xp.getMoParameters(this.domNode, null, path);
		
		String v = "";
		for (int i = 0; i < nl.getLength(); i++) {
			v = v + nl.item(i).getTextContent();
		}
		return new RanParametar(parameterName, v);
	}
	
	public List<String> getList(String listName) {
		List<String> rezultat = null;
		List<String> path = new ArrayList<>();
		path.add(listName);
		NodeList nl = xp.getMoParameters(this.domNode, null, path);
		if (nl.getLength() > 1) {
			// moze biti SAMO JEDNA LISTA
			// TODO Loguj neku gresku !
		} else {
			NodeList chajld = nl.item(0).getChildNodes();
			int ln_len = chajld.getLength();
			rezultat = new ArrayList();
			for (int i = 0; i < chajld.getLength(); i++) {
				Node n = chajld.item(i);
				String t = n.getTextContent().trim();
				if ( ! t.isEmpty()) {
					rezultat.add( n.getTextContent() );
				}
			}
		}
		return rezultat;
	}
	
	public String getListAsString(String listName) {
		List<String> list = this.getList(listName);
		String rezultat = "";
		for (int i = 0; i < list.size(); i++) {
			rezultat += list.get(i).trim();
			rezultat += "\n";
		}
		return rezultat;
	}
}
