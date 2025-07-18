package com.zapp.job_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class ClientDto {

    private String name;

    private String location;

    private String status;
}
