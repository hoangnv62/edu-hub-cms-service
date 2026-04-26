package vn.edu_hub.service.service.dataIO.exporter.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 * Lớp cơ sở chứa logic dùng chung cho việc xuất Excel (SXSSF streaming).
 * <p>
 * Lớp con định nghĩa tiêu đề cột, cách ghi từng dòng, và độ rộng cột.
 * Việc tạo style được uỷ quyền cho {@link ExcelHeaderStyleProvider} (SRP).
 * <p>
 * Không chứa method export — logic export nằm ở {@link BatchExcelExporter}
 * và {@link StreamingExcelExporter}.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
@Slf4j
public abstract class BaseExcelExporter<T> {

    protected static final int DEFAULT_WINDOW_SIZE = 500;

    private final ExcelHeaderStyleProvider headerStyleProvider;

    protected BaseExcelExporter() {
        this.headerStyleProvider = new DefaultHeaderStyleProvider();
    }

    protected BaseExcelExporter(ExcelHeaderStyleProvider headerStyleProvider) {
        this.headerStyleProvider = headerStyleProvider;
    }

    /**
     * Tên các cột tiêu đề (bao gồm cả STT nếu cần).
     */
    protected abstract String[] getHeaders();

    /**
     * Tên sheet. Ghi đè để tuỳ chỉnh.
     */
    protected String getSheetName() {
        return "Sheet1";
    }

    /**
     * Ghi một đối tượng dữ liệu vào dòng.
     *
     * @param item     đối tượng dữ liệu
     * @param row      dòng Excel
     * @param rowIndex số thứ tự dòng (bắt đầu từ 1), dùng cho cột STT nếu cần
     */
    protected abstract void writeRow(T item, Row row, int rowIndex);

    /**
     * Độ rộng các cột (đơn vị: số ký tự).
     * Mặc định tính từ độ dài text header. Ghi đè để tuỳ chỉnh.
     */
    protected int[] getColumnWidths() {
        String[] headers = getHeaders();
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
        }
        return widths;
    }

    /**
     * Hook được gọi sau khi tạo sheet nhưng trước khi ghi bất kỳ dòng nào.
     */
    protected void onSheetCreated(SXSSFSheet sheet) {
        // mặc định: không làm gì
    }

    /**
     * Ghi giá trị String vào cell, tự động xử lý null thành chuỗi rỗng.
     */
    protected void setCellValue(Row row, int index, String value) {
        row.createCell(index).setCellValue(value != null ? value : "");
    }

    /**
     * Ghi giá trị Number vào cell, tự động xử lý null thành 0.
     */
    protected void setCellValue(Row row, int index, Number value) {
        row.createCell(index).setCellValue(value != null ? value.doubleValue() : 0);
    }

    /**
     * Tạo sheet với header row.
     */
    protected SXSSFSheet initSheet(SXSSFWorkbook workbook) {
        SXSSFSheet sheet = workbook.createSheet(getSheetName());
        onSheetCreated(sheet);

        CellStyle headerStyle = headerStyleProvider.createHeaderStyle(workbook);
        writeHeaderRow(sheet, headerStyle);
        return sheet;
    }

    /**
     * Ghi một dòng dữ liệu.
     */
    protected void writeDataRow(SXSSFSheet sheet, T item, int rowIndex) {
        Row row = sheet.createRow(rowIndex);
        writeRow(item, row, rowIndex);
    }

    /**
     * Áp dụng độ rộng cột từ {@link #getColumnWidths()}.
     */
    protected void applyColumnWidths(SXSSFSheet sheet) {
        int[] columnWidths = getColumnWidths();
        for (int i = 0; i < columnWidths.length; i++) {
            int width = (columnWidths[i] + 3) * 256;
            sheet.setColumnWidth(i, Math.min(width, 255 * 256));
        }
    }

    private void writeHeaderRow(SXSSFSheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        String[] headers = getHeaders();
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }
}
