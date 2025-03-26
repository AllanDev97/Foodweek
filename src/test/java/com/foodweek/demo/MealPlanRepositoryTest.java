package com.foodweek.demo;

import com.foodweek.demo.model.MealPlan;
import com.foodweek.demo.model.User;
import com.foodweek.demo.repository.MealPlanRepository;
import com.foodweek.demo.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MealPlanRepositoryTest {

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldPerformCrudOnMealPlan() {
        // CREATE - enregistrer un utilisateur
        User user = new User();
        user.setUsername("john");
        user.setEmail("john@example.com");
        user.setPassword("secret");
        userRepository.save(user);

        // CREATE - enregistrer un meal plan
        MealPlan plan = new MealPlan();
        plan.setUser(user);
        plan.setWeekNumber(10);
        MealPlan savedPlan = mealPlanRepository.save(plan);

        assertThat(savedPlan.getId()).isNotNull();
        assertThat(savedPlan.getWeekNumber()).isEqualTo(10);

        // READ
        Optional<MealPlan> found = mealPlanRepository.findById(savedPlan.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUser().getUsername()).isEqualTo("john");

        // UPDATE
        savedPlan.setWeekNumber(12);
        MealPlan updated = mealPlanRepository.save(savedPlan);
        assertThat(updated.getWeekNumber()).isEqualTo(12);

        // DELETE
        mealPlanRepository.deleteById(updated.getId());
        Optional<MealPlan> deleted = mealPlanRepository.findById(updated.getId());
        assertThat(deleted).isEmpty();
    }
}
