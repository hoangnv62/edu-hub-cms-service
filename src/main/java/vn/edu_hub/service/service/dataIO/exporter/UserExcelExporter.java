package vn.edu_hub.service.service.dataIO.exporter;

import org.apache.poi.ss.usermodel.Row;
import vn.edu_hub.service.dto.response.UserResponseDTO;
import vn.edu_hub.service.service.dataIO.exporter.core.BaseExcelExporter;

/**
 * Exporter xuất danh sách người dùng ra file Excel.
 */
public class UserExcelExporter extends BaseExcelExporter<UserResponseDTO> {

    private static final String[] HEADERS = {"ID", "Tên đăng nhập", "Họ và tên", "Vai trò"};

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected String getSheetName() {
        return "Danh sách người dùng";
    }

    @Override
    protected void writeRow(UserResponseDTO user, Row row) {
        row.createCell(0).setCellValue(user.getId());
        row.createCell(1).setCellValue(user.getUsername());
        row.createCell(2).setCellValue(user.getFullName());
        row.createCell(3).setCellValue(user.getRole());
    }
}
