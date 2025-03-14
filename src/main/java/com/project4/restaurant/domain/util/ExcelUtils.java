package com.project4.restaurant.domain.util;

import com.google.common.collect.Lists;
import com.project4.restaurant.domain.core.exception.BadRequestException;
import com.project4.restaurant.domain.core.exception.base.ErrorMessage;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Log4j2
@Component
public class ExcelUtils {
  public static final int COLUMN_ID = 0;
  public static final int COLUMN_SET_CODE = 1;
  public static final int COLUMN_QUESTION_CODE = 2;
  public static final int COLUMN_NAME = 3;
  public static final int COLUMN_QUESTION = 4;
  public static final int COLUMN_RIGHT_ANSWER = 5;
  public static final int COLUMN_TIME_ANSWER = 6;
  public static final int COLUMN_ANSWER_1 = 7;
  public static final int COLUMN_ASSET_1 = 8;
  public static final int COLUMN_ANSWER_2 = 9;
  public static final int COLUMN_ASSET_2 = 10;
  public static final int COLUMN_ANSWER_3 = 11;
  public static final int COLUMN_ASSET_3 = 12;
  public static final int COLUMN_ANSWER_4 = 13;
  public static final int COLUMN_ASSET_4 = 14;
  public static final int ADD_POINT = 15;
  public static final int MINUS_POINT = 16;
  public static final int MESSAGE = 17;
  protected static final List<String> LIST_EXCEL_TYPE = Lists.newArrayList("application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

  public static boolean isExcelFile(MultipartFile file) {
    return LIST_EXCEL_TYPE.contains(file.getContentType());
  }

  public static Workbook readToWorkbook(MultipartFile file) throws IOException {
    if (file == null) {
      throw new BadRequestException(ErrorMessage.IMPORT_FILE_EXCEL_INVALID);
    }
    String contentType = file.getContentType();
    if (contentType == null) {
      throw new BadRequestException(ErrorMessage.IMPORT_FILE_EXCEL_INVALID);
    }
    if (contentType.equals("application/vnd.ms-excel")) {//xls
      return new HSSFWorkbook(file.getInputStream());
    }
    if (contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {//xlsx
      return new XSSFWorkbook(file.getInputStream());
    }
    throw new BadRequestException(ErrorMessage.IMPORT_FILE_EXCEL_INVALID);
  }

  public static <T> List<T> importExcel(MultipartFile file, String[] headers, Class<T> clazz) throws IOException {
    List<T> resultList = new ArrayList<>();
    Workbook workbook = new XSSFWorkbook(file.getInputStream());
    Sheet sheet = workbook.getSheetAt(0);

    int[] headerIndexes = getHeaderIndexes(sheet, headers);

    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (row == null || isRowEmpty(row)) {
        continue; // Bỏ qua dòng trống
      }
      T obj;
      try {
        obj = clazz.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new RuntimeException("Không thể khởi tạo đối tượng: " + clazz.getSimpleName(), e);
      }

      Field[] fields = clazz.getDeclaredFields();
      for (int j = 0; j < fields.length; j++) {
        fields[j].setAccessible(true);
        Cell cell = row.getCell(headerIndexes[j]);
        if (cell != null) {
          try {
            if (fields[j].getType() == String.class) {
              //cell.setCellType(CellType.STRING);
              fields[j].set(obj, getCellValueAsString(cell));
            } else if (fields[j].getType() == int.class || fields[j].getType() == Integer.class) {
              fields[j].set(obj, (int) cell.getNumericCellValue());
            } else if (fields[j].getType() == double.class || fields[j].getType() == Double.class) {
              fields[j].set(obj, cell.getNumericCellValue());
            } else if (fields[j].getType() == Date.class) {
              fields[j].set(obj, cell.getDateCellValue());
            } else if (fields[j].getType() == long.class || fields[j].getType() == Long.class) {
              fields[j].set(obj, (long) cell.getNumericCellValue());
            } else if (fields[j].getType() == LocalDate.class) {
              fields[j].set(obj, getCellValueAsLocalDate(cell, "dd/MM/yyyy"));
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
      }
      resultList.add(obj);
    }
    workbook.close();
    return resultList;
  }

  private static boolean isRowEmpty(Row row) {
    if (row == null) {
      return true;
    }
    for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
      Cell cell = row.getCell(i);
      if ((cell != null) && (cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
        return false;
      }
    }
    return true;
  }

  private static int[] getHeaderIndexes(Sheet sheet, String[] headers) {
    int[] indexes = new int[headers.length];
    Row headerRow = sheet.getRow(0);
    for (int i = 0; i < headers.length; i++) {
      String header = headers[i];
      for (int j = 0; j < headerRow.getLastCellNum(); j++) {
        Cell cell = headerRow.getCell(j);
        if (cell != null && cell.getStringCellValue().equals(header)) {
          indexes[i] = j;
          break;
        }
      }
    }
    return indexes;
  }

  private static String getCellValueAsString(Cell cell) {
    if (cell == null) return null;
    switch (cell.getCellTypeEnum()) {
      case STRING:
        return cell.getStringCellValue().trim();
      case NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          return new SimpleDateFormat("dd/MM/yyyy").format(cell.getDateCellValue()); // Chuyển số thành chuỗi ngày
        }
        return String.valueOf((int) cell.getNumericCellValue()); // Trường hợp số
      case BOOLEAN:
        return String.valueOf(cell.getBooleanCellValue());
      case BLANK:
        return "";
      default:
        return cell.toString().trim();
    }
  }

  private static LocalDate getCellValueAsLocalDate(Cell cell, String pattern) {
    if (cell.getCellTypeEnum() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
      return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    } else if (cell.getCellTypeEnum() == CellType.STRING) {
      String dateStr = cell.getStringCellValue().trim();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
      return LocalDate.parse(dateStr, formatter);
    }
    throw new RuntimeException("Giá trị không phải kiểu ngày hoặc sai định dạng: " + cell.toString());
  }

  public static byte[] createExcelFile(List<?> data, String[] header, String title) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Dữ liệu nhiệm vụ");

    // Tạo CellStyle cho tiêu đề chính
    CellStyle titleStyle = workbook.createCellStyle();
    Font titleFont = workbook.createFont();
    titleFont.setBold(true);
    titleFont.setFontHeightInPoints((short) 14);
    titleFont.setFontName("Times New Roman");
    titleStyle.setFont(titleFont);
    titleStyle.setAlignment(HorizontalAlignment.CENTER);
    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    titleStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
    titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    // Tạo dòng tiêu đề chính (dòng 0 và 1)
    Row titleRow = sheet.createRow(0);
    Cell titleCell = titleRow.createCell(0);
    titleCell.setCellValue(title); // Nội dung tiêu đề
    titleCell.setCellStyle(titleStyle);

    // Hợp nhất các ô của dòng tiêu đề
    int lastColumn = header.length - 1;
    sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, lastColumn)); // Gộp từ cột 0 đến cột cuối
    // Tạo border cho vùng hợp nhất
    RegionUtil.setBorderBottom(BorderStyle.THIN, new CellRangeAddress(0, 1, 0, lastColumn), sheet);
    RegionUtil.setBorderTop(BorderStyle.THIN, new CellRangeAddress(0, 1, 0, lastColumn), sheet);
    RegionUtil.setBorderLeft(BorderStyle.THIN, new CellRangeAddress(0, 1, 0, lastColumn), sheet);
    RegionUtil.setBorderRight(BorderStyle.THIN, new CellRangeAddress(0, 1, 0, lastColumn), sheet);

    Row headerRow = sheet.createRow(2);
    CellStyle headerStyle = createHeaderCellStyle(workbook);
    for (int i = 0; i < header.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(header[i]);
      cell.setCellStyle(headerStyle);
      if ("STT".equals(header[i])) {
        sheet.setColumnWidth(i, 1500);
      }
      else{
        sheet.setColumnWidth(i, 5500);
      }
    }

    CellStyle dataStyle = createDataCellStyle(workbook);
    //CellStyle dataNumberStyle = createNumberCellStyle(workbook);
    for (int i = 0; i < data.size(); i++) {
      Row row = sheet.createRow(i + 3);
      Object obj = data.get(i);

      Class<?> clazz = obj.getClass();
      Field[] fields = clazz.getDeclaredFields();
      for (int j = 0; j < fields.length; j++) {
        Cell cell = row.createCell(j);
        fields[j].setAccessible(true);
        try {
          Object value = fields[j].get(obj);
          if (value != null) {
            if (value instanceof Number) {
              cell.setCellValue(((Number) value).longValue());
              cell.setCellStyle(createRightAlignedDataCellStyle(workbook));
            } else {
              cell.setCellValue(value.toString());
              cell.setCellStyle(dataStyle);
            }
          }
          else{
            cell.setCellValue("");
            cell.setCellStyle(dataStyle);
          }
          if (j == 8 || j == 9) {
            cell.setCellStyle(createRightAlignedDataCellStyle(workbook));
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }

//    for (int i = 0; i < header.length; i++) {
//      try {
//        sheet.autoSizeColumn(i);
//      } catch (Exception e) {
//        System.out.println("autoSizeColumn failed for column: " + i);
//        sheet.setColumnWidth(i, 5500);
//      }
//    }

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    workbook.write(outputStream);
    workbook.close();
    return outputStream.toByteArray();
  }
  private static CellStyle createHeaderCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    font.setFontName("Times New Roman");
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.CENTER); // Căn giữa tiêu đề
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // Màu xanh
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setWrapText(true);
    return style;
  }

  private static CellStyle createDataCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setFontName("Times New Roman");
    style.setFont(font);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.LEFT);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setWrapText(true);
    return style;
  }

  private static CellStyle createRightAlignedDataCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.RIGHT);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }
  private static CellStyle createNumberCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    style.setAlignment(HorizontalAlignment.RIGHT);
    DataFormat dataFormat = workbook.createDataFormat();
    style.setDataFormat(dataFormat.getFormat("#,##0")); // Định dạng số: có dấu phân cách hàng nghìn
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }

  private static void createDropDownList(Workbook workbook, Sheet sheet, int startRow, String sheetName, int columnIndexInUnitSheet, int columnIndexInImportSheet) {
    // Lấy phạm vi dữ liệu từ sheet "Unit" và cột tương ứng
    Sheet unitSheet = workbook.getSheet(sheetName);
    //int rowCount = unitSheet.getPhysicalNumberOfRows();
    String columnName = getColumnName(columnIndexInUnitSheet);
    // Tìm vị trí dòng cuối cùng có dữ liệu
    int lastRow = unitSheet.getLastRowNum();
    int lastValidRow = 1; // Bắt đầu từ dòng 2 (index 1) vì dòng 1 là header
    for (int i = 1; i <= lastRow; i++) {
      Row row = unitSheet.getRow(i);
      if (row != null) {
        Cell cell = row.getCell(columnIndexInUnitSheet);
        if (cell != null && cell.getCellTypeEnum() != CellType.BLANK &&
            !cell.getStringCellValue().trim().isEmpty()) {
          lastValidRow = i;
        }
      }
    }
    // Tạo phạm vi danh sách giá trị từ sheet "Unit"
    String formula = "'" + sheetName + "'!" + columnName + "2:" + columnName + (lastValidRow + 1);    // Tạo Data Validation cho cột trong sheet mẫu import
    DataValidationHelper validationHelper = sheet.getDataValidationHelper();
    DataValidationConstraint constraint = validationHelper.createFormulaListConstraint(formula);
    // Áp dụng validation cho từng ô một thay vì cả cột
    for (int row = startRow; row <= startRow + 1000; row++) { // Giới hạn ở 1000 dòng, có thể điều chỉnh
      CellRangeAddressList addressList = new CellRangeAddressList(row, row, columnIndexInImportSheet, columnIndexInImportSheet);
      DataValidation validation = validationHelper.createValidation(constraint, addressList);

      // Cấu hình thêm cho validation
      validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
      validation.setEmptyCellAllowed(true);
      validation.setShowErrorBox(true);
      validation.setShowPromptBox(true);

      sheet.addValidationData(validation);
    }
  }

  public static String getColumnName(int columnIndex) {
    StringBuilder columnName = new StringBuilder();
    while (columnIndex >= 0) {
      columnName.insert(0, (char) ('A' + (columnIndex % 26)));
      columnIndex = columnIndex / 26 - 1;
    }
    return columnName.toString();
  }
}
