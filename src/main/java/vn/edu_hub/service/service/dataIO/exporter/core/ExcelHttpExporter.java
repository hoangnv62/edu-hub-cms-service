package vn.edu_hub.service.service.dataIO.exporter.core;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

/**
 * Tiện ích xuất Excel qua HTTP response.
 * Tuân thủ SRP: tách biệt logic truyền tải HTTP khỏi logic tạo Excel.
 */
public class ExcelHttpExporter {

    private static final String CONTENT_TYPE =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * Xuất dữ liệu theo batch ra HTTP response.
     */
    public <T> void export(IBatchExcelExporter<T> exporter, List<T> data,
                           String fileName, HttpServletResponse response) throws IOException {
        prepareResponse(fileName, response);
        try (OutputStream out = response.getOutputStream()) {
            exporter.export(data, out);
            out.flush();
        }
    }

    /**
     * Xuất dữ liệu theo stream ra HTTP response. Phù hợp cho tập dữ liệu lớn.
     */
    public <T> void export(IStreamingExcelExporter<T> exporter, Stream<T> dataStream,
                           String fileName, HttpServletResponse response) throws IOException {
        prepareResponse(fileName, response);
        try (OutputStream out = response.getOutputStream()) {
            exporter.export(dataStream, out);
            out.flush();
        }
    }

    private void prepareResponse(String fileName, HttpServletResponse response) {
        String safeFileName = sanitizeFileName(fileName);
        String encodedFileName = URLEncoder.encode(safeFileName, StandardCharsets.UTF_8)
                .replace("+", "%20");
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + safeFileName + "\"; filename*=UTF-8''" + encodedFileName);
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
