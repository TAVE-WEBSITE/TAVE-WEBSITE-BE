package com.tave.tavewebsite.domain.interviewfinal.usecase;

import com.tave.tavewebsite.domain.apply.initial.setup.service.ApplyInitialSetupService;
import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewExcelService;
import com.tave.tavewebsite.domain.resume.dto.response.ResumeMemberInfoDto;
import com.tave.tavewebsite.domain.resume.entity.EvaluationStatus;
import com.tave.tavewebsite.domain.resume.service.InterviewTimeService;
import com.tave.tavewebsite.domain.resume.service.ResumeGetService;
import com.tave.tavewebsite.global.s3.service.S3DownloadSerivce;
import com.tave.tavewebsite.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class InterviewExcelUseCase {

    private final InterviewExcelService interviewExcelService;
    private final InterviewTimeService interviewTimeService;
    private final ApplyInitialSetupService applyInitialSetupService;
    private final ResumeGetService resumeGetService;
    private final S3DownloadSerivce s3DownloadSerivce;
    private final S3Service s3Service;

    public void savePossibleInterviewTimeCSV() {
        int rowIdx = 1;
        String generation = applyInitialSetupService.getCurrentGeneration();
        // 실제 면접 날짜 LocalDate 리스트 가져오기 todo 이 부분은 개선 필요 (SQL 조회 쿼리 타입이 ALL)
        List<LocalDate> distinctDateList = interviewTimeService.getDistinctInterviewDates();

        Map<ResumeMemberInfoDto, List<LocalDateTime>> maps
                = resumeGetService.getResumeMemberAndInterviewTimeMap(generation, EvaluationStatus.PASS);

        // xlsx 생성
        Workbook workbook = createPossibleInterviewTimeCSV(distinctDateList, maps, rowIdx);
        // s3 저장
        s3Service.uploadWorkbookToS3(workbook);
    }

    public S3ExcelFileInputStreamDto getPossibleInterviewTimeCSV() throws IOException {
        return s3DownloadSerivce.downloadPossibleTimeTableXlsx();
    }

    public void saveInterviewTimeTableForManagerXLSX(MultipartFile file) {
        s3Service.uploadTimeTableForMangerXLSXToS3(file);
    }

    public S3ExcelFileInputStreamDto getInterviewTimeTableForManagerXLSX() throws IOException {
        return s3DownloadSerivce.downloadInterviewTimeTableForManagerXLSX();
    }


    /*
    * refactor
    * */

    private Workbook createPossibleInterviewTimeCSV(List<LocalDate> distinctDateList, Map<ResumeMemberInfoDto, List<LocalDateTime>> maps, int rowIdx) {
        // CSV 제작
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("면접자 시간 파악");

        // 헤더 작성, 본문 스타일 설정
        interviewExcelService.writeCSVHeader(workbook, sheet, distinctDateList);
        CellStyle bodyWrapStyle = interviewExcelService.setCSVBodyStyleOfInterviewTime(workbook);

        for (Map.Entry<ResumeMemberInfoDto, List<LocalDateTime>> entry : maps.entrySet()) {
            ResumeMemberInfoDto dto = entry.getKey();
            List<LocalDateTime> dateTimeList = entry.getValue();
            Row row = sheet.createRow(rowIdx++);
            int startIdx = 0;

            // 날짜별 시간 초기화
            TreeMap<LocalDate, List<LocalTime>> possibleTimes = groupInterviewTimesByDate(distinctDateList, dateTimeList);
            // 인적사항, 면접가능시간 작성
            startIdx = interviewExcelService.writeInfoOfRecruiter(row, dto, startIdx);
            interviewExcelService.writePossibleTimeOfRecruiter(possibleTimes, distinctDateList, bodyWrapStyle, row, startIdx);
        }

        return workbook;
    }

    // 같은 날짜에 면접 가능한 시간을 Map 컬렉션으로 반환
    private TreeMap<LocalDate, List<LocalTime>> groupInterviewTimesByDate(List<LocalDate> distinctDateList, List<LocalDateTime> dateTimeList) {
        TreeMap<LocalDate, List<LocalTime>> possibleTimes = new TreeMap<>();
        for (LocalDate date : distinctDateList) {
            possibleTimes.put(date, new ArrayList<>());
        }

        // 날짜별 시간 분류
        for (LocalDateTime dateTime : dateTimeList) {
            LocalDate date = dateTime.toLocalDate();
            LocalTime time = dateTime.toLocalTime();

            if (possibleTimes.containsKey(date)) {
                possibleTimes.get(date).add(time);
            }
        }

        // 시간 정렬
        for (List<LocalTime> times : possibleTimes.values()) {
            times.sort(Comparator.naturalOrder());
        }
        return possibleTimes;
    }

}
