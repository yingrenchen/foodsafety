package org.iii.ideas.foodsafety.rest;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHandler {
	private static final Logger logger = LogManager.getLogger(AFS_ini.class);

	public static String[][] getData(String fileName) throws IOException {
		logger.info("getData");
		FileInputStream fis = new FileInputStream(fileName);
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(fis);
		XSSFRow row;
		// XSSFSheet sheet = workbook.getSheetAt(workbook.getNumberOfSheets() -
		// 1);
		XSSFSheet sheet = workbook.getSheetAt(workbook.getFirstVisibleTab());
		String[][] result = new String[sheet.getLastRowNum()-4][4];
		for (int i = 2; i < sheet.getLastRowNum() -2; i++) {
			// System.out.println(sheet.getPhysicalNumberOfRows());
			int q = 0;
			row = sheet.getRow(i); // 取得第 i Row
			for (int j = 0; j < row.getLastCellNum(); j++) {
				if (j == 1 || j == 9 || j == 5 || j == 6) {
					if (j == 6) {
						// System.out.println(row.getCell(j).toString().split("/")[0]);
						result[i - 2][q] = row.getCell(j).toString().split("/")[0];
					} else {
						// System.out.println(row.getCell(j).toString());
						result[i - 2][q] = row.getCell(j).toString();

					}
					q++;

				}

			}
		}
		return result;
	}

}
