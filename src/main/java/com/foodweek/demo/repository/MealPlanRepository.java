package com.foodweek.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.foodweek.demo.model.MealPlan;

@RepositoryRestResource(collectionResourceRel = "meal_plans", path = "meal_plans")
public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

}
