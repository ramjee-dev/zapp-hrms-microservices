package com.zapp.candidate_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class JobDto {

    private String title;
    private String description;
    private String status;
    private Long clientId;

}
