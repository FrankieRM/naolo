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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kemitix.naolo.entities.VetSpecialisation;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * JPA Entity for Veterinarian.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Entity
@Table(name = "veterinarians")
@Getter
@NoArgsConstructor
@AllArgsConstructor
class VeterinarianJPA {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ElementCollection
    @CollectionTable(name = "veterinarian_specialisations")
    @Enumerated(EnumType.STRING)
    private Set<VetSpecialisation> specialisations;

    Set<VetSpecialisation> getSpecialisations() {
        return Optional.ofNullable(specialisations)
                .map(HashSet::new)
                .orElseGet(HashSet::new);
    }
}
