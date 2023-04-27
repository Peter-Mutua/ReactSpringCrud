package com.example.tutorials_crud.controller;

import com.example.tutorials_crud.model.Tutorial;
import com.example.tutorials_crud.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/v1")
public class TutorialController {

    @Autowired
    TutorialRepository tutorialRepository;

    @GetMapping("/get/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();

            if (title == null) {
                tutorialRepository.findAll().parallelStream().forEach(tutorials::add);
            } else {
                tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);
            }

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
    }

    @GetMapping("/get/tutorial/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {}

    @PostMapping("/add/tutorial")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {}

    @PutMapping("/update/tutorial/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {}

    @DeleteMapping("/delete/tutorial/{id}")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {}

    @DeleteMapping("/delete/all/tutorials")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {}

    @GetMapping("/get/all/tutorials/published")
    public ResponseEntity<List<Tutorial>> findByPublished() {}

}
