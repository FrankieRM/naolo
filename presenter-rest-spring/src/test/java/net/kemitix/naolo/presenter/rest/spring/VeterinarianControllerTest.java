package net.kemitix.naolo.presenter.rest.spring;

import net.jqwik.api.*;
import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.core.VeterinarianRepository;
import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;
import org.assertj.core.api.WithAssertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class VeterinarianControllerTest implements WithAssertions {

    private final VeterinarianRepository repository = mock(VeterinarianRepository.class);
    private final VeterinarianAdd veterinarianAdd = new VeterinarianAdd(repository);
    private final VeterinarianController controller = new VeterinarianController(veterinarianAdd);

    @Provide
    static Arbitrary<Set<VetSpecialisation>> specialisations() {
        return Arbitraries.of(VetSpecialisation.class)
                .set().ofMinSize(0).ofMaxSize(VetSpecialisation.values().length);
    }

    @Property
    void invokesAdd(
            @ForAll final Long id,
            @ForAll final String name,
            @ForAll("specialisations") final Set<VetSpecialisation> specialisations
    ) throws ExecutionException, InterruptedException {
        //given
        final Veterinarian veterinarian = new Veterinarian(id, name, specialisations);
        given(repository.create(name, specialisations)).willReturn(veterinarian);
        //when
        final ResponseEntity<Void> responseEntity = controller.add(name, specialisations);
        //then
        assertThat(responseEntity.getStatusCode()).isSameAs(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation())
                .isEqualTo(URI.create("/vet/" + id));
    }
}