package com.app.feegnna.controllers;

import com.app.feegnna.model.Item;
import com.app.feegnna.model.User;
import com.app.feegnna.repository.ItemRepository;
import com.app.feegnna.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemController(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/list")
    public List<Item> index() {
        return itemRepository.findAll();
    }

    @GetMapping("/claimed")
    public List<Item> claimedItems() {
        return itemRepository.findByClaimedTrue();
    }

    @GetMapping("/unclaimed")
    public List<Item> unclaimedItems() {
        return itemRepository.findByClaimedFalse();
    }

    @GetMapping("/claiming")
    public ResponseEntity<List<Item>> currentUserClaimedItems() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        return optionalUser.map(user1 -> ResponseEntity.ok().body(itemRepository.findByClaimedByName(user1.getPrenom() + " " + user1.getNom())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Item store(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> show(@PathVariable(name = "id") Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem
                .map(item -> ResponseEntity.ok().body(item))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> update(@PathVariable(name = "id") Long id, @RequestBody Item item) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem
                .map(item1 -> {
                    item1.setLabel(item.getLabel());
                    item1.setDescription(item.getDescription());
                    item1.setFoundAt(item.getFoundAt());
                    item1.setPlace(item.getPlace());
                    item1.setCategory(item.getCategory());
                    item1.setClaimed(item.getClaimed());
                    item1.setClaimedByName(item.getClaimedByName());
                    item1.setClaimedByEmail(item.getClaimedByEmail());
                    item1.setClaimedByPhone(item.getClaimedByPhone());
                    itemRepository.save(item1);
                    return ResponseEntity.ok().body(item1);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> destroy(@PathVariable(name = "id") Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem
                .map(item -> {
                    itemRepository.delete(item);
                    return ResponseEntity.ok().body(item);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/claim")
    public ResponseEntity<Item> claimItem(@PathVariable(name = "id") Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());
        return optionalUser.map(user1 -> optionalItem.map(item -> {
            item.setClaimed(true);
            item.setClaimedByName(user1.getPrenom() + " " + user1.getNom());
            item.setClaimedByEmail(user1.getEmail());
            item.setClaimedByPhone(user1.getTelephone());
            itemRepository.save(item);
            return ResponseEntity.ok().body(item);
        }).orElseGet(() -> ResponseEntity.notFound().build())).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
