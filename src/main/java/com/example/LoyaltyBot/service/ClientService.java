package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.ClientDto;
import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.exception.ClientNotFoundException;
import com.example.LoyaltyBot.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createClient(Client client) {
        clientRepository.save(client);
    }

    public ClientDto findById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(id));
        return ClientDto.toDto(client);

    }

    public List<ClientDto> findAll() {
        return clientRepository.findAll().stream()
                .map(ClientDto::toDto)
                .toList();
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(ClientDto clientDto, Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new ClientNotFoundException(id));

        if (!clientDto.id().equals(id)) {
            throw new IllegalArgumentException("ID в DTO не совпадает с ID пути");
        }
        client.setFirstName(clientDto.firstName());
        client.setLastName(clientDto.lastName());
        client.setBirthday(clientDto.birthday());
        client.setPhone(clientDto.phone());
        clientRepository.save(client);
    }
}
