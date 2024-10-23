package org.dimativator.is1.rest.impl;

import org.dimativator.is1.model.Color;
import org.dimativator.is1.model.Country;
import org.dimativator.is1.rest.ConstantsApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ConstantsApiImpl implements ConstantsApi {
    @Override
    public ResponseEntity<List<Color>> getAllColors() {
        return ResponseEntity.ok(Arrays.stream(Color.values()).toList());
    }

    @Override
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(Arrays.stream(Country.values()).toList());
    }
}
