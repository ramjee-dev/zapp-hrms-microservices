package com.zapp.client_service.service.impl;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.service.IClientMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
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
        client.setIndustry(dto.industry());

        log.info("Created new Client entity, companyName: {}, email: {}",
                client.getCompanyName(), client.getEmail());
        return client;
    }

    @Override
    public void updateEntity(Client client, UpdateClientRequestDto dto) {
        log.debug("Updating Client entity, id: {}, companyName: {}",
                client.getId(), dto.companyName());

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
        log.debug("Performing partial update on Client entity, id: {}", existingClient.getId());

        if (dto.companyName() != null) {
            log.debug("Updating companyName from {} to {}",
                    existingClient.getCompanyName(), dto.companyName());
            existingClient.setCompanyName(dto.companyName());
        }
        if (dto.contactPerson() != null) {
            log.debug("Updating contactPerson from {} to {}",
                    existingClient.getContactPerson(), dto.contactPerson());
            existingClient.setContactPerson(dto.contactPerson());
        }
//        if (dto.email() != null) {
//            log.debug("Updating email from {} to {}",
//                    existingClient.getEmail(), dto.email());
//            existingClient.setEmail(dto.email());
//        }
        if (dto.phone() != null) {
            log.debug("Updating phone from {} to {}",
                    existingClient.getPhone(), dto.phone());
            existingClient.setPhone(dto.phone());
        }
        if (dto.address() != null) {
            log.debug("Updating address");
            existingClient.setAddress(dto.address());
        }
        if (dto.city() != null) {
            log.debug("Updating city from {} to {}",
                    existingClient.getCity(), dto.city());
            existingClient.setCity(dto.city());
        }
        if (dto.state() != null) {
            log.debug("Updating state from {} to {}",
                    existingClient.getState(), dto.state());
            existingClient.setState(dto.state());
        }
        if (dto.postalCode() != null) {
            log.debug("Updating postalCode from {} to {}",
                    existingClient.getPostalCode(), dto.postalCode());
            existingClient.setPostalCode(dto.postalCode());
        }
        if (dto.country() != null) {
            log.debug("Updating country from {} to {}",
                    existingClient.getCountry(), dto.country());
            existingClient.setCountry(dto.country());
        }
        if (dto.website() != null) {
            log.debug("Updating website from {} to {}",
                    existingClient.getWebsite(), dto.website());
            existingClient.setWebsite(dto.website());
        }
        if (dto.clientType() != null) {
            log.debug("Updating clientType from {} to {}",
                    existingClient.getClientType(), dto.clientType());
            existingClient.setClientType(dto.clientType());
        }
//        if (dto.status() != null) {
//            log.debug("Updating status from {} to {}",
//                    existingClient.getStatus(), dto.status());
//            existingClient.setStatus(dto.status());
//        }
        if (dto.description() != null) {
            log.debug("Updating description");
            existingClient.setDescription(dto.description());
        }
        if (dto.employeeCount() != null) {
            log.debug("Updating employeeCount from {} to {}",
                    existingClient.getEmployeeCount(), dto.employeeCount());
            existingClient.setEmployeeCount(dto.employeeCount());
        }
        if (dto.industry() != null) {
            log.debug("Updating industry from {} to {}",
                    existingClient.getIndustry(), dto.industry());
            existingClient.setIndustry(dto.industry());
        }

        log.info("Partially updated Client entity, id: {}", existingClient.getId());
    }

    @Override
    public ClientResponseDto toResponseDto(Client client) {
        log.debug("Mapping Client entity to ClientResponseDto, id: {}", client.getId());
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
        log.debug("Converting list of {} Client entities to ClientResponseDto list", clients.size());
        return clients.stream().map(this::toResponseDto).toList();
    }
}
