package com.app.feegnna.repository;

import com.app.feegnna.model.Item;
import com.app.feegnna.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByClaimedTrue(); // claimed items
    List<Item> findByClaimedFalse(); // unclaimed items
    List<Item> findByClaimedByName(String name); // items claimed by the logged user


}
