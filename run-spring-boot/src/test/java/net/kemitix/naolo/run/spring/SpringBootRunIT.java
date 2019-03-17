package net.kemitix.naolo.run.spring;

import net.kemitix.naolo.test.RunIT;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                Main.class
        })
@AutoConfigureMockMvc
class SpringBootRunIT implements WithAssertions, RunIT {

    @Autowired
    MockMvc mvc;

    @Test
    void contextStarts() {
        assertThat(true).isTrue();
    }

    @Override
    @Test
    public void getAllVets() throws Exception {
        mvc.perform(get("/vets/"))
                .andExpect(status().isOk())
                .andExpect(content().string(new AssertionMatcher<String>() {
                    @Override
                    public void assertion(String actual) throws AssertionError {
                        assertSoftly(s -> IntStream.rangeClosed(1, 10).forEach(i ->
                                s.assertThat(actual).contains(format("{\"id\":%d,", i))));
                    }
                }));
    }

    @Override
    @Test
    public void addVet() throws Exception {
        mvc.perform(post("/vet")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Name\",\"specialisations\":[\"SURGERY\", \"DENTISTRY\"]}"))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("/vet/11"));
    }

}
