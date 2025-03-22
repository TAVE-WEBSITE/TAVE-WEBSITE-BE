package com.tave.tavewebsite.domain.history.entity;

import com.tave.tavewebsite.domain.history.dto.request.HistoryRequestDto;
import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historyId")
    private Long id;

    @NotNull
    @Size(min = 1, max = 5)
    @Column(length = 5, nullable = false)
    private String generation;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(length = 500, nullable = false)
    private String description;

    @Size(min = 1, max = 255)
    private String additionalDescription;

    @NotNull
    @Column(nullable = false)
    private boolean isPublic;

    @Builder
    public History(String generation, String description, String additionalDescription, boolean isPublic) {
        this.generation = generation;
        this.description = description;
        this.additionalDescription = additionalDescription;
        this.isPublic = isPublic;
    }

    public void patchHistory(HistoryRequestDto historyResponseDto) {
        generation = historyResponseDto.generation();
        description = historyResponseDto.description();
        additionalDescription = historyResponseDto.additionalDescription();
        isPublic = historyResponseDto.isPublic();
    }
}
