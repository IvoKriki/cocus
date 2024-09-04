package com.kriki.ivo.cocus.tec.test.controller;

import com.kriki.ivo.cocus.tec.test.service.WordSquareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/word-square")
public class WordSquareController {

    private final WordSquareService wordSquareService;

    public WordSquareController(WordSquareService wordSquareService) {
        this.wordSquareService = wordSquareService;
    }

    @PostMapping
    public ResponseEntity<List<String>> generateWordSquare(@RequestParam String letters) {
        var wordSquares = wordSquareService.generateWordSquare(letters);
        if (wordSquares.isEmpty()) {
            return ResponseEntity.ok(List.of("No valid word square found"));
        }
        return ResponseEntity.ok(wordSquares);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.badRequest().body("ERROR: " + ex.getMessage());
    }
}
