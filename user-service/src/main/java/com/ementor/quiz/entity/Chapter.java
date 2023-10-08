package com.ementor.quiz.entity;


import com.ementor.quiz.core.entity.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter extends CommonEntity {
    @Serial
    private static final long serialVersionUID = 445428308201064483L;

    @Column(name = "title", length = 200)
    @NotBlank(message = "Content of the question cannot be null or blank")
    @Size(min = 1, max = 200)
    private String title;

    @Column(name = "description", length = 300)
    @Size(max = 300)
    private String description;

    @Column(name = "created_by")
    private UUID createdBy;
}
