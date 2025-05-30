package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.member.entity.Member;
import com.tave.tavewebsite.domain.programinglaunguage.entity.LanguageLevel;
import com.tave.tavewebsite.domain.resume.dto.request.PersonalInfoRequestDto;
import com.tave.tavewebsite.domain.resume.dto.request.SocialLinksRequestDto;
import com.tave.tavewebsite.domain.resume.exception.AlreadySubmittedResumeException;
import com.tave.tavewebsite.global.common.BaseEntity;
import com.tave.tavewebsite.global.common.FieldType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume extends BaseEntity {

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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FieldType field;

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

    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private ResumeState state = ResumeState.TEMPORARY;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeTimeSlot> resumeTimeSlots = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LanguageLevel> languageLevels = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeQuestion> resumeQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeEvaluation> resumeEvaluations = new ArrayList<>();

    @Builder
    public Resume(String school, String major, String minor, Integer resumeGeneration, String blogUrl, String githubUrl,
                  String portfolioUrl, ResumeState state, FieldType field, Member member) {
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

    public void addTimeSlot(ResumeTimeSlot resumeTimeSlot) {
        this.resumeTimeSlots.add(resumeTimeSlot);
    }

    public void addLanguageLevel(LanguageLevel languageLevel) {
        this.languageLevels.add(languageLevel);
    }

    public void updatePersonalInfo(PersonalInfoRequestDto requestDto, FieldType fieldType) {
        this.school = requestDto.getSchool();
        this.major = requestDto.getMajor();
        this.minor = requestDto.getMinor();
        this.field = fieldType;
    }

    public void updateSocialLinks(SocialLinksRequestDto socialLinksRequestDto) {
        this.blogUrl = socialLinksRequestDto.getBlogUrl();
        this.githubUrl = socialLinksRequestDto.getGithubUrl();
    }

    public void updatePortfolio(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public void submit() {
        if (this.state == ResumeState.SUBMITTED) {
            throw new AlreadySubmittedResumeException();
        }
        this.state = ResumeState.SUBMITTED;
    }

}
