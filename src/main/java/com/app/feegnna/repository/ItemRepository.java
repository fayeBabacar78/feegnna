package com.app.feegnna.repository;

import com.app.feegnna.model.Category;
import com.app.feegnna.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByClaimedTrue(); // claimed items
    List<Item> findByClaimedFalse(); // unclaimed items
    List<Item> findByClaimedByName(String name); // items claimed by the logged user
    List<Item> findByClaimedFalseAndCategory(Category category); // items claimed by the logged user


}
