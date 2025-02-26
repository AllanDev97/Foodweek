package com.foodweek.demo.controller;

import org.springframework.data.rest.webmvc.RepositoryRestController;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@RepositoryRestController
public class MealController {

	@PersistenceContext
    private EntityManager em;

}
