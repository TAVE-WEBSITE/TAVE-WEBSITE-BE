package com.tave.tavewebsite.domain.history.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historyId")
    private Long id;

    @Size(min = 1, max = 5)
    @Column(nullable = false)
    private String generation;

    @Size(min = 1, max = 500)
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isPublic;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public History(String generation, String description, boolean isPublic) {
        this.generation = generation;
        this.description = description;
        this.isPublic = isPublic;
    }
}
