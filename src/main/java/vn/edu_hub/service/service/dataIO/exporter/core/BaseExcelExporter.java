package vn.edu_hub.service.service.dataIO.exporter.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Lớp cơ sở cho việc xuất Excel dạng streaming sử dụng Apache POI SXSSF.
 * <p>
 * Lớp con định nghĩa tiêu đề cột và cách ghi từng dòng dữ liệu.
 * Việc tạo style được uỷ quyền cho {@link ExcelHeaderStyleProvider} (SRP).
 * Lớp con có thể ghi đè các hook method để tuỳ chỉnh hành vi (OCP).
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
@Slf4j
public abstract class BaseExcelExporter<T> implements ExcelExporter<T> {

    private static final int DEFAULT_WINDOW_SIZE = 500;

    private final ExcelHeaderStyleProvider headerStyleProvider;
    private int[] maxColumnWidths;

    protected BaseExcelExporter() {
        this.headerStyleProvider = new DefaultHeaderStyleProvider();
    }

    protected BaseExcelExporter(ExcelHeaderStyleProvider headerStyleProvider) {
        this.headerStyleProvider = headerStyleProvider;
    }

    /**
     * Tên các cột tiêu đề cho dòng đầu tiên.
     */
    protected abstract String[] getHeaders();

    /**
     * Tên sheet. Ghi đè để tuỳ chỉnh.
     */
    protected String getSheetName() {
        return "Sheet1";
    }

    /**
     * Ghi một đối tượng dữ liệu vào dòng. Sử dụng {@code row.createCell(index)} để điền dữ liệu.
     */
    protected abstract void writeRow(T item, Row row);

    /**
     * Hook được gọi sau khi tạo sheet nhưng trước khi ghi bất kỳ dòng nào.
     * Ghi đè để thêm cấu hình cho sheet.
     */
    protected void onSheetCreated(SXSSFSheet sheet) {
        // mặc định: không làm gì
    }

    /**
     * Hook được gọi sau khi ghi xong tất cả dữ liệu nhưng trước khi flush workbook.
     * Ghi đè để xử lý sau (ví dụ: dòng tổng hợp, công thức).
     */
    protected void onDataWritten(SXSSFSheet sheet, List<T> data) {
        // mặc định: không làm gì
    }

    @Override
    public void export(List<T> data, OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE)) {
            SXSSFSheet sheet = initSheet(workbook);

            writeDataRows(sheet, data);
            onDataWritten(sheet, data);
            applyColumnWidths(sheet);

            workbook.write(outputStream);
        }
    }

    @Override
    public void export(Stream<T> dataStream, OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE)) {
            SXSSFSheet sheet = initSheet(workbook);

            writeDataRows(sheet, dataStream);
            applyColumnWidths(sheet);

            workbook.write(outputStream);
        }
    }

    private SXSSFSheet initSheet(SXSSFWorkbook workbook) {
        SXSSFSheet sheet = workbook.createSheet(getSheetName());
        maxColumnWidths = new int[getHeaders().length];
        onSheetCreated(sheet);

        CellStyle headerStyle = headerStyleProvider.createHeaderStyle(workbook);
        writeHeaderRow(sheet, headerStyle);
        return sheet;
    }

    private void writeHeaderRow(SXSSFSheet sheet, CellStyle headerStyle) {
        Row headerRow = sheet.createRow(0);
        String[] headers = getHeaders();
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        trackColumnWidths(headerRow);
    }

    private void writeDataRows(SXSSFSheet sheet, List<T> data) {
        int rowIndex = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowIndex++);
            writeRow(item, row);
            trackColumnWidths(row);
        }
    }

    private void writeDataRows(SXSSFSheet sheet, Stream<T> dataStream) {
        AtomicInteger rowIndex = new AtomicInteger(1);
        dataStream.forEach(item -> {
            Row row = sheet.createRow(rowIndex.getAndIncrement());
            writeRow(item, row);
            trackColumnWidths(row);
        });
    }

    /**
     * Theo dõi độ rộng tối đa của từng cột dựa trên nội dung thực tế,
     * thay vì dùng autoSizeColumn (chỉ tính trên các dòng còn trong memory window).
     */
    private void trackColumnWidths(Row row) {
        for (int i = 0; i < maxColumnWidths.length; i++) {
            Cell cell = row.getCell(i);
            if (cell != null) {
                int length = cell.toString().length();
                if (length > maxColumnWidths[i]) {
                    maxColumnWidths[i] = length;
                }
            }
        }
    }

    /**
     * Áp dụng độ rộng cột dựa trên dữ liệu đã theo dõi.
     * Mỗi đơn vị = 1/256 ký tự, thêm padding 3 ký tự, giới hạn tối đa 255 ký tự (giới hạn Excel).
     */
    private void applyColumnWidths(SXSSFSheet sheet) {
        for (int i = 0; i < maxColumnWidths.length; i++) {
            int width = (maxColumnWidths[i] + 3) * 256;
            sheet.setColumnWidth(i, Math.min(width, 255 * 256));
        }
    }
}
