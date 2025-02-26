package com.foodweek.demo.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "meal_plans", path = "meal_plans")
public class MealPlanRepository {

}
