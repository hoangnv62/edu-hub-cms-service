package vn.edu_hub.service.service.dataIO.exporter.core;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Xuất Excel theo stream — tiêu thụ dữ liệu từng phần tử qua Stream.
 * Phù hợp cho tập dữ liệu lớn (>10.000 dòng), tránh load toàn bộ vào RAM.
 * Stream được tự động đóng sau khi export xong.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
public abstract class StreamingExcelExporter<T> extends BaseExcelExporter<T>
        implements IStreamingExcelExporter<T> {

    protected StreamingExcelExporter() {
        super();
    }

    protected StreamingExcelExporter(ExcelHeaderStyleProvider headerStyleProvider) {
        super(headerStyleProvider);
    }

    /**
     * Hook được gọi sau khi ghi xong tất cả dữ liệu từ stream.
     */
    protected void onDataWritten(SXSSFSheet sheet) {
        // mặc định: không làm gì
    }

    @Override
    public void export(Stream<T> dataStream, OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_WINDOW_SIZE);
             Stream<T> stream = dataStream) {
            SXSSFSheet sheet = initSheet(workbook);

            AtomicInteger rowIndex = new AtomicInteger(1);
            stream.forEachOrdered(item ->
                    writeDataRow(sheet, item, rowIndex.getAndIncrement())
            );

            onDataWritten(sheet);
            applyColumnWidths(sheet);
            workbook.write(outputStream);
        }
    }
}
