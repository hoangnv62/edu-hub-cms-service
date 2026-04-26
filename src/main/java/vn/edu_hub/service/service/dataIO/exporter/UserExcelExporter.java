package vn.edu_hub.service.service.dataIO.exporter;

import org.apache.poi.ss.usermodel.Row;
import vn.edu_hub.service.dto.response.UserResponseDTO;
import vn.edu_hub.service.service.dataIO.exporter.core.StreamingExcelExporter;

/**
 * Exporter xuất danh sách người dùng ra file Excel.
 */
public class UserExcelExporter extends StreamingExcelExporter<UserResponseDTO> {
    private static final String[] HEADERS = {"STT", "ID", "Tên đăng nhập", "Họ và tên", "Vai trò"};
    private static final int[] COLUMN_WIDTHS = {5, 10, 20, 25, 15};

    @Override
    protected String[] getHeaders() {
        return HEADERS;
    }

    @Override
    protected String getSheetName() {
        return "Danh sách người dùng";
    }

    @Override
    protected int[] getColumnWidths() {
        return COLUMN_WIDTHS;
    }

    @Override
    protected void writeRow(UserResponseDTO user, Row row, int rowIndex) {
        int col = 0;
        setCellValue(row, col++, rowIndex);
        setCellValue(row, col++, user.getId());
        setCellValue(row, col++, user.getUsername());
        setCellValue(row, col++, user.getFullName());
        setCellValue(row, col, user.getRole());
    }
}
