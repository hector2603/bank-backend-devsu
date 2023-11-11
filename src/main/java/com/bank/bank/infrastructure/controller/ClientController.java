package com.bank.bank.infrastructure.controller;

import com.bank.bank.domain.model.Client;
import com.bank.bank.domain.service.ClientService;
import com.bank.bank.infrastructure.dto.request.CreateClientRequest;
import com.bank.bank.infrastructure.dto.response.ClientResponse;
import com.bank.bank.infrastructure.dto.response.MessageResponse;
import com.bank.bank.infrastructure.dto.response.WrapperResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@Tag(name = "Client Controller", description = "Controller to manage the clients")
public class ClientController {

    @Autowired
    private  ClientService clientService;

    @PostMapping
    @Operation(summary = "Create Client", description = "This method creates a new client")
    public ResponseEntity<WrapperResponse<ClientResponse>> createClient(@Valid @RequestBody CreateClientRequest request) {
        return ResponseEntity.ok(new WrapperResponse<>(clientService.createClient(request), null));
    }

    @GetMapping
    @Operation(summary = "Get All Clients", description = "This method returns all the clients")
    public ResponseEntity<WrapperResponse<List<ClientResponse>>> getAllClients() {
        return ResponseEntity.ok(new WrapperResponse<>(clientService.getAllClients(), null));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update Client", description = "This method updates a client")
    public ResponseEntity<WrapperResponse<ClientResponse>> updateClient(@PathVariable Long id, @Valid @RequestBody CreateClientRequest request) {
        return ResponseEntity.ok(new WrapperResponse<>(clientService.updateClient(id, request), null));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Client", description = "This method deletes a client")
    public ResponseEntity<WrapperResponse<MessageResponse>> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok(new WrapperResponse<>(new MessageResponse("Client deleted successfully"), null));
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Client", description = "This method returns a client by id")
    public ResponseEntity<WrapperResponse<ClientResponse>> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(new WrapperResponse<>(clientService.getClient(id), null));
    }

}
