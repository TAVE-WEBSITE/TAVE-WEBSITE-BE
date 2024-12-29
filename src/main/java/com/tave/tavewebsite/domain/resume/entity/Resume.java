package com.tave.tavewebsite.domain.resume.entity;

import com.tave.tavewebsite.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Size(min = 1, max = 8)
    @Column(length = 8)
    private String field;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL)
    private List<LanguageLevel> languageLevels = new ArrayList<>();

    @Builder
    public Resume(String school, String major, String field, Member member) {
        this.school = school;
        this.major = major;
        this.field = field;
        this.member = member;
        member.addResume(this);
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        this.timeSlots.add(timeSlot);
    }

    public void addLanguageLevel(LanguageLevel languageLevel) {
        this.languageLevels.add(languageLevel);
    }
}
