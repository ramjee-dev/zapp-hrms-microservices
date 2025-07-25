package com.zapp.client_service.service.impl;
import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.exception.ClientAlreadyExistsException;
import com.zapp.client_service.exception.ResourceNotFoundException;
import com.zapp.client_service.mapper.ClientMapper;
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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private  final IClientValidationService validationService;
    private final IClientMappingService mappingService;

    @Override
    @Transactional
    public ClientResponseDto createClient(@Valid @NonNull ClientCreateRequestDto clientDto) {

        log.info("Creating client with name='{}'", clientDto.getName());

        validationService.validateForCreate(clientDto);

        if (clientRepository.existsByName(clientDto.getName())) {
            log.warn("Attempt to create duplicate client with name='{}'", clientDto.getName());
            throw new ClientAlreadyExistsException("Client with name :"+clientDto.getName()+" already exists", clientDto.getName());
        }

        Client client = mappingService.toEntity(clientDto);
        client.setStatus(Client.Status.ACTIVE);

        Client savedClient = clientRepository.save(client);

        log.info("Successfully created client: id={}, name='{}'", savedClient.getClientId(), savedClient.getName());

        return mappingService.toResponseDto(savedClient);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponseDto fetchClientById(@NonNull Long clientId) {

        log.info("Retrieving client with id={}", clientId);

        Client client = clientRepository.findById(clientId).orElseThrow(
                () ->{ log.warn("Client not found with id={}", clientId);
                       return new ResourceNotFoundException("Client", "ClientId",clientId+"");
                });

        log.info("Successfully retrieved client: id={}, name='{}'", client.getClientId(), client.getName());

        return mappingService.toResponseDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientPageResponseDto getAllClients(@NonNull @Valid ClientPageRequestDto dto) {

        log.info("Retrieving clients: page={}, size={}, sortBy={}, sortDir={}",
                dto.getPage(), dto.getSize(),dto.getSortBy(),dto.getSortDir());

        Sort.Direction direction = Sort.Direction.fromString(dto.getSortDir());
        Pageable pageable = PageRequest.of(dto.getPage(), dto.getSize(), Sort.by(direction, dto.getSortBy()));

        Page<Client> clientPage = clientRepository.findByFilters(
                dto.getName(),
                dto.getLocation(),
                dto.getStatus()!=null?Client.Status.valueOf(dto.getStatus().name()):null,
                pageable);

        log.info("Successfully retrieved {} clients out of {} total",
                clientPage.getNumberOfElements(), clientPage.getTotalElements());

        return mappingService.toPageResponseDto(clientPage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientResponseDto> getClientByStatus(Client.Status status) {

        log.info("Retrieving clients by status={}", status);

        List<Client> clients = clientRepository.findByStatus(status);

        log.info("Found {} clients with status={}", clients.size(), status);

        return mappingService.toResponseDtoList(clients);
    }


    @Override
    @Transactional
    public ClientResponseDto updateClient(@NonNull Long clientId, @Valid @NonNull ClientUpdateRequestDto clientDto) {

        log.info("Updating client with id={}, name='{}'", clientId, clientDto.getName());

        validationService.validateForUpdate(clientId, clientDto);

        Client existingClient = clientRepository.findById(clientId).orElseThrow(
                () -> {
                    log.warn("Client not found for update with id={}", clientId);
                    return new ResourceNotFoundException("Client", "ClientId", clientId + "");
                });

        // Check for name uniqueness (excluding current client)
        if (clientRepository.existsByNameAndClientIdNot(clientDto.getName(), clientId)) {
            log.warn("Attempt to update client with duplicate name='{}', id={}", clientDto.getName(), clientId);
            throw new ClientAlreadyExistsException(
                    "Client with name '" + clientDto.getName() + "' already exists",
                    clientDto.getName()
            );
        }

        Client updatedClient = mappingService.toEntity(clientDto);
        updatedClient.setClientId(existingClient.getClientId());
        updatedClient.setCreatedAt(existingClient.getCreatedAt());
        updatedClient.setCreatedBy(existingClient.getCreatedBy());

        Client savedClient = clientRepository.save(updatedClient);

        log.info("Successfully updated client: id={}, name='{}'", savedClient.getClientId(),
                savedClient.getName());

        return mappingService.toResponseDto(savedClient);
    }

    @Override
    @Transactional
    public ClientResponseDto partialUpdateClient(@NonNull Long clientId, @Valid @NonNull ClientPartialUpdateRequestDto clientDto) {

        log.info("Partially Updating client with id={}", clientId);

        validationService.validateForPartialUpdate(clientId,clientDto);

        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    log.warn("Client not found for partial update with id={}", clientId);
                    return new ResourceNotFoundException("Client", "clientId", clientId+"");
                });

        // Check name uniqueness if name is being updated
        if (clientDto.getName() != null &&
                !clientDto.getName().equals(existingClient.getName()) &&
                clientRepository.existsByNameAndClientIdNot(clientDto.getName(), clientId)) {
            log.warn("Attempt to partially update client with duplicate name='{}', id={}", clientDto.getName(), clientId);
            throw new ClientAlreadyExistsException(
                    "Client with name '" + clientDto.getName() + "' already exists",
                    clientDto.getName()
            );
        }

        mappingService.mapPartialUpdate(clientDto,existingClient);

        Client savedClient = clientRepository.save(existingClient);

        log.info("Successfully partially updated client: id={}, name='{}'", savedClient.getClientId(), savedClient.getName());

        return mappingService.toResponseDto(savedClient);


    }


    @Override
    @Transactional
    public void deleteClient(@NonNull Long clientId) {

        log.info("Deleting client with id={}", clientId);

        validationService.validateForDelete(clientId);

        if (!clientRepository.existsById(clientId)) {
            log.warn("Client not found for deletion with id={}", clientId);
            throw new ResourceNotFoundException("Client", "clientId", clientId+"");
        }

        clientRepository.deleteById(clientId);

        log.info("Successfully deleted client with id={}", clientId);
    }

}
