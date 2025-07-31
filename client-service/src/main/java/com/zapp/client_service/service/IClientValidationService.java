package com.zapp.client_service.service;

import com.zapp.client_service.dto.CreateClientRequestDto;
import com.zapp.client_service.dto.PartialUpdateClientRequestDto;
import com.zapp.client_service.dto.UpdateClientRequestDto;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;

import java.util.UUID;

public interface IClientValidationService {

    void validateCreateClientRequest(CreateClientRequestDto dto);

    void validateUpdateClientRequest(UUID clientId, UpdateClientRequestDto dto);

    void validatePartialUpdateClientRequest(UUID clientId, PartialUpdateClientRequestDto dto);

    void validateStatusTransition(Client existingClient, ClientStatus newStatus);

    void validateClientDeletion(Client client);

}
