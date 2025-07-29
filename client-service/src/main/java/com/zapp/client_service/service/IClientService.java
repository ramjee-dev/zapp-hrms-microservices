package com.zapp.client_service.service;

import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;

import java.util.List;
import java.util.UUID;

public interface IClientService {

    ClientResponseDto createClient(CreateClientRequestDto dto);

    ClientResponseDto fetchClientById(UUID clientId);

    PagedClientResponseDto fetchAllClients(ClientPageRequestDto dto);

    ClientResponseDto updateClient(UUID clientId, UpdateClientRequestDto clientDto);

    ClientResponseDto partialUpdateClient(UUID clientId, PartialUpdateClientRequestDto clientDto);

    ClientResponseDto changeClientStatus(UUID clientId, ClientStatus newStatus);

    void deleteClient(UUID clientId);
}
