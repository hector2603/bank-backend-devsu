package com.bank.bank.domain.service;

import com.bank.bank.domain.model.Client;
import com.bank.bank.infrastructure.dto.request.CreateClientRequest;
import com.bank.bank.infrastructure.dto.response.ClientResponse;

import java.util.List;

public interface ClientService {
    ClientResponse createClient(CreateClientRequest request);
    List<ClientResponse> getAllClients();
    ClientResponse updateClient(Long id, CreateClientRequest request);
    void deleteClient(Long id);
    ClientResponse getClient(Long id);
}
