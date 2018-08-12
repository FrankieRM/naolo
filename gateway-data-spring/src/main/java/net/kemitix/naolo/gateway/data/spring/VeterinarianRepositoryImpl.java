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

package net.kemitix.naolo.gateway.data.spring;

import lombok.extern.slf4j.Slf4j;
import net.kemitix.naolo.core.VeterinarianRepository;
import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Spring Gateway Repository for Veterinarians.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Slf4j
@Repository
class VeterinarianRepositoryImpl implements VeterinarianRepository {

    private final VeterinarianRepositorySpring repository;

    /**
     * Constructor.
     *
     * @param repository the Spring Data repository
     */
    VeterinarianRepositoryImpl(final VeterinarianRepositorySpring repository) {
        log.debug("Create Veterinarian Repository");
        this.repository = repository;
    }

    /**
     * Converts VeterinarianJPA to core entity type.
     */
    private static Veterinarian fromJPA(final VeterinarianJPA source) {
        return Veterinarian.create(
                source.getId(),
                source.getName(),
                source.getSpecialisations());
    }

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
        final VeterinarianJPA saved = repository.save(new VeterinarianJPA(null, name, specialisations));
        log.debug("created: Created Veterinarian id:{} '{}'", saved.getId(), name);
        return fromJPA(saved);
    }

}
