package vn.edu_hub.service.service.dataIO.exporter.core;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Xuất Excel theo batch — nhận toàn bộ dữ liệu dạng List.
 * Phù hợp cho tập dữ liệu nhỏ đến trung bình.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
public abstract class BatchExcelExporter<T> extends BaseExcelExporter<T>
        implements IBatchExcelExporter<T> {

    protected BatchExcelExporter() {
        super();
    }

    protected BatchExcelExporter(ExcelHeaderStyleProvider headerStyleProvider) {
        super(headerStyleProvider);
    }

    /**
     * Hook được gọi sau khi ghi xong tất cả dữ liệu.
     * Ghi đè để xử lý sau (ví dụ: dòng tổng hợp, công thức).
     */
    protected void onDataWritten(SXSSFSheet sheet, List<T> data) {
        // mặc định: không làm gì
    }

    @Override
    public void export(List<T> data, OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE)) {
            SXSSFSheet sheet = initSheet(workbook);

            int rowIndex = 1;
            for (T item : data) {
                writeDataRow(sheet, item, rowIndex++);
            }

            onDataWritten(sheet, data);
            applyColumnWidths(sheet);
            workbook.write(outputStream);
        }
    }
}
