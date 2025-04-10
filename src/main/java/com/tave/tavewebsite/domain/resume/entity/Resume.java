package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @Size(min = 1, max = 20)
    @Column(length = 20)
    private String school;

    @Size(max = 20)
    @Column(length = 20)
    private String major;

    @Size(max = 20)
    @Column(length = 20)
    private String minor;

    @Size(min = 1, max = 15)
    @Column(length = 8)
    private String field;

    @Min(1)
    @Max(5)
    private Integer resumeGeneration;

    @Size(max = 255)
    @Column(length = 50)
    private String blogUrl;

    @Size(max = 255)
    @Column(length = 50)
    private String githubUrl;

    @Size(max = 255)
    @Column(length = 50)
    private String portfolioUrl;

    @Size(min = 1, max = 10)
    @Column(length = 10)
    private String state;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<ResumeQuestion> specificResumeQuestion = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<ResumeQuestion> commonResumeQuestion = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<LanguageLevel> languageLevels = new ArrayList<>();

    @Builder
    public Resume(String school, String major, String minor, Integer resumeGeneration, String blogUrl, String githubUrl,
                  String portfolioUrl, String state, String field, Member member) {
        this.school = school;
        this.major = major;
        this.minor = minor;
        this.field = field;
        this.resumeGeneration = resumeGeneration;
        this.blogUrl = blogUrl;
        this.githubUrl = githubUrl;
        this.portfolioUrl = portfolioUrl;
        this.state = state;
        this.member = member;

        member.addResume(this);
    }

    public void addSpecificQuestion(ResumeQuestion resumeQuestion) {
        this.specificResumeQuestion.add(resumeQuestion);
    }

    public void addCommonQuestion(ResumeQuestion resumeQuestion) {
        this.commonResumeQuestion.add(resumeQuestion);
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.add(timeSlot);
    }

    public void addLanguageLevel(LanguageLevel languageLevel) {
        this.languageLevels.add(languageLevel);
    }

    public void updatePersonalInfo(PersonalInfoRequestDto requestDto) {
        this.school = requestDto.getSchool();
        this.major = requestDto.getMajor();
        this.minor = requestDto.getMinor();
        this.field = requestDto.getField();
    }

    public enum FieldType {
        DESIGNER("designer"),
        WEBFRONTEND("Web Frontend"),
        APPFRONTEND("App Frontend"),
        BACKEND("Backend"),
        DATAANALYSIS("Data Analysis"),
        DEEPLEARNING("Deep Learning");

        private final String message;

        FieldType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public void updateSocialLinks(SocialLinksRequestDto socialLinksRequestDto) {
        this.blogUrl = socialLinksRequestDto.getBlogUrl();
        this.githubUrl = socialLinksRequestDto.getGithubUrl();
    }

    public void updatePortfolio(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }
}
