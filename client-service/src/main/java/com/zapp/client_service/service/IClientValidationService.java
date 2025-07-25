package com.zapp.client_service.service;

import com.zapp.client_service.dto.ClientCreateRequestDto;
import com.zapp.client_service.dto.ClientPartialUpdateRequestDto;
import com.zapp.client_service.dto.ClientUpdateRequestDto;

public interface IClientValidationService {

    void validateForCreate(ClientCreateRequestDto clientDto);

    void validateForUpdate(Long clientId, ClientUpdateRequestDto dto);

    void validateForPartialUpdate(Long clientId, ClientPartialUpdateRequestDto dto);

    void validateForDelete(Long clientId);

}
