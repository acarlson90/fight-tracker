package com.aaroncarlson.repository;

import com.aaroncarlson.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    // READ
    Optional<City> findByNameIgnoreCase(String name);
    Optional<City> findByNameOrNameIgnoreCase(String nameOne, String nameTwo);
    List<City> findByNameIsNotNull();
    List<City> findByNameNot(String name);
    // Enhanced JPQL Syntax with embedded % - this keeps % out of client code
    @Query("SELECT c FROM City c WHERE c.name LIKE ?1%")
    List<City> queryByName(String name);
    List<City> findByNameLike(String name);
    List<City> findByNameLikeIgnoreCase(String name);
    List<City> findByNameLikeIgnoreCaseOrderByNameAsc(String name);
    List<City> findByNameStartingWith(String name);
    List<City> findByNameEndingWith(String name);
    List<City> findByNameContaining(String name);
    List<City> findByNameContains(String name);
    List<City> findByNameIsContaining(String name);

    // WRITE
    // Return integer is the number of affected rows
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE City c SET c.name = ?1 WHERE c.name = ?2")
    int updateCityName(String newName, String currentName);
}
