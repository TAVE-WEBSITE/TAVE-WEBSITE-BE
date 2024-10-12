package com.tave.tavewebsite.domain.history.entity;

import com.tave.tavewebsite.global.common.BaseEntity;
import jakarta.persistence.*;
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
    @Column(nullable = false)
    private String generation;

    @NotNull
    @Size(min = 1, max = 500)
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private boolean isPublic;

    @Builder
    public History(String generation, String description, boolean isPublic) {
        this.generation = generation;
        this.description = description;
        this.isPublic = isPublic;
    }
}
