package com.foodweek.demo;

import com.foodweek.demo.model.Message;
import com.foodweek.demo.model.User;
import com.foodweek.demo.repository.MessageRepository;
import com.foodweek.demo.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPerformCrudOnMessage() {
        // CREATE utilisateurs
        User sender = new User();
        sender.setUsername("alice");
        sender.setEmail("alice@example.com");
        sender.setPassword("pass");
        userRepository.save(sender);

        User receiver = new User();
        receiver.setUsername("bob");
        receiver.setEmail("bob@example.com");
        receiver.setPassword("pass");
        userRepository.save(receiver);

        // CREATE message
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent("Hello Bob!");
        message.setSentAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);
        assertThat(savedMessage.getId()).isNotNull();
        assertThat(savedMessage.getContent()).isEqualTo("Hello Bob!");

        // READ
        Optional<Message> found = messageRepository.findById(savedMessage.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getSender().getUsername()).isEqualTo("alice");

        // UPDATE
        savedMessage.setContent("Updated content");
        Message updated = messageRepository.save(savedMessage);
        assertThat(updated.getContent()).isEqualTo("Updated content");

        // DELETE
        messageRepository.deleteById(updated.getId());
        Optional<Message> deleted = messageRepository.findById(updated.getId());
        assertThat(deleted).isEmpty();
    }
}
