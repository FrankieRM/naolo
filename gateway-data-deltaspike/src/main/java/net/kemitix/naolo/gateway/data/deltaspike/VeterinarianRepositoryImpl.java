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

package net.kemitix.naolo.gateway.data.deltaspike;

import lombok.extern.slf4j.Slf4j;
import net.kemitix.naolo.core.VeterinarianRepository;
import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * DeltaSpike Data Gateway Repository for Veterinarians.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Slf4j
@ApplicationScoped
class VeterinarianRepositoryImpl implements VeterinarianRepository {

    private final VeterinarianRepositoryDeltaSpike repository;

    /**
     * Default constructor.
     */
    VeterinarianRepositoryImpl() {
        repository = null;
    }

    /**
     * CDI Constructor.
     *
     * @param repository the DeltaSpike Veterinarians Repository
     */
    @Inject
    VeterinarianRepositoryImpl(final VeterinarianRepositoryDeltaSpike repository) {
        log.debug("Create Veterinarian Repository");
        this.repository = Objects.requireNonNull(repository, "DeltaSpike Veterinarian Repository");
    }

    private static Veterinarian fromJPA(final VeterinarianJPA source) {
        log.trace("fromJPA: {} - {}", source.getId(), source.getName());
        return Veterinarian.create(
                source.getId(),
                source.getName(),
                source.getSpecialisations().stream()
                        .map(VetSpecialisationJPA::getValue)
                        .collect(Collectors.toSet()));
    }

    /**
     * Find all Veterinarians.
     *
     * @return a Stream of Veterinarians
     */
    @Override
    public Stream<Veterinarian> findAll() {
        log.debug("findAll: Find all veterinarians");
        final List<VeterinarianJPA> all = repository.findAll();
        log.info("findAll: Found {} veterinarians", all.size());
        return all.stream()
                .map(VeterinarianRepositoryImpl::fromJPA);
    }

    @Override
    public Veterinarian create(final String name, final Set<VetSpecialisation> specialisations) {
        log.debug("create: Creating Veterinarian '{}' with specialisations {}", name, specialisations);
        final Set<VetSpecialisationJPA> specialisationsJPA = specialisations.stream()
                .map(VetSpecialisationJPA::new).collect(Collectors.toSet());
        final VeterinarianJPA veterinarianJPA = new VeterinarianJPA(null, name, specialisationsJPA);
        final VeterinarianJPA saved = repository.save(veterinarianJPA);
        log.debug("created: Created Veterinarian id:{} '{}'", saved.getId(), name);
        return fromJPA(saved);
    }

}
