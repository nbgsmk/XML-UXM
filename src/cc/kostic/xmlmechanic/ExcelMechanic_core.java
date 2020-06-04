package cc.kostic.xmlmechanic;



import org.apache.poi.ss.usermodel.*;


import java.io.File;
import java.io.IOException;

/*

	https://www.callicoder.com/java-read-excel-file-apache-poi/
	https://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/
	
	// KAKO CITATI SAMO FIZICKI PRISUTNE REDOVE I KOLONE
	https://stackoverflow.com/questions/1516144/how-to-read-and-write-excel-file
	
	
	https://gist.github.com/madan712/3912272
	
	TODO hidden cells?

*/

public class ExcelMechanic_core {
	
	
	public ExcelMechanic_core() {
	}
	
	
	
	public Workbook readExcelFile(String excelFileName) throws IOException {
		File fajl = new File(excelFileName);
		Workbook excelFajl = WorkbookFactory.create(fajl);       // Creating a Workbook from an Excel file (.xls or .xlsx)
		return excelFajl;
	}
	
	
	public int getIndexOfColumnName(Sheet sheet, String targetCol) {
		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();
		int rezultat = -1;
		Cell rezultatCelija = null;
		outerloop:
		for (Row row: sheet) {
			for(Cell cell: row) {
				String cellValue = dataFormatter.formatCellValue(cell);
//				System.out.print(cellValue + "\t");
				if (cellValue.equalsIgnoreCase(targetCol)) {
					rezultat = cell.getColumnIndex();
					rezultatCelija = cell;
//					System.out.println(targetCol + " pronadjen u " + cell.getAddress().formatAsString());
					break outerloop;
				}
			}
			if (row.getRowNum() > 30) {
				// ako nije pronadjeno u prvih nekoliko redova, odustani
				break outerloop;
			}
		}
//		return rezultatCelija;
		return rezultat;
	}
	
	
	
	public Cell getCellContainingText(Sheet sheet, String targetCol) {
		// Create a DataFormatter to format and get each cell's value as String
		DataFormatter dataFormatter = new DataFormatter();
		int rezultat = -1;
		Cell rezultatCelija = null;
		outerloop:
		for (Row row: sheet) {
			for(Cell cell: row) {
				String cellValue = dataFormatter.formatCellValue(cell);
//				System.out.print(cellValue + "\t");
				if (cellValue.equalsIgnoreCase(targetCol)) {
					rezultat = cell.getColumnIndex();
					rezultatCelija = cell;
//					System.out.println(targetCol + " pronadjen u " + cell.getAddress().formatAsString());
					break outerloop;
				}
			}
			if (row.getRowNum() > 30) {
				// ako nije pronadjeno u prvih nekoliko redova, odustani
				break outerloop;
			}
		}
		return rezultatCelija;
//		return rezultat;
	}
	
	
	
	
	public Row getRowOf(Sheet datafill, String siteName, int colIndex) {
		Row r = null;
		for (int ro = 0; ro < datafill.getPhysicalNumberOfRows(); ro++) {
			Row temp = datafill.getRow(ro);
			if (temp == null) {
				continue;
			}
			String val = datafill.getRow(ro).getCell(colIndex).getStringCellValue();
			if (!val.equalsIgnoreCase(siteName)) {
				continue;
			}
			System.out.println("YEAH! ro: " + ro + " co: " + colIndex + " val: " + val);
			r = temp;
			return r;
		}
		return r;
	}
	
	public String getStringFromNumericCell(Cell cell) {
		String rezultat;
		if (cell.getCellType() == CellType.NUMERIC) {
			rezultat = String.valueOf( (int) cell.getNumericCellValue());
		} else {
			rezultat = cell.getStringCellValue().trim();
		}
		return rezultat;
		
	}
}
