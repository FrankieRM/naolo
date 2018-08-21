package net.kemitix.naolo.run.meecrowave.jpa;

import net.kemitix.naolo.test.RunIT;
import net.kemitix.naolo.test.meecrowave.AbstractMeecrowaveTest;
import org.apache.meecrowave.Meecrowave;
import org.apache.meecrowave.junit5.MeecrowaveConfig;
import org.apache.meecrowave.testing.ConfigurationInject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@MeecrowaveConfig()
public class MeecrowaveJPARunIT extends AbstractMeecrowaveTest implements RunIT {

    @ConfigurationInject
    private Meecrowave.Builder config;

    @BeforeEach
    void setUp() {
        super.config(config);
    }

    @Test
    @Override
    public void getAllVets() throws IOException {
        doGetAllVetsTest();
    }

    @Test
    @Override
    public void addVet() throws IOException {
        doAddVetTest();
    }

}
