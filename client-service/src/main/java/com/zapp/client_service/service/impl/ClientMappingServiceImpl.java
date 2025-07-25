package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.service.IClientMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientMappingServiceImpl implements IClientMappingService {

    @Override
    public Client toEntity(ClientCreateRequestDto dto) {

        log.debug("Mapping ClientCreateRequestDto to Client entity");
        return Client.builder()
                .name(dto.getName().trim())
                .location(dto.getLocation()!=null? dto.getLocation().trim() :null )
                .build();
    }

    @Override
    public Client toEntity(ClientUpdateRequestDto dto) {

        log.debug("Mapping ClientUpdateRequestDto to Client entity");
        return Client.builder()
                .name(dto.getName().trim())
                .location(dto.getLocation() != null ? dto.getLocation().trim() : null)
                .status(dto.getStatus() != null ?
                        Client.Status.valueOf(dto.getStatus().name()) : Client.Status.ACTIVE)
                .build();
    }

    @Override
    public ClientResponseDto toResponseDto(Client entity) {

        log.debug("Mapping client entity to ClientResponseDto: id={}",entity.getClientId());
        return ClientResponseDto.builder()
                .clientId(entity.getClientId())
                .name(entity.getName())
                .location(entity.getLocation())
                .status(ClientResponseDto.Status.valueOf(entity.getStatus().name()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    @Override
    public List<ClientResponseDto> toResponseDtoList(List<Client> entities) {

        log.debug("Mapping {} Client entities to ClientResponseDto list", entities.size());

        return entities.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Override
    public ClientPageResponseDto toPageResponseDto(Page<Client> page) {

        log.debug("Mapping Client page to ClientPageResponseDto: page={}, size={}, totalElements={}",
                page.getNumber(), page.getSize(), page.getTotalElements());

        return ClientPageResponseDto.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .content(toResponseDtoList(page.getContent()))
                .build();
    }

    @Override
    public void mapPartialUpdate(ClientPartialUpdateRequestDto dto, Client existingClient) {

        log.debug("Applying partial update to Client entity: id={}", existingClient.getClientId());

        if(dto.getName()!=null){
            existingClient.setName(dto.getName().trim());
        }

        if(dto.getLocation()!=null){
            existingClient.setLocation(dto.getLocation().trim());
        }

        if(dto.getStatus()!=null){
            existingClient.setStatus(Client.Status.valueOf(dto.getStatus().name()));
        }

    }


}
