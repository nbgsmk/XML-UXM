package cc.kostic.probe;

import cc.kostic.processing_rules.RuleSet;
import cc.kostic.processing_rules.RulesetLoader_CSV;

public class RuleSet_test {
	
	public static void udri() {
		
		RulesetLoader_CSV ldr = new RulesetLoader_CSV();
		RuleSet pravila_onetime = ldr.loadRulesFromCSV("pravila_one_time.csv");

		
		System.out.println(" rule lists loaded" );
		
		
		
	}
}
