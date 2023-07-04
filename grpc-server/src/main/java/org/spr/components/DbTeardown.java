package org.spr.components;

import org.spr.repositories.ReactiveMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class DbTeardown implements ApplicationListener<ContextClosedEvent> {
    private ReactiveMessageRepository messageRepository;

    @Autowired
    public void MongoDataInitializer(ReactiveMessageRepository myDocumentRepository) {
        this.messageRepository = myDocumentRepository;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // Remove documents when the application context is closed (server shutdown)
        removeDocuments();
    }

    public void removeDocuments() {
        messageRepository.deleteAll().block();
    }
}
