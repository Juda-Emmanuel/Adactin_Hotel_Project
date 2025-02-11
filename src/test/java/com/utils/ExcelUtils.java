package com.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	public static Object[][] getExcelData(String filePath, String sheetName) {
		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {

			// Get the specified sheet from the workbook
			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new RuntimeException("❌ Sheet '" + sheetName + "' not found in file: " + filePath);
			}

			// Get the number of columns in the first row (header)
			int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
			List<Object[]> dataList = new ArrayList<>();

			// Iterate through rows, skipping the header row
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next(); // Skip header

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Object[] rowData = new Object[colCount];
				boolean isRowEmpty = true;

				// Iterate through columns in the row
				for (int j = 0; j < colCount; j++) {
					Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
					rowData[j] = getCellValue(cell);

					if (cell != null && cell.getCellType() != CellType.BLANK) {
						isRowEmpty = false;
					}
				}

				// Add non-empty rows to the data list
				if (!isRowEmpty) {
					dataList.add(rowData);
				}
			}

			System.out.println("✅ Successfully read data from Excel sheet: " + sheetName);
			return dataList.toArray(new Object[0][]);

		} catch (IOException e) {
			System.err.println("❌ Failed to read Excel file: " + filePath);
			e.printStackTrace();
			throw new RuntimeException("Excel file read error: " + filePath, e);
		}
	}

	private static Object getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		// Determine cell type and return appropriate value
		switch (cell.getCellType()) {
		case STRING:
			return cell.getStringCellValue().trim();

		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
			}
			return (cell.getNumericCellValue() % 1 == 0) ? String.valueOf((long) cell.getNumericCellValue())
					: String.valueOf(cell.getNumericCellValue());

		case BOOLEAN:
			return cell.getBooleanCellValue();

		case FORMULA:
			return evaluateFormula(cell);

		case BLANK:
			return "";

		default:
			return "UNKNOWN";
		}
	}

	private static Object evaluateFormula(Cell cell) {
		try {
			FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
			CellValue cellValue = evaluator.evaluate(cell);

			switch (cellValue.getCellType()) {
			case STRING:
				return cellValue.getStringValue();
			case NUMERIC:
				return (cellValue.getNumberValue() % 1 == 0) ? String.valueOf((long) cellValue.getNumberValue())
						: String.valueOf(cellValue.getNumberValue());
			case BOOLEAN:
				return cellValue.getBooleanValue();
			default:
				return "FORMULA_ERROR";
			}
		} catch (Exception e) {
			System.err.println("⚠️ Error evaluating formula in cell: " + cell.getAddress());
			return "ERROR_EVALUATING_FORMULA";
		}
	}
}
