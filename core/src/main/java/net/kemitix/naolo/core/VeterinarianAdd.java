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

package net.kemitix.naolo.core;

import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Use-case to add a {@link Veterinarian}.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public class VeterinarianAdd
        implements UseCase<VeterinarianAdd.Request, VeterinarianAdd.Response> {

    private final VeterinarianRepository repository;

    /**
     * Create a new {@VeterinarianAdd} use-case.
     *
     * @param repository the Veterinarian Repository
     */
    public VeterinarianAdd(final VeterinarianRepository repository) {
        this.repository = repository;
    }

    @Override
    public final CompletableFuture<Response> invoke(final Request request) {
        return CompletableFuture.supplyAsync(() -> () ->
                repository.create(request.name, request.specialisations)
                        .getId());
    }

    /**
     * Request for creating a new {@link Veterinarian}.
     */
    public static class Request {
        private final String name;
        private final Set<VetSpecialisation> specialisations;

        /**
         * Constructor for Add a Veterinarian Request.
         *
         * @param name            the name of the new Veterinarian
         * @param specialisations any specialisations of the new veterinarian
         */
        public Request(final String name, final Set<VetSpecialisation> specialisations) {
            this.name = name;
            this.specialisations = new HashSet<>(specialisations);
        }
    }

    /**
     * Response.
     */
    public interface Response {

        /**
         * The Id of the created Veterinarian.
         *
         * @return the veterinarian Id
         */
        Long getId();
    }
}
