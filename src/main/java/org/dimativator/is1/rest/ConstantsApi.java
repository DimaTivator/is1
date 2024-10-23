package org.dimativator.is1.rest;

import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/api/v1")
public interface ConstantsApi {
    @GetMapping("/colors")
    ResponseEntity<List<Color>> getAllColors();

    @GetMapping("/countries")
    ResponseEntity<List<Country>> getAllCountries();
}
