package com.zapp.client_service.service;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IClientMappingService {

    Client toEntity(CreateClientRequestDto dto);

    void updateEntity(Client existingClient, UpdateClientRequestDto dto);

    void partialUpdateEntity(Client existingClient, PartialUpdateClientRequestDto dto);

    ClientResponseDto toResponseDto(Client client);

    List<ClientResponseDto> toResponseDtoList(List<Client> clientList);

}