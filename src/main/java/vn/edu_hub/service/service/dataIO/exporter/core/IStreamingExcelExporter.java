package vn.edu_hub.service.service.dataIO.exporter.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Stream;

/**
 * Interface xuất dữ liệu ra Excel theo stream.
 * Phù hợp cho tập dữ liệu lớn (>10.000 dòng), chỉ giữ từng phần tử trong RAM khi ghi.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
public interface IStreamingExcelExporter<T> {

    void export(Stream<T> dataStream, OutputStream outputStream) throws IOException;
}
