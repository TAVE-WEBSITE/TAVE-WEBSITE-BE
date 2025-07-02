package com.tave.tavewebsite.domain.interviewfinal.usecase;

import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalConvertDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.InterviewFinalSaveDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.S3ExcelFileInputStreamDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalDetailDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.InterviewFinalForMemberDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.InterviewTimeTableDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.InterviewTimeTableGroupByDayDto;
import com.tave.tavewebsite.domain.interviewfinal.dto.response.timetable.TotalDateTimeDto;
import com.tave.tavewebsite.domain.interviewfinal.entity.InterviewFinal;
import com.tave.tavewebsite.domain.interviewfinal.mapper.InterviewFinalMapper;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewExcelService;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewGetService;
import com.tave.tavewebsite.domain.interviewfinal.service.InterviewSaveService;
import com.tave.tavewebsite.domain.interviewfinal.utils.InterviewGroupUtil;
import com.tave.tavewebsite.domain.interviewplace.dto.response.InterviewPlaceDetailDto;
import com.tave.tavewebsite.domain.interviewplace.service.InterviewPlaceService;
import com.tave.tavewebsite.domain.member.dto.response.MemberResumeDto;
import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.member.service.MemberService;
import com.tave.tavewebsite.global.s3.service.S3DownloadSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewFinalUseCase {

    private final InterviewExcelService interviewExcelService;
    private final InterviewSaveService interviewSaveService;
    private final InterviewGetService interviewGetService;
    private final S3DownloadSerivce s3DownloadSerivce;
    private final InterviewPlaceService interviewPlaceService;
    private final InterviewFinalMapper mapper;
    private final InterviewGroupUtil groupUtil;
    private final MemberService memberService;

    public S3ExcelFileInputStreamDto downloadInterviewFinal() throws IOException {
        return s3DownloadSerivce.downloadInterviewFinalSetUpForm();
    }

    public void insertInterviewEntityFromExcel(MultipartFile file) {
        // Excel에서 최종 면접 데이터 추출
        List<InterviewFinalConvertDto> dtoList = interviewExcelService.exportInterviewFinalFromExcel(file);

        // Map <email, InterviewDto 객체>
        Map<String, InterviewFinalConvertDto> mappingEmailInterviewFinal = dtoList.stream()
                .collect(Collectors.toMap(InterviewFinalConvertDto::email, Function.identity()));

        // SQL IN 조회를 위해 List<String> email 추출
        List<String> emailList = mappingEmailInterviewFinal.keySet().stream().toList();

        String generation = dtoList.get(0).generation();
        List<MemberResumeDto> memberResumeDtoList = memberService.findMemberResumeDto(generation, emailList);

        List<InterviewFinalSaveDto> pairedList = combineMemberResumeInterview(dtoList, memberResumeDtoList);

        // BulkInsert
        interviewSaveService.saveInterviewFinalList(pairedList);

    }

    public List<InterviewFinalDetailDto> getInterviewFinalList(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);

        return interviewGetService.getInterviewFinalPageableList(pageable)
                .getContent()
                .stream()
                .map(InterviewFinalDetailDto::from)
                .toList();
    }

    public InterviewFinalForMemberDto getMemberInterviewFinalDetail(Member currentMember, String generation) {

        InterviewFinal interviewFinal = interviewGetService.getInterviewFinalByMemberId(currentMember.getId(), String.valueOf(generation));
        InterviewPlaceDetailDto interviewDateInfo = interviewPlaceService.getInterviewPlaceByDate(interviewFinal.getInterviewDate());

        return mapper.mapInterviewFinalForMember(interviewFinal, interviewDateInfo);
    }

    public InterviewTimeTableDto getTimeTableList(String generation){
        List<InterviewFinal> interviewFinalList = interviewGetService.getInterviewFinalListByGeneration(generation);

        // 그룹화
        Map<LocalDate, Map<LocalTime, List<InterviewFinal>>> group =
                groupUtil.groupByDateAndTime(interviewFinalList);

        // 그룹화 -> Dto 변환, 면접 전체 날짜와 시간 -> Dto변환
        List<InterviewTimeTableGroupByDayDto> timeTableList = groupUtil.createTimeTableDtoList(group);
        TotalDateTimeDto totalDateTimeList = groupUtil.getTotalDateTimeDto(group);

        return InterviewTimeTableDto.of(totalDateTimeList, timeTableList);
    }

    /*
    * refactor
    * */

    /*
    * InterviewFinal을 저장할 때 추후 편리성을 위해 memberId와 resumeId를 같이 저장하고자 함.
    * 이를 위해MemberResumeDto와 InterviewDto를 고유한 값 email + generation을 기준으로 결합.
    * */
    private List<InterviewFinalSaveDto> combineMemberResumeInterview(List<InterviewFinalConvertDto> dtoList, List<MemberResumeDto> memberResumeDtoList) {
        return dtoList.stream()
                .map(interviewDto -> {
                    String email = interviewDto.email();

                    // "분석 실패"인 경우
                    if ("분석 실패".equals(email)) {
                        return InterviewFinalSaveDto.analysisFailed(interviewDto);
                    }

                    // 정상 이메일인 경우: MemberResumeDto 찾기
                    Optional<MemberResumeDto> matchedMember = memberResumeDtoList.stream()
                            .filter(memberDto -> email.equals(memberDto.email()))
                            .findFirst();

                    // MemberResumeDto가 존재하면 success, 없으면 analysisFailed
                    return matchedMember
                            .map(memberDto -> InterviewFinalSaveDto.analysisSucceeded(interviewDto, memberDto))
                            .orElseGet(() -> InterviewFinalSaveDto.analysisFailed(interviewDto));
                })
                .toList();
    }

}
