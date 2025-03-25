package com.foodweek.demo;

import com.foodweek.demo.model.Notification;
import com.foodweek.demo.model.User;
import com.foodweek.demo.repository.NotificationRepository;
import com.foodweek.demo.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPerformCrudOnNotification() {
        // CREATE utilisateur
        User user = new User();
        user.setUsername("lucie");
        user.setEmail("lucie@example.com");
        user.setPassword("1234");
        userRepository.save(user);

        // CREATE notification
        Notification notif = new Notification();
        notif.setUser(user);
        notif.setType("INFO");
        notif.setMessage("Votre plan de repas a été mis à jour.");
        notif.setRead(false);

        Notification savedNotif = notificationRepository.save(notif);
        assertThat(savedNotif.getId()).isNotNull();
        assertThat(savedNotif.getType()).isEqualTo("INFO");
        assertThat(savedNotif.isRead()).isFalse();

        // READ
        Optional<Notification> found = notificationRepository.findById(savedNotif.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUsername()).isEqualTo("lucie");

        // UPDATE
        savedNotif.setRead(true);
        Notification updated = notificationRepository.save(savedNotif);
        assertThat(updated.isRead()).isTrue();

        // DELETE
        notificationRepository.deleteById(updated.getId());
        Optional<Notification> deleted = notificationRepository.findById(updated.getId());
        assertThat(deleted).isEmpty();
    }
}
