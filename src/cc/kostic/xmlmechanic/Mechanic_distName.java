package cc.kostic.xmlmechanic;

import cc.kostic.radioxm.RAN_Dictionary;
import org.w3c.dom.*;

import java.util.StringJoiner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Mechanic_distName {
	
	
	
	public String getDnPart(String distName, String part) {
		return null;
	}
	
	
	public String getBaseDN(String distName_Old) {
		String rezultat = cleanupDn( removePLMN(distName_Old));         // izbacim PLMN-PLMN
		
		String pathDelimiter = "/";
		// distName_Old = SBTS-5043/LCELC-3452/ANTL-3
		String[] sviSegmenti = new String[0];                     // segmenti su SBTS-5043, LCELC-3452, ANTL-3
		sviSegmenti = rezultat.split(pathDelimiter);              // sviSegmenti su SBTS-5043, LCELC-3452, ANTL-3
		
		return sviSegmenti[0];  // nulti element mora da je osnovni SBTS- ili MRBTS-..
	}
	
	public String removePLMN(String distName_Old) {
		String r = distName_Old.replace(RAN_Dictionary.PLMN_PLMN, "");
		if (r.startsWith("/")) {
			r = r.replaceFirst("/", "");
		}
		return r;
	}
	
	
	public String cleanupDn(String distName_Old) {
		String rezultat = removePLMN(distName_Old);
		if (rezultat.endsWith("/")) {
			rezultat = rezultat.substring(0, rezultat.length()-1);
		}
		return rezultat;
	}
	
	
	public String modifyDistName(String distName_Old, String dnSegmentName, String oldInstanceNum, int newInstanceNum) {
		String nw = String.valueOf(newInstanceNum);
		return modifyDistName(distName_Old, dnSegmentName, oldInstanceNum, nw);
	}
	
	public String modifyDistName(String distName_Old, String dnSegmentName, String oldInstanceNum, String newInstanceNum) {
		String pathDelimiter = "/";
		String idDelimiter = "-";
		
		// distName_Old = SBTS-5043/LCELC-3452/ANTL-3
		String[] sviSegmenti = new String[0];            // segmenti su SBTS-5043, LCELC-3452, ANTL-3
		String[] curSeg = new String[2];                // jedan curSeg je npr LCELC-3452
		String segId;                                    // segId je 3452
		
		sviSegmenti = distName_Old.split(pathDelimiter);                // sviSegmenti su SBTS-5043, LCELC-3452, ANTL-3
		for ( int i = 0; i < sviSegmenti.length; i++ ) {
			if ( sviSegmenti[i].contains(dnSegmentName) ) {
				curSeg = sviSegmenti[i].split(idDelimiter);        // daje curSeg[0]=LCELC, curSeg[1]=3452
				segId = curSeg[1];                                    // curSeg[1]=3452
				
				if ( oldInstanceNum.equals("*") || oldInstanceNum.isEmpty() ) {
					curSeg[1] = newInstanceNum;                            // ako je oldInstanceNum wildcard ili prazan, zameni ga
					sviSegmenti[i] = curSeg[0] + idDelimiter + curSeg[1];        // opet dobijam LCELC-<newInstanceNum>
					
				} else {
					if ( curSeg[1].equals(oldInstanceNum) ) {            // ako oldInstanceNum nije prazan, zameni ga sa novim samo ako je odgovarajuci
						curSeg[1] = newInstanceNum;
						sviSegmenti[i] = curSeg[0] + idDelimiter + curSeg[1];        // opet dobijam LCELC-<newInstanceNum>
					}
				}
				
			}
		}
		
		
		StringJoiner joiner = new StringJoiner(pathDelimiter);
		String novi = String.join(pathDelimiter, sviSegmenti);
		
		System.out.println(distName_Old + "\t" + novi);
		return novi;


		/*
		Test_ulazni
			class="SBTS" distName="SBTS-2092"
			class="LCEL" distName="SBTS-2092/ANTL-2"
			class="ADJW" distName="SBTS-2092/ANTL-22"
			class="ANTL" distName="SBTS-2092/ANTL-1/LCELC-2"
			class="ANTL" distName="SBTS-2092/ANTL-2/LCELC-2"
			class="ANTL" distName="SBTS-2092/ANTL-22/LCELC-3"

		test_1
			classFilter = ANTL
			objekat = ANTL
			oldInstanceNum = 2, newInstanceNum = 7
		rezultat_1
			SBTS-2092/ANTL-1/LCELC-2	SBTS-2092/ANTL-1/LCELC-2
			SBTS-2092/ANTL-2/LCELC-2	SBTS-2092/ANTL-7/LCELC-2
			SBTS-2092/ANTL-22/LCELC-3	SBTS-2092/ANTL-22/LCELC-3
		test_end

		test_2
			classFilter = * ili ""
			objekat = ANTL
			oldInstanceNum = 2, newInstanceNum = 7
		rezultat_2
			SBTS-2092					SBTS-2092
			SBTS-2092/ANTL-2			SBTS-2092/ANTL-7
			SBTS-2092/ANTL-22			SBTS-2092/ANTL-22
			SBTS-2092/ANTL-1/LCELC-2	SBTS-2092/ANTL-1/LCELC-2
			SBTS-2092/ANTL-2/LCELC-2	SBTS-2092/ANTL-7/LCELC-2
			SBTS-2092/ANTL-22/LCELC-3	SBTS-2092/ANTL-22/LCELC-3
		test_end

		test_3
			classFilter = * ili ""
			objekat = ANTL
			oldInstanceNum = * ili "", newInstanceNum = 7
		rezultat_3
			SBTS-2092					SBTS-2092
			SBTS-2092/ANTL-2			SBTS-2092/ANTL-7
			SBTS-2092/ANTL-22			SBTS-2092/ANTL-7
			SBTS-2092/ANTL-1/LCELC-2	SBTS-2092/ANTL-7/LCELC-2
			SBTS-2092/ANTL-2/LCELC-2	SBTS-2092/ANTL-7/LCELC-2
			SBTS-2092/ANTL-22/LCELC-3	SBTS-2092/ANTL-7/LCELC-3
		test_end


		 */
	}
	
	
	public void listByTag(Document doc, String tag) {

//		tag = "p";
//		tag = "managedObject";
//		tag = "moXX";
		/*
		Nadjem sve nodove "p" i menjam po zelji
		Problem je sto se ne zna da li je parent SBTS ili ANTL i slicno
		 */

//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList mOs = doc.getElementsByTagName(tag);
		System.out.println("----------------------------");
		StringBuilder sb = new StringBuilder();
		
		sb.append("koliko mOs " + mOs.getLength() + "\n");
		for ( int temp = 0; temp < mOs.getLength(); temp++ ) {
			Node mO = mOs.item(temp);
			NamedNodeMap atributi = mO.getAttributes();
			sb.append("MO-" + temp + "\n");
			sb.append(tag + " ATRIBUTI \t" + atributi.getLength() + "\n");
			for ( int atc = 0; atc < atributi.getLength(); atc++ ) {
				Node atr = atributi.item(atc);
				sb
						.append("parent:" + atr.getParentNode() + "\t")
						.append("atr_name:" + atr.getNodeName() + "\t")
						.append("atr_val:" + atr.getNodeValue() + "\t");
			}
			System.out.print(sb.toString());
			System.out.println("\n----\n");
		}
		
		
		for ( int temp = 0; temp < mOs.getLength(); temp++ ) {
			Node mO = mOs.item(temp);
			NodeList nl = mO.getChildNodes();
			sb = new StringBuilder();
			sb.append("MO-" + temp + "\n");
			sb.append(tag + " CHILD NODES \t" + nl.getLength() + "\n");
			for ( int i = 0; i < nl.getLength(); i++ ) {
				Node n = nl.item(i);
				sb
						.append("nod_parent:" + n.getParentNode() + "\t")
						.append("nod_nejm:" + n.getNodeName() + "\t")
						.append("nod_val:" + n.getNodeValue() + "\t")
						.append("nod_tekst:" + n.getTextContent() + "\t")
						.append("\n");
				
			}
			System.out.println(sb.toString());
			System.out.println("\n----\n");
			
		}
	}
	

	
}


