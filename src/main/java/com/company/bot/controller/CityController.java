package com.company.bot.controller;

import com.company.bot.controller.form.CityCreateForm;
import com.company.bot.controller.form.CityUpdateForm;
import com.company.bot.model.City;
import com.company.bot.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @PostMapping(value = "/create")
    public ResponseEntity<City> createCity(@RequestBody CityCreateForm form) {
        return new ResponseEntity<>(cityService.create(form), HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/update")
    public ResponseEntity<Void> updateCity(@PathVariable Long id, @RequestBody CityUpdateForm form) {
        cityService.update(id, form);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/remove")
    public ResponseEntity<Void> removeCity(@PathVariable Long id) {
        cityService.remove(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCity() {
        return new ResponseEntity<>(cityService.getAll(), HttpStatus.OK);
    }
}
