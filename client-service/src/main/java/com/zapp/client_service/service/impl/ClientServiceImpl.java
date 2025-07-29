package com.zapp.client_service.service.impl;
import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.exception.ClientAlreadyExistsException;
import com.zapp.client_service.exception.ResourceNotFoundException;
import com.zapp.client_service.repository.ClientRepository;
import com.zapp.client_service.service.IClientMappingService;
import com.zapp.client_service.service.IClientService;
import com.zapp.client_service.service.IClientValidationService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private  final IClientValidationService validationService;
    private final IClientMappingService mappingService;

    @Transactional
    public ClientResponseDto createClient(CreateClientRequestDto createClientDto) {

        log.info("Creating new client: {}", createClientDto.companyName());

        validationService.validateCreateClientRequest(createClientDto);

        Client client = mappingService.toEntity(createClientDto);
        Client savedClient = clientRepository.save(client);

        log.info("Successfully created client with id: {}", savedClient.getId());
        return mappingService.toResponseDto(savedClient);
    }

    @Override
    public ClientResponseDto fetchClientById(UUID clientId) {

        log.debug("Fetching client with id: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client","clientId",clientId+""));

        return mappingService.toResponseDto(client);
    }

    @Override
    public PagedClientResponseDto fetchAllClients(ClientPageRequestDto requestDto) {

        log.debug("Fetching clients with filters: {}", requestDto);

        Sort sort = Sort.by(Sort.Direction.fromString(requestDto.sortDir()), requestDto.sortBy());
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.size(), sort);

        Page<Client> clientPage = clientRepository.findClientsWithFilters(
                requestDto.status(),
                requestDto.clientType(),
                requestDto.industry(),
                requestDto.country(),
                requestDto.companyName(),
                pageable
        );

        return new PagedClientResponseDto(
                clientPage.getNumber(),
                clientPage.getTotalPages(),
                clientPage.getTotalElements(),
                clientPage.isFirst(),
                clientPage.isLast(),
                clientPage.hasNext(),
                clientPage.hasPrevious(),
                mappingService.toResponseDtoList(clientPage.getContent())
        );
    }

    @Override
    @Transactional
    public ClientResponseDto updateClient(UUID clientId, UpdateClientRequestDto updateClientDto) {
        log.info("Updating client with id: {}", clientId);

        validationService.validateUpdateClientRequest(clientId, updateClientDto);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client","clientId",clientId+""));

        mappingService.updateEntity(client, updateClientDto);
        Client updatedClient = clientRepository.save(client);

        log.info("Successfully updated client with id: {}", clientId);
        return mappingService.toResponseDto(updatedClient);
    }

    @Override
    @Transactional
    public ClientResponseDto partialUpdateClient(UUID clientId, PartialUpdateClientRequestDto clientDto) {

        log.info("Partially Updating client with id={}", clientId);

//        validationService.validateForPartialUpdate(clientId,clientDto);

        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client","clientId",clientId+""));

        mappingService.partialUpdateEntity(existingClient, clientDto);
        Client updatedClient = clientRepository.save(existingClient);

        return mappingService.toResponseDto(updatedClient);
        }

    @Override
    @Transactional
    public ClientResponseDto changeClientStatus(UUID clientId, ClientStatus newStatus) {

        log.info("Changing status of client {} to {}", clientId, newStatus);

        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client","clientId",clientId+""));

        validationService.validateStatusTransition(existingClient, newStatus);

        existingClient.setStatus(newStatus);
        Client updatedClient = clientRepository.save(existingClient);

        log.info("Successfully changed status of client {} to {}", clientId, newStatus);
        return mappingService.toResponseDto(updatedClient);
    }

    @Override
    @Transactional
    public void deleteClient(UUID clientId) {

        log.info("Deleting client with id: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client","clientId",clientId+""));

        validationService.validateClientDeletion(client);

        clientRepository.delete(client);
        log.info("Successfully deleted client with id: {}", clientId);
    }
}

