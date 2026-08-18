package com.example.LoyaltyBot.handler.registration;

import com.example.LoyaltyBot.entity.Client;
import com.example.LoyaltyBot.entity.RegistrationState;
import com.example.LoyaltyBot.service.ClientService;
import com.example.LoyaltyBot.service.NotificationService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Component
public class AskPhoneRegistrationHandler implements RegistrationHandler {

    private final ClientService clientService;


    public AskPhoneRegistrationHandler(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public SendMessage handle(Message message, Client client) {

        if (message.hasContact()) {

            client.setPhone(message.getContact().getPhoneNumber());
            client.setRegistrationState(RegistrationState.REGISTERED);
            clientService.updateClient(client);
            return sendMessage(message.getChatId(), sendWelcomeMessage(client.getFirstName()));
        }
        return sendMessage(message.getChatId(), "Не удалось получить номер ");
    }

    private SendMessage sendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public String sendWelcomeMessage( String firstName) {
        return String.format("""
                         <b>Добро пожаловать в программу лояльности!</b>
                        
                        %s, мы рады приветствовать вас!
                        
                        
                        Как это работает:
                         1% от каждой покупки возвращается бонусами
                         Накопительные баллы можно использовать для оплаты
                         Повышайте уровень и получайте больше!
                        
                        Желаем приятных покупок!
                        """,
                firstName);
    }
}
