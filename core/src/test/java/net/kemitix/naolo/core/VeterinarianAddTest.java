package net.kemitix.naolo.core;

import net.jqwik.api.*;
import net.kemitix.naolo.entities.VetSpecialisation;
import org.assertj.core.api.WithAssertions;

import java.util.Set;
import java.util.concurrent.ExecutionException;

class VeterinarianAddTest implements WithAssertions {

    @Property
    void addAVeterinarian(
            @ForAll Long id,
            @ForAll String name,
            @ForAll("specialisation") Set<VetSpecialisation> specialisations
    ) throws ExecutionException, InterruptedException {
        //given
        final MockVeterinarianRepository repository = new MockVeterinarianRepository();
        final VeterinarianAdd veterinarianAdd = new VeterinarianAdd(repository);
        //when
        final VeterinarianAdd.Response response =
                veterinarianAdd.invoke(new VeterinarianAdd.Request(name, specialisations)).get();
        //then
        assertThat(response.getId()).isEqualTo(1);
    }

    @Provide
    static Arbitrary<Set<VetSpecialisation>> specialisation() {
        return Arbitraries.of(VetSpecialisation.class)
                .set().ofMinSize(0).ofMaxSize(VetSpecialisation.values().length);
    }
}