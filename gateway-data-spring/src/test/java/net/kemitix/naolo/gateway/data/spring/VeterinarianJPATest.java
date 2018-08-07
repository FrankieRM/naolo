package net.kemitix.naolo.gateway.data.spring;

import net.jqwik.api.*;
import org.assertj.core.api.WithAssertions;

import java.util.HashSet;
import java.util.Set;

class VeterinarianJPATest implements WithAssertions {

    @Provide
    static Arbitrary<VeterinarianJPA> jpa() {
        final Arbitrary<Long> ids = Arbitraries.longs();
        final Arbitrary<String> names = Arbitraries.strings();
        final Arbitrary<Set<String>> specialisations = Arbitraries.strings()
                .set().ofMinSize(0).ofMaxSize(3);
        return Combinators.combine(ids, names, specialisations)
                .as(VeterinarianJPA::new);
    }

    @Provide
    static Arbitrary<Set<String>> stringSet() {
        return Arbitraries.strings()
                .set().ofMinSize(0).ofMaxSize(3);
    }

    @Property
    void requireName(
            @ForAll final Long id,
            @ForAll("stringSet") final Set<String> specialisations
    ) {
        assertThatNullPointerException().isThrownBy(() ->
                new VeterinarianJPA(id, null, specialisations));
    }

    @Property
    void requireSpecialisation(
            @ForAll final Long id,
            @ForAll final String name
    ) {
        assertThatNullPointerException().isThrownBy(() ->
                new VeterinarianJPA(id, name, null));
    }

    @Property
    void hasGetters(
            @ForAll final Long id,
            @ForAll final String name,
            @ForAll("stringSet") final Set<String> specialisations
    ) {
        //given
        final VeterinarianJPA jpa = new VeterinarianJPA(id, name, specialisations);
        //then
        assertThat(jpa)
                .returns(id, VeterinarianJPA::getId)
                .returns(name, VeterinarianJPA::getName)
                .returns(specialisations, VeterinarianJPA::getSpecialisations);
    }

    @Property
    void specialisationsAreImmutable(
            @ForAll final Long id,
            @ForAll final String name,
            @ForAll("stringSet") final Set<String> specialisations,
            @ForAll("stringSet") final Set<String> alternate
    ) {
        //given
        final VeterinarianJPA jpa = new VeterinarianJPA(id, name, new HashSet<>(specialisations));
        final Set<String> set = jpa.getSpecialisations();
        // check that set and alternate are not already the same
        Assume.that(!set.equals(alternate));
        //when
        set.addAll(alternate);
        //then
        assertThat(jpa.getSpecialisations())
                .as("Specialisations in JPA should match the original set of specialisations")
                .containsExactlyElementsOf(specialisations);
    }
}