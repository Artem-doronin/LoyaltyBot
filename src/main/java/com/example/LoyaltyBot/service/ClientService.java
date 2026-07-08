package com.example.LoyaltyBot.service;

import com.example.LoyaltyBot.dto.ClientDto;
import com.example.LoyaltyBot.entity.Client;
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
                () -> new RuntimeException("Client not found"));
        return ClientDto.toDto(client);

    }

    public List<ClientDto> findAll() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(ClientDto::toDto)
                .toList();
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    public void updateClient(ClientDto clientDto, Long id) {
        Client client = clientRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Client not found"));
        client.setId(clientDto.id());
        client.setFirstName(clientDto.firstName());
        client.setLastName(clientDto.lastName());
        client.setBirthday(clientDto.birthday());
        client.setChatId(clientDto.chatId());
        client.setPhone(clientDto.phone());
        client.setBonusBalance(clientDto.bonusBalance());
        clientRepository.save(client);
    }
}
