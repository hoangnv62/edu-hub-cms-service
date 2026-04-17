package vn.edu_hub.service.service.dataIO.exporter.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * Interface xuất dữ liệu ra định dạng Excel.
 * Tuân thủ ISP: client chỉ phụ thuộc vào khả năng export cần thiết.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
public interface ExcelExporter<T> {

    /**
     * Xuất toàn bộ dữ liệu ra Excel. Phù hợp cho tập dữ liệu nhỏ.
     */
    void export(List<T> data, OutputStream outputStream) throws IOException;

    /**
     * Xuất dữ liệu ra Excel theo stream. Phù hợp cho tập dữ liệu lớn,
     * chỉ giữ từng phần tử trong RAM khi ghi.
     */
    void export(Stream<T> dataStream, OutputStream outputStream) throws IOException;
}
