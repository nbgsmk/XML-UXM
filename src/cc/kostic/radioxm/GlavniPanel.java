package cc.kostic.radioxm;

import cc.kostic.xmlmechanic.XMLMechanicCore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GlavniPanel {
	public JPanel panel;
	private JButton btn;
	private String inputFile;
	
	public GlavniPanel() {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fc1();
				
			}
		});
		
	}
	
	
	
	
	
	
	public void fc1() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("XML files", "xml");
		chooser.setFileFilter(filter);
		chooser.setMultiSelectionEnabled(false);
		chooser.setCurrentDirectory(new File("."));
		int returnVal = chooser.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			String s = chooser.getSelectedFile().getName();
			System.out.println("You chose to open this file: " + s);
			setInputFile(s);
		} else {
			setInputFile(null);
		}
		
		if ( getInputFile() != null ) {
			XMLMechanicCore xmlCore = new XMLMechanicCore();
			xmlCore.xmlCitac_OK("Commissioning_BGO796_v02.xml");
//			xmlp.xmlCitac_OK(getInputFile());
		}
	}
	
	
	public String getInputFile() {
		return inputFile;
	}
	
	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}
}

	/*
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif");
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(parent);
		if ( returnVal == JFileChooser.APPROVE_OPTION ) {
			System.out.println("You chose to open this file: " +
					chooser.getSelectedFile().getName());
		}
* */


