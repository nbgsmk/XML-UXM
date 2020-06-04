package cc.kostic.radioxm;


import cc.kostic.probe.Xml_Radio_elementi_test;
import cc.kostic.radovi.CommFajl_1po1;
import cc.kostic.radovi.RET_razvrzlama;

import javax.swing.JFrame;
import java.io.IOException;

/*
	


TODO class="ETHLK"  <p name="connectorLabel">FSMF_EIF1_RJ45</p>
TODO LNCEL <p name="pMax">460</p>


*/



public class AppMain {

	public static void main(String[] args) {
		JFrame frame = new JFrame("naslov");
		frame.setContentPane(new GlavniPanel().panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
//		frame.setVisible(true);

//		Xml_Radio_elementi_test.udri();
//		RuleSet_test.udri();
		
//		RET_razvrzlama.udri(null, null);
		
		try {
			CommFajl_1po1 cf = new CommFajl_1po1();
			cf.udri(null, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}


