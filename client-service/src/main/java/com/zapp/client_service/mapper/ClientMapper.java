package com.zapp.client_service.mapper;

import com.zapp.client_service.dto.ClientDto;
import com.zapp.client_service.entity.Client;

public class ClientMapper {

    public static ClientDto mapToClientDto(Client client, ClientDto clientDto){
        clientDto.setName(client.getName());
        clientDto.setLocation(client.getLocation());
        clientDto.setStatus(client.getStatus());
        return clientDto;
    }

    public static Client mapToClient(ClientDto clientDto,Client client){
        client.setName(clientDto.getName());
        client.setLocation(clientDto.getLocation());
        client.setStatus(clientDto.getStatus());
        return client;
    }
}
