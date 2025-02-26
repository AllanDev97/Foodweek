package com.foodweek.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.foodweek.demo.model.Meal;

@RepositoryRestResource(collectionResourceRel = "meals", path = "meals")
public interface MealRepository extends JpaRepository<Meal, Long>{

}
