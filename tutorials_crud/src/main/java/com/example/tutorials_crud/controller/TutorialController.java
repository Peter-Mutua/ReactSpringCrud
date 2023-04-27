package com.example.tutorials_crud.controller;

import com.example.tutorials_crud.response.ResponseHandler;
import com.example.tutorials_crud.model.Tutorial;
import com.example.tutorials_crud.repository.TutorialRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/v1")
public class TutorialController {

    final
    TutorialRepository tutorialRepository;

    public TutorialController(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    @GetMapping("/get/tutorials")
    public ResponseEntity<Object> getAllTutorials(@RequestParam(required = false) String title) {
        try {
            List<Tutorial> tutorials = new ArrayList<Tutorial>();

            if (title == null) {
                tutorialRepository.findAll().parallelStream().forEach(tutorials::add);
            } else {
                tutorialRepository.findByTitleContaining(title).parallelStream().forEach(tutorials::add);
            }

            if (tutorials.isEmpty()) {
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                return ResponseHandler.generateResponse("Nothing found to display!", HttpStatus.NO_CONTENT, null);
            }

//            return new ResponseEntity<>(tutorials, HttpStatus.OK);
            return ResponseHandler.generateResponse("Data fetched successfully!", HttpStatus.OK, tutorials);
        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping("/get/tutorial/{id}")
    public ResponseEntity<Object> getTutorialById(@PathVariable("id") long id) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            return ResponseHandler.generateResponse("Data retrieved successfully!", HttpStatus.OK, tutorialData.get());
        } else {
            return ResponseHandler.generateResponse("Data doesn't exist!", HttpStatus.NOT_FOUND, null);
        }
    }

    @PostMapping("/add/tutorial")
    public ResponseEntity<Object> createTutorial(@RequestBody Tutorial tutorial) {
        try {
            Tutorial _tutorial = tutorialRepository.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
            return ResponseHandler.generateResponse("Submitted Successfully!", HttpStatus.CREATED, _tutorial);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PutMapping("/update/tutorial/{id}")
    public ResponseEntity<Object> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
        Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

        if (tutorialData.isPresent()) {
            Tutorial _tutorial = tutorialData.get();
            _tutorial.setTitle(tutorial.getTitle());
            _tutorial.setDescription(tutorial.getDescription());
            _tutorial.setPublished(tutorial.isPublished());

            return ResponseHandler.generateResponse(
                    "Updated successfully!",
                    HttpStatus.OK,
                    tutorialRepository.save(_tutorial));
        } else {
            return ResponseHandler.generateResponse("Not Updated!", HttpStatus.UNAUTHORIZED, null);
        }
    }

    @DeleteMapping("/delete/tutorial/{id}")
    public ResponseEntity<Object> deleteTutorial(@PathVariable("id") long id) {
        try {
            tutorialRepository.deleteById(id);
            return ResponseHandler.generateResponse("Deleted Successfully!", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @DeleteMapping("/delete/all/tutorials")
    public ResponseEntity<Object> deleteAllTutorials() {
        try {
            tutorialRepository.deleteAll();
            return ResponseHandler.generateResponse("All Tutorials Deleted successfully!", HttpStatus.OK, null);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @GetMapping("/get/all/tutorials/published")
    public ResponseEntity<Object> findByPublished() {
        try {
            List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

            if (tutorials.isEmpty()) {
                return ResponseHandler.generateResponse("No information available!", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse("Data available!", HttpStatus.OK, tutorials);
            }
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

}
