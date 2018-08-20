package net.kemitix.naolo.run.meecrowave.deltaspike;

import net.kemitix.naolo.core.UseCase;
import net.kemitix.naolo.core.VeterinarianAdd;
import net.kemitix.naolo.core.VeterinarianRepository;
import net.kemitix.naolo.core.VeterinariansListAll;
import net.kemitix.naolo.test.UseCasesTest;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class MeecrowaveDeltaspikeUseCasesTest implements WithAssertions, UseCasesTest {

    private final UseCases useCases = new UseCases();

    private final VeterinarianRepository veterinarianRepository = mock(VeterinarianRepository.class);

    @Override
    @Test
    public void hasListAllVeterinariansUseCase() {
        //given
        final VeterinariansListAll useCase = useCases.veterinariansListAll(veterinarianRepository);
        //then
        assertThat(useCase).isInstanceOf(UseCase.class);
    }

    @Override
    @Test
    public void hasAddVeterinarianUseCase() {
        //given
        final VeterinarianAdd useCase = useCases.veterinarianAdd(veterinarianRepository);
        //then
        assertThat(useCase).isInstanceOf(UseCase.class);
    }
}
