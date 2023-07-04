package org.spr.components;

import org.spr.data.Message;
import org.spr.repository.MessageRepository;
import org.spr.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DbSetup implements ApplicationListener<ContextRefreshedEvent> {

    private MessageRepository messageRepository;

    @Autowired
    public void MongoDataInitializer(MessageRepository myDocumentRepository) {
        this.messageRepository = myDocumentRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Add documents when the application context is refreshed (server startup)
        addDocuments();
    }

    public void addDocuments() {
        messageRepository.saveAll(Utils.generateRandomString(100, 200).stream().map(Message::new).toList()).blockLast();
    }
}