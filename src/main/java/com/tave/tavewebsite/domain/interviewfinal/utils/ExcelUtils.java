package com.tave.tavewebsite.domain.interviewfinal.utils;

import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;


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

        return cell.getLocalDateTimeCellValue().toLocalDate();
    }

    public LocalTime getTimeToCell(Cell cell) {
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

}
