package com.ementor.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterDTO {
    private UUID id;

    @NotBlank(message = "Content of the chapter title cannot be null or blank")
    @Size(min = 1, max = 100)
    private String title;

    @Size(max = 200)
    private String description;

    private UUID createdBy;
}
