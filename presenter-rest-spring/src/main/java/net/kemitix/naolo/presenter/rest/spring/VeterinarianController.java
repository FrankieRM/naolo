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

import lombok.RequiredArgsConstructor;
import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.entities.VetSpecialisation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * REST Controller for individual Veterinarians.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@RestController
@RequestMapping("/vet")
@RequiredArgsConstructor
public class VeterinarianController {

    private final VeterinarianAdd add;

    /**
     * List all Veterinarians endpoint.
     *
     * @param name            the name of the new veterinarian
     * @param specialisations the specialisations of the new veterinarian
     * @return the response
     * @throws ExecutionException   if there is an error completing the request
     * @throws InterruptedException if there is an error completing the request
     */
    @PostMapping
    ResponseEntity<Void> add(
            @RequestParam("name") final String name,
            @RequestParam("specialisations") final Set<VetSpecialisation> specialisations
    ) throws ExecutionException, InterruptedException {
        final VeterinarianAdd.Request request = new VeterinarianAdd.Request(name, specialisations);
        final URI uri = add.invoke(request)
                .thenApply(VeterinarianAdd.Response::getId)
                .thenApply(id -> URI.create("/vet/" + id))
                .get();
        return ResponseEntity.created(uri).build();
    }

}
