/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 Paul Campbell
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.kemitix.naolo.presenter.rest.spring;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * REST Controller for individual Veterinarians.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Slf4j
@RestController
@RequestMapping("/vet")
@RequiredArgsConstructor
public class VeterinarianController {

    private final VeterinarianAdd add;

    /**
     * List all Veterinarians endpoint.
     *
     * @param request the request
     * @return the response
     * @throws ExecutionException   if there is an error completing the request
     * @throws InterruptedException if there is an error completing the request
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> add(
            @RequestBody final AddRequest request
    ) throws ExecutionException, InterruptedException {
        log.info("add: {}, {}", request.name, request.specialisations);
        final URI uri = add.invoke(new VeterinarianAdd.Request(request.name, request.specialisations))
                .thenApply(VeterinarianAdd.Response::getId)
                .thenApply(id -> linkTo(methodOn(VeterinarianController.class).get(id)).toUri())
                .get();
        return ResponseEntity.created(uri).build();
    }

    /**
     * Get a Veterinarian endpoint.
     *
     * @param id the ID of the Vet
     * @return the response
     */
    @GetMapping("/{id}")
    ResponseEntity<Veterinarian> get(@PathVariable("id") final Long id) {
        return null;
    }

    /**
     * Request body for adding a Vet.
     */
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    static class AddRequest {
        private String name;
        private Set<VetSpecialisation> specialisations = new HashSet<>();
    }
}
