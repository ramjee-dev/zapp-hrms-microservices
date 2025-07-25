package com.zapp.client_service.service;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IClientMappingService {

    Client toEntity(ClientCreateRequestDto dto);

    Client toEntity(ClientUpdateRequestDto dto);

    ClientResponseDto toResponseDto(Client entity);

    List<ClientResponseDto> toResponseDtoList(List<Client> entities);

    ClientPageResponseDto toPageResponseDto(Page<Client> page);

    void mapPartialUpdate(ClientPartialUpdateRequestDto dto , Client existingClient);

}