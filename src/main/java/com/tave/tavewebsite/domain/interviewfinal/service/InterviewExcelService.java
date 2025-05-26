package com.tave.tavewebsite.domain.interviewfinal.service;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.interviewfinal.exception.ExcelBadRequestException;
import com.tave.tavewebsite.domain.interviewfinal.utils.ExcelUtils;
import com.tave.tavewebsite.domain.member.entity.Sex;
import com.tave.tavewebsite.global.common.FieldType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterviewExcelService {

    private final ExcelUtils excelUtils;

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

        }catch (IOException e) {
            throw new ExcelBadRequestException();
        }

        return dtoList;
    }

    private InterviewFinalConvertDto convertRowToInterviewFinal(Row row) {
        String username = excelUtils.getStringToCell(row.getCell(0));
        Sex sex = excelUtils.StringConvertToSex(excelUtils.getStringToCell(row.getCell(1)));
        String email = excelUtils.getStringToCell(row.getCell(2));
        FieldType field = excelUtils.StringConvertToField(excelUtils.getStringToCell(row.getCell(3)));
        String university = excelUtils.getStringToCell(row.getCell(4));
        String interviewDay = excelUtils.getDayToCell(row.getCell(5));
        String interviewTime = excelUtils.getTimeToCell(row.getCell(6));
        Integer generation = excelUtils.getIntegerToCell(row.getCell(7));

        return InterviewFinalConvertDto
                .from(username, email, generation, sex, field, university, interviewDay, interviewTime);
    }

    private void logExcelLastRowNum(Sheet sheet) {
        log.info("=============================");
        log.info("서버에서 인식하는 Excel 마지막 행 번호: {}", sheet.getLastRowNum());
        log.info("=============================");
    }

}
