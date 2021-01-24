package com.company.bot.service;

import com.company.bot.controller.form.CityCreateForm;
import com.company.bot.controller.form.CityUpdateForm;
import com.company.bot.model.City;
import com.company.bot.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public City create(CityCreateForm form) {
        log.info("create()");
        City city = new City();
        city.setName(form.getName());
        city.setDescription(form.getDescription());
        return save(city);
    }

    public void update(Long id, CityUpdateForm form) {
        log.info("update()");
        Optional<City> cityOptional = cityRepository.findById(id);
        cityOptional.ifPresentOrElse(city -> {
            city.setName(form.getName());
            city.setDescription(form.getDescription());
            save(city);
        }, () -> {
            log.error("City with id{} doesn't exist!", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City with such id doesn't exist!");
        });
    }

    public void remove(Long id) {
        log.info("remove()");
        Optional<City> cityOptional = cityRepository.findById(id);
        cityOptional.ifPresentOrElse(this::delete, () -> {
            log.error("City with id{} doesn't exist!", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City with such id doesn't exist!");
        });
    }

    public List<City> getAll() {
        log.info("getAll()");
        return cityRepository.findAll();
    }

    private City save(City city) {
        log.info("save()");
        return cityRepository.save(city);
    }

    private void delete(City city) {
        log.info("delete()");
        cityRepository.delete(city);
    }

}
