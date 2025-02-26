package com.foodweek.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.foodweek.demo.model.Ingredient;

@RepositoryRestResource(collectionResourceRel = "ingredients", path = "ingredients")
public interface IngredientReposiroty extends JpaRepository<Ingredient, Long>{

}
	