package vn.edu_hub.service.service.dataIO.exporter.core;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * Chuyển đổi bất kỳ {@link ExcelExporter} nào để ghi trực tiếp vào HTTP response.
 * Tuân thủ SRP: tách biệt logic truyền tải HTTP khỏi logic tạo Excel.
 *
 * @param <T> kiểu dữ liệu của mỗi dòng
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExcelHttpExporter<T> {

    private static final String CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    ExcelExporter<T> excelExporter;

    public void export(List<T> data, String fileName, HttpServletResponse response) throws IOException {
        prepareResponse(fileName, response);
        try (OutputStream out = response.getOutputStream()) {
            excelExporter.export(data, out);
            out.flush();
        }
    }

    /**
     * Xuất dữ liệu ra HTTP response theo stream. Phù hợp cho tập dữ liệu lớn.
     */
    public void export(Stream<T> dataStream, String fileName, HttpServletResponse response) throws IOException {
        prepareResponse(fileName, response);
        try (OutputStream out = response.getOutputStream()) {
            excelExporter.export(dataStream, out);
            out.flush();
        }
    }

    private void prepareResponse(String fileName, HttpServletResponse response) {
        String safeFileName = sanitizeFileName(fileName);
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + safeFileName + "\"");
    }

    /**
     * Loại bỏ các ký tự có thể gây ra HTTP header injection hoặc lỗi hệ thống file.
     */
    private String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return "export.xlsx";
        }
        return fileName.replaceAll("[\\r\\n\\t\"\\\\/:*?<>|]", "_");
    }
}
