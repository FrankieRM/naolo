package net.kemitix.naolo.core;

import net.kemitix.naolo.entities.VetSpecialisation;
import net.kemitix.naolo.entities.Veterinarian;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class MockVeterinarianRepository implements VeterinarianRepository {

    private final List<Veterinarian> veterinarians = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    @Override
    public Stream<Veterinarian> findAll() {
        return veterinarians.stream();
    }

    @Override
    public Veterinarian create(String name, Set<VetSpecialisation> specialisations) {
        final Veterinarian veterinarian = new Veterinarian(idCounter.incrementAndGet(), name, specialisations);
        veterinarians.add(veterinarian);
        return veterinarian;
    }
}
