package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.service.IClientMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ClientMappingServiceImpl implements IClientMappingService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public Client toEntity(CreateClientRequestDto dto) {

        log.debug("Mapping CreateClientRequestDto to Client entity, companyName: {}, email: {}",
                dto.companyName(), dto.email());

        Client client = new Client();
        client.setCompanyName(dto.companyName());
        client.setContactPerson(dto.contactPerson());
        client.setEmail(dto.email());
        client.setPhone(dto.phone());
        client.setAddress(dto.address());
        client.setCity(dto.city());
        client.setState(dto.state());
        client.setPostalCode(dto.postalCode());
        client.setCountry(dto.country());
        client.setWebsite(dto.website());
        client.setClientType(dto.clientType());
        client.setDescription(dto.description());
        client.setEmployeeCount(dto.employeeCount());
        client.setIndustry(dto.industry()); // need to set client default status
        log.info("Created new Client entity, companyName: {}, email: {}",
                client.getCompanyName(), client.getEmail());
        return client;
    }

    @Override
    public void updateEntity(Client client, UpdateClientRequestDto dto) {
        log.debug("Updating Client entity, id: {}, companyName: {}",
                client.getId(), dto.companyName());
// omit fields which are immutable after client creation like email is immutable
// and also omit fields that have dedicated flow like status
        String prevCompanyName = client.getCompanyName();
        client.setCompanyName(dto.companyName());
        client.setContactPerson(dto.contactPerson());
        client.setPhone(dto.phone());
        client.setAddress(dto.address());
        client.setCity(dto.city());
        client.setState(dto.state());
        client.setPostalCode(dto.postalCode());
        client.setCountry(dto.country());
        client.setWebsite(dto.website());
        client.setClientType(dto.clientType());
        client.setDescription(dto.description());
        client.setEmployeeCount(dto.employeeCount());
        client.setIndustry(dto.industry());

        log.info("Updated Client entity, id: {}, previous companyName: {}, new companyName: {}",
                client.getId(), prevCompanyName, client.getCompanyName());
    }

    @Override
    public void partialUpdateEntity(Client existingClient, PartialUpdateClientRequestDto dto) {

        log.debug("Starting partial update on Client entity with id: {}", existingClient.getId());

        if (dto.companyName() != null) {
            existingClient.setCompanyName(dto.companyName());
        }
        if (dto.contactPerson() != null) {
            existingClient.setContactPerson(dto.contactPerson());
        }
        // email update intentionally excluded here per business rules/comment
        if (dto.phone() != null) {
            existingClient.setPhone(dto.phone());
        }
        if (dto.address() != null) {
            existingClient.setAddress(dto.address());
        }
        if (dto.city() != null) {
            existingClient.setCity(dto.city());
        }
        if (dto.state() != null) {
            existingClient.setState(dto.state());
        }
        if (dto.postalCode() != null) {
            existingClient.setPostalCode(dto.postalCode());
        }
        if (dto.country() != null) {
            existingClient.setCountry(dto.country());
        }
        if (dto.website() != null) {
            existingClient.setWebsite(dto.website());
        }
        if (dto.clientType() != null) {
            existingClient.setClientType(dto.clientType());
        }
        // status update intentionally excluded here per business rules/comment
        if (dto.description() != null) {
            existingClient.setDescription(dto.description());
        }
        if (dto.employeeCount() != null) {
            existingClient.setEmployeeCount(dto.employeeCount());
        }
        if (dto.industry() != null) {
            existingClient.setIndustry(dto.industry());
        }

        log.info("Completed partial update on Client entity with id: {}", existingClient.getId());
    }

    @Override
    public ClientResponseDto toResponseDto(Client client) {

        log.trace("Mapping Client entity to ClientResponseDto, id: {}", client.getId());

        return new ClientResponseDto(
                client.getId(),
                client.getCompanyName(),
                client.getContactPerson(),
                client.getEmail(),
                client.getPhone(),
                client.getAddress(),
                client.getCity(),
                client.getState(),
                client.getPostalCode(),
                client.getCountry(),
                client.getWebsite(),
                client.getClientType(),
                client.getStatus(),
                client.getDescription(),
                client.getEmployeeCount(),
                client.getIndustry(),
                client.getCreatedAt() != null ? FORMATTER.format(client.getCreatedAt()) : null,
                client.getUpdatedAt() != null ? FORMATTER.format(client.getUpdatedAt()) : null,
                client.getCreatedBy(),
                client.getUpdatedBy()
        );
    }

    @Override
    public List<ClientResponseDto> toResponseDtoList(List<Client> clients) {

        int count = (clients == null) ? 0 : clients.size();
        log.trace("Mapping list of {} Client entities to ClientResponseDto list", count);
        if (clients == null) return List.of();
        return clients.stream()
                .filter(Objects::nonNull)
                .map(this::toResponseDto)
                .toList();
    }
}
