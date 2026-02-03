package com.tave.tavewebsite.domain.interviewfinal.utils;

import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;


@Slf4j
@Component
public class ExcelUtils {

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("M/d");
    public static final String PARSING_NULL = "분석 실패";
    public static final String WEB_FRONTEND = "Web 프론트엔드";
    public static final String APP_FRONTEND = "App 프론트엔드";
    public static final String BACKEND = "백엔드";
    public static final String DEEPLEARNING = "딥러닝";
    public static final String DATAANALYSIST = "데이터분석";
    public static final String DESIGN = "디자인";
    public static final String MALE = "남자";
    public static final String FEMALE = "여자";

    public LocalDate getDateToCell(Cell cell) {
        if (cell == null) return null;
        CellType cellType = cell.getCellType();
        log.info("DEBUG cellType = " + cellType);
        if (cell.getCellType() == CellType.STRING) {
            String dateStr = cell.getStringCellValue().trim();
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        return cell.getLocalDateTimeCellValue().toLocalDate();
    }

    public LocalTime getTimeToCell(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            String timeStr = cell.getStringCellValue().trim();
            DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("[h:mm:ss a]")
                    .appendPattern("[H:mm:ss]")
                    .toFormatter(Locale.ENGLISH);
            return LocalTime.parse(timeStr, formatter);
        }

        return cell.getLocalDateTimeCellValue().toLocalTime();
    }

    public String getStringToCell(Cell cell) {
        if (cell == null) return PARSING_NULL;
        return cell.getStringCellValue().trim();
    }

    public Integer getIntegerToCell(Cell cell) {
        return (Integer) (int) cell.getNumericCellValue();
    }

    public Sex StringConvertToSex(String sex) {
        return switch (sex) {
            case PARSING_NULL -> Sex.NULL;
            case MALE -> Sex.MALE;
            case FEMALE -> Sex.FEMALE;
            default -> Sex.NON_SEX;
        };
    }

    public FieldType StringConvertToField(String field) {
        if (field.equals(PARSING_NULL)) return FieldType.EXCEL_PARSING_NULL;

        return switch (field.trim()) {
            case BACKEND -> FieldType.BACKEND;
            case DATAANALYSIST -> FieldType.DATAANALYSIS;
            case DEEPLEARNING -> FieldType.DEEPLEARNING;
            case WEB_FRONTEND -> FieldType.WEBFRONTEND;
            case DESIGN -> FieldType.DESIGN;
            case APP_FRONTEND -> FieldType.APPFRONTEND;
            default -> FieldType.PARSING_NULL;
        };
    }

    // 헤더 스타일 생성 메서드
    public CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();

        // 폰트 설정
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        // 배경색 설정
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // 정렬 설정
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 테두리 설정
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setTopBorderColor(IndexedColors.WHITE.getIndex());
        style.setBottomBorderColor(IndexedColors.WHITE.getIndex());

        return style;
    }

    // 면접 시간 (Body) Cell 스타일
    public CellStyle setBodyStyleOfInterviewTime(Workbook workbook) {
        CellStyle wrapStyle = workbook.createCellStyle();
        wrapStyle.setWrapText(true);
        wrapStyle.setAlignment(HorizontalAlignment.CENTER);
        wrapStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return wrapStyle;
    }

    public void writeText(Row row, String value, int idx){
        row.createCell(idx).setCellValue(value);
    }

    public void writeText(Row row, String value, int idx, CellStyle cellStyle){
        Cell cell = row.createCell(idx);
        cell.setCellValue(value);
        cell.setCellStyle(cellStyle);
    }

}
