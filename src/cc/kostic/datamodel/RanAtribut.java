package cc.kostic.datamodel;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

public class RanAtribut extends Par {
	
	private RanAtribut() {
		super();
		// no instance
	}
	
	public RanAtribut(String naziv, String vrednost) {
		super(naziv, vrednost);
	}
	
	
	@Override
	public DocumentFragment toXml() {
		return null;
	}
}
