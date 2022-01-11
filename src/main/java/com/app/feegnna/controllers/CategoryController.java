package com.app.feegnna.controllers;

import com.app.feegnna.model.Category;
import com.app.feegnna.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/categories")
public class CategoryController {
    final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/list")
    public List<Category> index() {
        return categoryRepository.findAll();
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Category store(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> show(@PathVariable(name = "id") Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory
                .map(category -> ResponseEntity.ok().body(category))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable(name = "id") Long id, @RequestBody Category category) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory
                .map(category1 -> {
                    category1.setLabel(category.getLabel());
                    categoryRepository.save(category1);
                    return ResponseEntity.ok().body(category1);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Category> destroy(@PathVariable(name = "id") Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.ok().body(category);
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
