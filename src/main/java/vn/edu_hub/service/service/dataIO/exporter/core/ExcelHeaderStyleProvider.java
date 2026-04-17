package vn.edu_hub.service.service.dataIO.exporter.core;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Strategy tạo style cho ô tiêu đề.
 * Tuân thủ SRP: tách biệt logic styling khỏi logic export.
 */
public interface ExcelHeaderStyleProvider {

    CellStyle createHeaderStyle(SXSSFWorkbook workbook);
}
