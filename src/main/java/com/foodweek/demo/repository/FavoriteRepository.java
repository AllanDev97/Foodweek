package com.foodweek.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.foodweek.demo.model.Favorite;

@RepositoryRestResource(collectionResourceRel = "favorites", path = "favorites")
public interface FavoriteRepository extends JpaRepository <Favorite, Long>{

}
