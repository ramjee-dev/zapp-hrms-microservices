package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.ClientCreateRequestDto;
import com.zapp.client_service.dto.ClientPartialUpdateRequestDto;
import com.zapp.client_service.dto.ClientUpdateRequestDto;
import com.zapp.client_service.exception.BusinessValidationException;
import com.zapp.client_service.service.IClientValidationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientValidationServiceImpl implements IClientValidationService {

    @Override
    public void validateForCreate(@Valid ClientCreateRequestDto clientDto) {

        log.debug("Validating Client data for creation: name='{}'",clientDto.getName());

        validateClientName(clientDto.getName());
    }

    @Override
    public void validateForUpdate(@NonNull Long clientId, @Valid ClientUpdateRequestDto dto) {

        log.debug("Validating client data for update: id={}, name='{}'", clientId, dto.getName());

        validateClientId(clientId);
        validateClientName(dto.getName());

    }

    @Override
    public void validateForPartialUpdate(@NonNull Long clientId, @Valid ClientPartialUpdateRequestDto dto) {

        log.debug("Validating client data for partial update: id={}", clientId);

        validateClientId(clientId);

        if (dto.getName() != null) {
            validateClientName(dto.getName());
        }

        // Ensure at least one field is provided for update
        if(dto.getName()==null&&dto.getLocation()==null&&dto.getStatus()==null){
            throw new BusinessValidationException("At least one field must be provided for partial update","request",dto);
        }

    }

    @Override
    public void validateForDelete(Long clientId) {

        log.debug("Validating client for deletion: id={}", clientId);

        validateClientId(clientId);

        // check if client has associated data that prevents deletion
    }

    private  void validateClientId(Long clientId) {

        if(clientId ==null || clientId <=0){
            throw  new BusinessValidationException("Client ID must be a positive number","clientId",clientId);
        }
    }

    private void validateClientName(String name) {

        if(name!=null && name.trim().toLowerCase().contains("test")){
            throw  new BusinessValidationException("Client name can not contain 'test' keyword","name",name);
        }
    }
}
