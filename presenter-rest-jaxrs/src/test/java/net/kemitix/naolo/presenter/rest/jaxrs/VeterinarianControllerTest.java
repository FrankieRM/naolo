package net.kemitix.naolo.presenter.rest.jaxrs;

import net.jqwik.api.*;
import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.core.VeterinarianRepository;
import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class VeterinarianControllerTest implements WithAssertions {

    private final VeterinarianRepository repository = mock(VeterinarianRepository.class);
    private final VeterinarianAdd addVet = new VeterinarianAdd(repository);
    private final VeterinarianController controller = new VeterinarianController(addVet);

    @Test
    void defaultConstructorForController() {
        assertThat(new VeterinarianController()).isNotNull();
    }

    @Provide
    static Arbitrary<Set<VetSpecialisation>> specialisations() {
        return Arbitraries.of(VetSpecialisation.class)
                .set().ofMinSize(0).ofMaxSize(VetSpecialisation.values().length);
    }

    @Property
    void canAddAVet(
            @ForAll final Long id,
            @ForAll final String name,
            @ForAll("specialisations") final Set<VetSpecialisation> specialisations
    ) throws ExecutionException, InterruptedException {
        //given
        final Veterinarian veterinarian = new Veterinarian(id, name, specialisations);
        given(repository.create(eq(name), any())).willReturn(veterinarian);
        //when
        final Response response = controller.add(name, specialisations);
        //then
        assertThat(response.getStatusInfo().toEnum()).isEqualTo(Response.Status.CREATED);
        final URI uri = response.getLocation();
        assertThat(uri).hasPath("/vet/" + id);
    }

}