package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.exception.ExcelBadRequestException;
import com.tave.tavewebsite.domain.interviewfinal.exception.ExcelNullPointException;
import com.tave.tavewebsite.domain.interviewfinal.utils.ExcelUtils;
import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeMemberInfoDto;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewExcelService {

    private final ExcelUtils excelUtils;

    private static final String[] HEADERS = {"이름", "성별", "이메일", "지원 분야", "대학"};
    private static final int HEADER_SIZE = 5;

    public List<InterviewFinalConvertDto> exportInterviewFinalFromExcel(MultipartFile file) {
        List<InterviewFinalConvertDto> dtoList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            logExcelLastRowNum(sheet);
            boolean isFirstRow = true;

            for (Row row : sheet) {
                // 0행 스킵
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }
                // 빈 행 스킵
                if (row == null || row.getCell(0) == null) continue;

                InterviewFinalConvertDto dto = convertRowToInterviewFinal(row);
                dtoList.add(dto);
            }

        }catch (NullPointerException e) {
            throw new ExcelNullPointException();
        }catch (IOException e) {
            throw new ExcelBadRequestException();
        }

        return dtoList;
    }

    // 면접 평가 시트에 면접자 정보 작성 비지니스 로직
    public void writeInterviewerData(Workbook workbook, List<InterviewFinal> interviewList) {
        try {
            int startRow = 2;
            int sequence = 1;
            Sheet sheet = workbook.getSheetAt(0);
            CellStyle cellStyle = setCSVBodyStyleOfInterviewTime(workbook);
            writeInterviewerData(sheet, startRow, interviewList.get(0), sequence, cellStyle);

            for (int i = 1; i < interviewList.size(); i++) {
                InterviewFinal data = interviewList.get(i);  // 리스트에서 인덱스로 데이터 가져오기
                InterviewFinal before = interviewList.get(i - 1);

                if(checkSequence(before, data)) sequence++;
                else sequence = 1;

                writeInterviewerData(sheet, startRow+i, data, sequence, cellStyle);
            }
        } catch (Exception e) {
            throw new RuntimeException("엑셀 데이터 작성 실패", e);
        }
    }

    private void writeInterviewerData(Sheet sheet, int startRow, InterviewFinal data, int sequence, CellStyle cellStyle) {
        Row row = sheet.getRow(startRow);
        if (row == null) {
            row = sheet.createRow(startRow);
        }

        // 기존 데이터 작성 로직
        String value = formatDateTimeToEvaluation(data.getInterviewDate(), data.getInterviewTime(), sequence);
        excelUtils.writeText(row, value, 0, cellStyle);
        excelUtils.writeText(row, data.getFieldType().getDisplayName(), 1, cellStyle);
        excelUtils.writeText(row, data.getUsername(), 2, cellStyle);
    }

    public void writeCSVHeader(Workbook workbook, Sheet sheet, List<LocalDate> localDateList) {
        // 폰트 생성. Header, Body 분리
        CellStyle headerStyle = excelUtils.createHeaderStyle(workbook);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADER_SIZE; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }
        sheet.setColumnWidth(2, 5000); // 이메일 컬럼 너비 조정.

        for (int i = HEADER_SIZE; i < HEADER_SIZE + localDateList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(localDateList.get(i - HEADER_SIZE).toString());
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 6000);
        }
    }

    // 면접 시간 (Body) Cell 스타일
    public CellStyle setCSVBodyStyleOfInterviewTime(Workbook workbook) {
        return excelUtils.setBodyStyleOfInterviewTime(workbook);
    }

    // 인적사항 작성
    public int writeInfoOfRecruiter(Row row, ResumeMemberInfoDto dto, int startIdx){

        excelUtils.writeText(row, dto.username(), startIdx++);
        excelUtils.writeText(row, dto.sex(), startIdx++);
        excelUtils.writeText(row, dto.email(), startIdx++);
        excelUtils.writeText(row, dto.field(), startIdx++);
        excelUtils.writeText(row, dto.univ(), startIdx++);

        return startIdx;
    }

    // 지원자의 면접가능시간 작성
    public int writePossibleTimeOfRecruiter(
            TreeMap<LocalDate, List<LocalTime>> possibleTimes, List<LocalDate> distinctDateList ,
            CellStyle cellStyle, Row row, int startIdx
    ){
        for (LocalDate date : distinctDateList) {
            List<LocalTime> times = possibleTimes.get(date);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < times.size(); i++) {
                if (i > 0) {
                    if (i % 4 == 0) sb.append("\n");
                    else sb.append(", ");
                }
                sb.append(times.get(i).toString());
            }
            excelUtils.writeText(row, sb.toString(), startIdx++, cellStyle);
        }
        return startIdx;
    }

    private InterviewFinalConvertDto convertRowToInterviewFinal(Row row) {
        String username = excelUtils.getStringToCell(row.getCell(0));
        Sex sex = excelUtils.StringConvertToSex(excelUtils.getStringToCell(row.getCell(1)));
        String email = excelUtils.getStringToCell(row.getCell(2));
        FieldType field = excelUtils.StringConvertToField(excelUtils.getStringToCell(row.getCell(3)));
        String university = excelUtils.getStringToCell(row.getCell(4));
        LocalDate interviewDate = excelUtils.getDateToCell(row.getCell(5));
        LocalTime interviewTime = excelUtils.getTimeToCell(row.getCell(6));
        Integer generation = excelUtils.getIntegerToCell(row.getCell(7));

        return InterviewFinalConvertDto
                .from(username, email, String.valueOf(generation), sex, field, university, interviewDate, interviewTime);
    }

    private void logExcelLastRowNum(Sheet sheet) {
        log.info("=============================");
        log.info("서버에서 인식하는 Excel 마지막 행 번호: {}", sheet.getLastRowNum());
        log.info("=============================");
    }

    private String formatDateTimeToEvaluation(LocalDate date, LocalTime time, int sequence) {
        StringBuilder sb = new StringBuilder();

        sb.append(date.getDayOfMonth()).append("_");
        sb.append(time.format(DateTimeFormatter.ofPattern("HHmm"))).append("_");
        sb.append(sequence);
        return sb.toString();
    }

    private boolean checkSequence(InterviewFinal before, InterviewFinal after) {
        return before.getInterviewDate().equals(after.getInterviewDate()) &&
                before.getInterviewTime().equals(after.getInterviewTime());
    }

}
