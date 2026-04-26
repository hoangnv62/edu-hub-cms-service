package vn.edu_hub.service.service.dataIO.exporter.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Interface xuất dữ liệu ra Excel theo batch (List).
 * Phù hợp cho tập dữ liệu nhỏ đến trung bình, load toàn bộ vào RAM.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
public interface IBatchExcelExporter<T> {

    void export(List<T> data, OutputStream outputStream) throws IOException;
}
