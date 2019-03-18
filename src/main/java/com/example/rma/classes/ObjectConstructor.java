package com.example.rma.classes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

public class ObjectConstructor {
	private int NUMBER_OF_COLUMNS = 24;
	
	private List<Entry> parseEntry(String foo) {
		List<Entry> parsed_data = new ArrayList<>();
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		//System.out.println("-- parseEntry --");
		//System.out.println(bar);
		
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));

			//System.out.println("bar:");
			//System.out.println(bar.get(i));
			
			//System.out.println("foobar:");
			//System.out.println(foobar);
			
			if (i==0 && foobar.size() == 1) {
				// no results
				break;
			}
			
			//add the new entry
			parsed_data.add(new Entry(
					Integer.parseInt(foobar.get(0)), 
					foobar.get(1), 
					foobar.get(2), 
					foobar.get(3), 
					Date.valueOf(foobar.get(4)), 
					foobar.get(5), 
					foobar.get(6),
					foobar.get(7), 
					Date.valueOf(foobar.get(8)), 
					Integer.parseInt(foobar.get(9)), 
					foobar.get(10), 
					Date.valueOf(foobar.get(11)), 
					Date.valueOf(foobar.get(12)),
					Integer.valueOf(foobar.get(13)), 
					Integer.valueOf(foobar.get(14)), 
					foobar.get(15),
					foobar.get(16), 
					Integer.parseInt(foobar.get(17)),
					Integer.parseInt(foobar.get(18)),
					foobar.get(19), 
					foobar.get(20), 
					foobar.get(21), 
					Integer.parseInt(foobar.get(22)),
					Integer.parseInt(foobar.get(23))
					));
		}
		
		return parsed_data;
	}
	
	private List<String> parseSuppliers(String foo) {
		List<String> parsed_data = new ArrayList<>();
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		for (int i=0; i < bar.size(); i = i + 1) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i)));
			if (foobar.get(0).equals("")) {
				continue;
			}
			
			parsed_data.add(foobar.get(0));
		}
		
		return parsed_data;
	}
	
 	private List<List<String>> parseSupplierSummary(String foo) {
		List<List<String>> parsed_data = new ArrayList<>();
		List<String> bar = new ArrayList<String>(Arrays.asList(foo.split("::\n")));
		
		List<String> foobar;
		for (int i = 0; i < bar.size(); i++) {
			foobar = new ArrayList<>(Arrays.asList(bar.get(i).split("\\s*::,\\s*")));
			if (i==0 && foobar.size() == 1) {
				// no results
				break;
			}
			parsed_data.add(foobar);
		}
		
		return parsed_data;
	}
	
	public List<Entry> constructEntry(ConnectionManager manager) {
		List<Entry> parsed_data = new ArrayList<>();
		
		String query = manager.send("ViewEntries\f");
		
		parsed_data = parseEntry(query);
		
		return parsed_data;
	}
	
	public List<Entry> constructOpenEntry(ConnectionManager manager) {
		List<Entry> parsed_data = new ArrayList<>();
		
		String query = manager.send("ViewOpenEntries\f");
		
		parsed_data = parseEntry(query);
		
		return parsed_data;
	}
	
	public List<Entry> filterEntry(ConnectionManager manager, String rts) {
		List<Entry> parsed_data = new ArrayList<>();
		
		String query = String.format("FilterEntries\f%s\f", rts);
		
		String result = manager.send(query);
		
		parsed_data = parseEntry(result);
		
		return parsed_data;
	}

	public List<Entry> constructEntryForYear(ConnectionManager manager, int year) {
		List<Entry> parsed_data = new ArrayList<>();
		
		String query = String.format("GenerateReport\f%s\f", year);
		
		String result = manager.send(query);
		
		parsed_data = parseEntry(result);
		
		//System.out.println(String.format("constructEntryForYear: result size is %s", parsed_data.size()));
		return parsed_data;
	}
	
	public List<Entry> constructOpenEntryForYear(ConnectionManager manager, int year) {
		List<Entry> parsed_data = new ArrayList<>();
		
		String query = String.format("GenerateOpenReport\f%s\f", year);
		
		String result = manager.send(query);
		
		parsed_data = parseEntry(result);
		
		//System.out.println(String.format("constructEntryForYear: result size is %s", parsed_data.size()));
		return parsed_data;
	}
	
	public List<Entry> constructEntryForTSC(ConnectionManager manager, int year) {
		List<Entry> parsed_data = new ArrayList<>();
		
		String query = String.format("GenerateReportTSC\f%s\f", year);
		
		String result = manager.send(query);
		
		parsed_data = parseEntry(result);
		
		//System.out.println(String.format("constructEntryForYear: result size is %s", parsed_data.size()));
		return parsed_data;
	}
	
	public List<List<String>> constructEntryForSupplier(ConnectionManager manager, int year) {
		
		String query = String.format("GenerateReportSupplier\f%s\f", year);
		
		String result = manager.send(query);
		
		List<List<String>> parsed_data = parseSupplierSummary(result);
		
		//System.out.println(String.format("constructEntryForYear: result size is %s", parsed_data.size()));
		return parsed_data;
	}
	
	public List<Entry> constructEntryForIndividualSupplier(ConnectionManager manager, int year, String supplier) {
		
		String query = String.format("GenerateReportIndividualSupplier\f%s\f%s\f", year, supplier);
		
		String result = manager.send(query);
		
		List<Entry> parsed_data = parseEntry(result);
		
		return parsed_data;
	}
	
	public List<String> constructSuppliers(ConnectionManager manager) {
		manager.connect();
		String data = manager.send("ViewSuppliers\f");
		manager.disconnect();
		
		return parseSuppliers(data);
	}
	
	public byte[] generateReport(ConnectionManager manager, int inputYear) {
		//gather necessary data
		manager.connect();
		List<List<Entry>> data = classifyByMonth(constructEntryForYear(manager, inputYear));
		manager.disconnect();
		
		return formatReport(data, inputYear);
	}
	
	public byte[] generateReportOpen(ConnectionManager manager, int inputYear) {
		//gather necessary data
		manager.connect();
		List<List<Entry>> data = classifyByMonth(constructOpenEntryForYear(manager, inputYear));
		manager.disconnect();
		
		return formatReport(data, inputYear);
	}
	
	public byte[] generateReportTSC(ConnectionManager manager, int inputYear) {
		//gather necessary data
		manager.connect();
		List<List<Entry>> data = classifyByMonth(constructEntryForTSC(manager, inputYear));
		manager.disconnect();
		
		return formatReport(data, inputYear);
	}
	
	public byte[] generateReportQuarter(ConnectionManager manager, int inputYear) {
		//gather necessary data
		manager.connect();
		List<List<Entry>> data = classifyByQuarter(constructEntryForYear(manager, inputYear));
		manager.disconnect();
				
		return formatReport(data, inputYear);
	}
	
	public byte[] generateReportSupplier(ConnectionManager manager, int inputYear) {
		manager.connect();
		List<List<String>> data = constructEntryForSupplier(manager, inputYear);
		manager.disconnect();
		
		return formatSupplierReport(data, inputYear);
	}
	
	public byte[] generateReportIndividualSupplier(ConnectionManager manager, int inputYear, String supplier) {
		manager.connect();
		List<List<Entry>> data = classifyByMonth(constructEntryForIndividualSupplier(manager, inputYear, supplier));
		manager.disconnect();
		
		return formatReport(data, inputYear);
	}
	
	private byte[] formatReport(List<List<Entry>> data, int inputYear) {
		//track the longest string in each column
		List<Integer> maxNumCharacters = new ArrayList<>(NUMBER_OF_COLUMNS);
		//initialize the lengths
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			maxNumCharacters.add(((int)(15 * 1.14388)) * 256);
		}

		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		//create styles
		HSSFFont defaultFont= workbook.createFont();
		defaultFont.setFontHeightInPoints((short)10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(false);
		defaultFont.setItalic(false);

		HSSFFont font= workbook.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);

		HSSFFont headerFont= workbook.createFont();
		headerFont.setFontHeightInPoints((short)20);
		headerFont.setFontName("Arial");
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		headerFont.setBold(true);
		headerFont.setItalic(false);

		CellStyle headerStyle=workbook.createCellStyle();
		headerStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		//headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);

		CellStyle columnStyle=workbook.createCellStyle();
		columnStyle.setAlignment(HorizontalAlignment.CENTER);
		columnStyle.setBorderLeft(BorderStyle.THIN);
		columnStyle.setBorderRight(BorderStyle.THIN);
		//columnStyle.setBorderTop(BorderStyle.MEDIUM);
		columnStyle.setBorderBottom(BorderStyle.MEDIUM);
		columnStyle.setFont(font);

		CellStyle cellStyle=workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setFont(defaultFont);
		cellStyle.setWrapText(true);

		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
		HSSFCell cell = null;

		for (int m = 0; m < data.size(); m++) {
			//create sheet and set sheet name
			HSSFSheet sheet = workbook.createSheet();
			//assume the arraylist is not empty
			String monthName = data.get(m).get(0).getReportDateLocal().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
			workbook.setSheetName(m, 
					String.format("%s %s RMA", monthName, inputYear));

			//initialize sheet header
			row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellValue(
					String.format("LOGISTICS RMA REPORT FOR %s - %s", monthName, inputYear)
					);
			cell.setCellStyle(headerStyle);

			//initialize column headers
			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellValue("Supplier");

			cell = row.createCell(1);
			cell.setCellValue("Sales Order");

			cell = row.createCell(2);
			cell.setCellValue("Client");

			cell = row.createCell(3);
			cell.setCellValue("Date Received");

			cell = row.createCell(4);
			cell.setCellValue("RTS#");

			cell = row.createCell(5);
			cell.setCellValue("Description");

			cell = row.createCell(6);
			cell.setCellValue("Serial#");

			cell = row.createCell(7);
			cell.setCellValue("Date Reported");

			cell = row.createCell(8);
			cell.setCellValue("Quantity Received");

			cell = row.createCell(9);
			cell.setCellValue("Problem");

			cell = row.createCell(10);
			cell.setCellValue("Date Pulled Out");

			cell = row.createCell(11);
			cell.setCellValue("Date Returned");

			cell = row.createCell(12);
			cell.setCellValue("Non-Working Days");

			cell = row.createCell(13);
			cell.setCellValue("Days for Turnaround");

			cell = row.createCell(14);
			cell.setCellValue("Quantity Remaining");

			cell = row.createCell(15);
			cell.setCellValue("POS Ref.");

			cell = row.createCell(16);
			cell.setCellValue("RTC Ref.");

			cell = row.createCell(17);
			cell.setCellValue("Quantity Returned");

			cell = row.createCell(18);
			cell.setCellValue("New Serial#");

			cell = row.createCell(19);
			cell.setCellValue("Remarks");

			cell = row.createCell(20);
			cell.setCellValue("Status");

			cell = row.createCell(21);
			cell.setCellValue("Aging");

			cell = row.createCell(22);
			cell.setCellValue("Entry ID#");

			cell = row.createCell(23);
			cell.setCellValue("Parent ID#");

			for (int i = 0; i<NUMBER_OF_COLUMNS; i++) {
				row.getCell(i).setCellStyle(columnStyle);
			}

			sheet.createFreezePane(0, 2);
			//end initialize header//

			//write each row into the book

			for (int i = 0; i < data.get(m).size(); i++) {
				Entry dataRow = data.get(m).get(i);

				row = sheet.createRow(i+2);

				cell = row.createCell(0);
				cell.setCellValue(dataRow.getSupplier());
				maxNumCharacters.set(0, 
						(dataRow.getSupplier().length() > maxNumCharacters.get(0)) ? 
								dataRow.getSupplier().length() : maxNumCharacters.get(0)
						);

				cell = row.createCell(1);
				cell.setCellValue(dataRow.getSalesOrder());
				maxNumCharacters.set(1, 
						(dataRow.getSalesOrder().length() > maxNumCharacters.get(1)) ? 
								dataRow.getSalesOrder().length() : maxNumCharacters.get(1)
						);

				cell = row.createCell(2);
				cell.setCellValue(dataRow.getClient());
				maxNumCharacters.set(2, 
						(dataRow.getClient().length() > maxNumCharacters.get(2)) ? 
								dataRow.getClient().length() : maxNumCharacters.get(2)
						);

				cell = row.createCell(3);
				cell.setCellValue(dataRow.getReceiveDate().toString());
				maxNumCharacters.set(3, 
						(dataRow.getReceiveDate().toString().length() > maxNumCharacters.get(3)) ? 
								dataRow.getReceiveDate().toString().length() : maxNumCharacters.get(3)
						);

				cell = row.createCell(4);
				cell.setCellValue(dataRow.getRts());
				maxNumCharacters.set(4, 
						(dataRow.getRts().length() > maxNumCharacters.get(4)) ? 
								dataRow.getRts().length() : maxNumCharacters.get(4)
						);

				cell = row.createCell(5);
				cell.setCellValue(dataRow.getDescription());
				maxNumCharacters.set(5, 
						(dataRow.getDescription().length() > maxNumCharacters.get(5)) ? 
								dataRow.getDescription().length() : maxNumCharacters.get(5)
						);

				cell = row.createCell(6);
				cell.setCellValue(dataRow.getSerial());
				maxNumCharacters.set(6, 
						(dataRow.getSerial().length() > maxNumCharacters.get(6)) ? 
								dataRow.getSerial().length() : maxNumCharacters.get(6)
						);

				cell = row.createCell(7);
				cell.setCellValue(dataRow.getReportDate().toString());
				maxNumCharacters.set(7, 
						(dataRow.getReportDate().toString().length() > maxNumCharacters.get(7)) ? 
								dataRow.getReportDate().toString().length() : maxNumCharacters.get(7)
						);

				cell = row.createCell(8);
				cell.setCellValue(dataRow.getQuantity());
				maxNumCharacters.set(8, 
						(dataRow.getQuantityStr().length() > maxNumCharacters.get(8)) ? 
								dataRow.getQuantityStr().length() : maxNumCharacters.get(8)
						);

				cell = row.createCell(9);
				cell.setCellValue(dataRow.getProblem());
				maxNumCharacters.set(9, 
						(dataRow.getProblem().length() > maxNumCharacters.get(9)) ? 
								dataRow.getProblem().length() : maxNumCharacters.get(9)
						);

				cell = row.createCell(10);
				cell.setCellValue(dataRow.getPullOutDate().toString());
				maxNumCharacters.set(10, 
						(dataRow.getPullOutDate().toString().length() > maxNumCharacters.get(10)) ? 
								dataRow.getPullOutDate().toString().length() : maxNumCharacters.get(10)
						);

				cell = row.createCell(11);
				cell.setCellValue(dataRow.getReturnDate().toString());
				maxNumCharacters.set(11, 
						(dataRow.getReturnDate().toString().length() > maxNumCharacters.get(11)) ? 
								dataRow.getReturnDate().toString().length() : maxNumCharacters.get(11)
						);

				cell = row.createCell(12);
				cell.setCellValue(dataRow.getNonWorkingDays());
				maxNumCharacters.set(12, 
						(dataRow.getNonWorkingDaysStr().length() > maxNumCharacters.get(12)) ? 
								dataRow.getNonWorkingDaysStr().length() : maxNumCharacters.get(12)
						);

				cell = row.createCell(13);
				cell.setCellValue(dataRow.getTurnaround());
				maxNumCharacters.set(13, 
						(dataRow.getTurnaroundStr().length() > maxNumCharacters.get(13)) ? 
								dataRow.getTurnaroundStr().length() : maxNumCharacters.get(13)
						);

				cell = row.createCell(14);
				cell.setCellValue(dataRow.getQuantityRemaining());
				maxNumCharacters.set(14, 
						(dataRow.getQuantityRemainingStr().length() > maxNumCharacters.get(14)) ? 
								dataRow.getQuantityRemainingStr().length() : maxNumCharacters.get(14)
						);

				cell = row.createCell(15);
				cell.setCellValue(dataRow.getPos());
				maxNumCharacters.set(15, 
						(dataRow.getPos().length() > maxNumCharacters.get(15)) ? 
								dataRow.getPos().length() : maxNumCharacters.get(15)
						);

				cell = row.createCell(16);
				cell.setCellValue(dataRow.getRtc());
				maxNumCharacters.set(16, 
						(dataRow.getRtc().length() > maxNumCharacters.get(16)) ? 
								dataRow.getRtc().length() : maxNumCharacters.get(16)
						);

				cell = row.createCell(17);
				cell.setCellValue(dataRow.getQuantityReturned());
				maxNumCharacters.set(17, 
						(dataRow.getQuantityReturnedStr().length() > maxNumCharacters.get(17)) ? 
								dataRow.getQuantityReturnedStr().length() : maxNumCharacters.get(17)
						);

				cell = row.createCell(18);
				cell.setCellValue(dataRow.getNewSerial());
				maxNumCharacters.set(18, 
						(dataRow.getNewSerial().length() > maxNumCharacters.get(18)) ? 
								dataRow.getNewSerial().length() : maxNumCharacters.get(18)
						);

				cell = row.createCell(19);
				cell.setCellValue(dataRow.getRemarks());
				maxNumCharacters.set(19, 
						(dataRow.getRemarks().length() > maxNumCharacters.get(19)) ? 
								dataRow.getRemarks().length() : maxNumCharacters.get(19)
						);

				cell = row.createCell(20);
				cell.setCellValue(dataRow.getStatus());
				maxNumCharacters.set(20, 
						(dataRow.getStatus().length() > maxNumCharacters.get(20)) ? 
								dataRow.getStatus().length() : maxNumCharacters.get(20)
						);

				cell = row.createCell(21);
				cell.setCellValue(dataRow.getAging());
				maxNumCharacters.set(21, 
						(String.valueOf(dataRow.getAging()).length() > maxNumCharacters.get(21)) ? 
								String.valueOf(dataRow.getAging()).length() : maxNumCharacters.get(21)
						);

				cell = row.createCell(22);
				cell.setCellValue(dataRow.getEntryID());

				cell = row.createCell(23);
				cell.setCellValue(dataRow.getTrace());

				if (i > 0) {
					cellStyle.setBorderTop(BorderStyle.THIN);
				}
				for (int j = 0; j<NUMBER_OF_COLUMNS; j++) {
					row.getCell(j).setCellStyle(cellStyle);
				}
			}
			//DEBUG
			//reset the column widths
			for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
				maxNumCharacters.set(i, ((int)(15 * 1.14388)) * 256);
			}

			//resize the columns
			for (int k = 0; k < NUMBER_OF_COLUMNS; k ++) {
				int width = ((int)(maxNumCharacters.get(k) * 1.14388));
				sheet.setColumnWidth(k, width);
			}

		}
		//end of loop//

		try {
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	
	private byte[] formatSupplierReport(List<List<String>> data, int inputYear) {
		//function final output stored here
		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		//create the workbook
		HSSFWorkbook workbook = new HSSFWorkbook();

		//create styles
		HSSFFont defaultFont= workbook.createFont();
		defaultFont.setFontHeightInPoints((short)10);
		defaultFont.setFontName("Arial");
		defaultFont.setColor(IndexedColors.BLACK.getIndex());
		defaultFont.setBold(false);
		defaultFont.setItalic(false);

		HSSFFont font= workbook.createFont();
		font.setFontHeightInPoints((short)10);
		font.setFontName("Arial");
		font.setColor(IndexedColors.BLACK.getIndex());
		font.setBold(true);
		font.setItalic(false);

		CellStyle columnStyle=workbook.createCellStyle();
		columnStyle.setAlignment(HorizontalAlignment.CENTER);
		columnStyle.setBorderLeft(BorderStyle.THIN);
		columnStyle.setBorderRight(BorderStyle.THIN);
		//columnStyle.setBorderTop(BorderStyle.MEDIUM);
		columnStyle.setBorderBottom(BorderStyle.MEDIUM);
		columnStyle.setFont(font);

		CellStyle cellStyle=workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setFont(defaultFont);
		cellStyle.setWrapText(true);

		//use these to refer to rows and cells in iterations
		HSSFRow row = null;
		HSSFCell cell = null;
		
		//write the contents
		HSSFSheet sheet = workbook.createSheet();
		
		//write the first row
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("Supplier");
		
		cell = row.createCell(1);
		cell.setCellValue("Total # of RMA");
		
		cell = row.createCell(2);
		cell.setCellValue("Percentage of RMA");
		
		for (int i = 0; i<3; i++) {
			row.getCell(i).setCellStyle(columnStyle);
		}

		sheet.createFreezePane(0, 1);
		
		//write the rest of the rows
		for (int i = 0; i < data.size(); i++) {
			row = sheet.createRow(i+1);
			
			cell = row.createCell(0);
			cell.setCellValue(data.get(i).get(0));
			
			cell = row.createCell(1);
			cell.setCellValue(data.get(i).get(1));
			
			cell = row.createCell(2);
			cell.setCellValue(data.get(i).get(2));
			
			if (i > 0) {
				cellStyle.setBorderTop(BorderStyle.THIN);
			}
			for (int j = 0; j<3; j++) {
				row.getCell(j).setCellStyle(cellStyle);
			}
		}
		
		//resize the columns
		for (int k = 0; k < 3; k ++) {
			sheet.setColumnWidth(k, ((int)(20 * 1.14388)) * 256);
		}
		
		try {
			workbook.write(bos);
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
	

	private List<List<Entry>> classifyByMonth(List<Entry> list) {
		List<List<Entry>> result = new ArrayList<>();
		//holds the input
		List<Entry> foo = list;
		//holds the sublist
		List<Entry> bar = null;
		
		//segregate all content
		while (!foo.isEmpty()) {
			//reset the dummy list
			bar = new ArrayList<>();
			
			for (Entry entry : foo) {
				if (bar.isEmpty()) {
					//happens only once
					bar.add(entry);
				}
				//assume list is not empty, group same months together
				else if (entry.getReportDateLocal().getMonth().getValue() ==
						bar.get(0).getReportDateLocal().getMonth().getValue()) {
					bar.add(entry);
				}
			}
			
			foo.removeAll(bar);
			result.add(bar);
		}
		
		//System.out.println(String.format("classifyByMonth: result size is %s", result.size()));
		return result;
	}
	
	private List<List<Entry>> classifyByQuarter(List<Entry> list) {
		List<List<Entry>> result = new ArrayList<>();
		//holds the input
		List<Entry> foo = list;
		//holds the sublist
		List<Entry> bar = null;
		
		//segregate all content
		for (int i = 0; i < 4; i++) {
			bar = new ArrayList<>();
			
			for (Entry entry : foo) {
				if (entry.getReportDateLocal().getMonth().getValue() >= 1 + i * 3 &&
						entry.getReportDateLocal().getMonth().getValue() <= 3 + i * 3) {
					bar.add(entry);
				}
			}
			
			foo.removeAll(bar);
			if (!bar.isEmpty()) result.add(bar);
		}
		
		//System.out.println(String.format("classifyByQuarter: result size is %s", result.size()));
		return result;
	}
}
