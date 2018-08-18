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

package net.kemitix.naolo.presenter.rest.jaxrs;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.entities.VetSpecialisation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * REST Controller for Veterinarians.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Slf4j
@Path("/vet")
@ApplicationScoped
public class VeterinarianController {

    private final VeterinarianAdd addUseCase;

    /**
     * Default constructor.
     */
    VeterinarianController() {
        addUseCase = null;
    }

    /**
     * CDI Constructor.
     *
     * @param addUseCase the UseCase for Add a Veterinarian
     */
    @Inject
    VeterinarianController(final VeterinarianAdd addUseCase) {
        this.addUseCase = Objects.requireNonNull(addUseCase);
    }

    /**
     * Add a new Veterinarian.
     *
     * @param addRequest the add request
     * @return the response
     * @throws ExecutionException   if there is an error completing the request
     * @throws InterruptedException if there is an error completing the request
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(
            final AddRequest addRequest
    ) throws ExecutionException, InterruptedException {
        log.info("add: {}, {}", addRequest.name, addRequest.specialisations);
        final VeterinarianAdd.Request request =
                new VeterinarianAdd.Request(addRequest.name, addRequest.specialisations);
        return addUseCase.invoke(request)
                .thenApply(VeterinarianAdd.Response::getId)
                .thenApply(String::valueOf)
                .thenApply(id -> "/vet/" + id)
                .thenApply(URI::create)
                .thenApply(Response::created)
                .thenApply(Response.ResponseBuilder::build)
                .get();
    }

    /**
     * Request body for adding a Vet.
     */
    @Setter
    @NoArgsConstructor
    static class AddRequest {
        private String name;
        private Set<VetSpecialisation> specialisations;
    }
}
