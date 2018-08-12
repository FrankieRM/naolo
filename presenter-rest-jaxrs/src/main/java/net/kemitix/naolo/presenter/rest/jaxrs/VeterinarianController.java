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

import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.entities.VetSpecialisation;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
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
     * @param name            the name of the new Veterinarian
     * @param specialisations the specialisations of the new veterinarian
     * @return the response
     * @throws ExecutionException   if there is an error completing the request
     * @throws InterruptedException if there is an error completing the request
     */
    @POST
    public final Response add(
            @QueryParam("name") final String name,
            @QueryParam("specialisations") final Set<VetSpecialisation> specialisations
    ) throws ExecutionException, InterruptedException {
        return Response.created(
                URI.create("/vet/" + addUseCase.invoke(new VeterinarianAdd.Request(name, specialisations))
                        .thenApply(VeterinarianAdd.Response::getId)
                        .get()))
                .build();
    }

}
