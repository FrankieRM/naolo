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

package net.kemitix.naolo.test;

import okhttp3.*;
import org.apache.meecrowave.Meecrowave;
import org.assertj.core.api.WithAssertions;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.util.stream.IntStream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

/**
 * Base class for testing Meecrowave servers.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
public abstract class AbstractMeecrowaveTest implements WithAssertions {

    private static final OkHttpClient OK_HTTP = new OkHttpClient();

    private String base;

    /**
     * Configures the test with the base URL for the server.
     *
     * @param config the Meecrowave Builder
     */
    protected void config(final Meecrowave.Builder config) {
        base = "http://localhost:" + config.getHttpPort();
    }

    private void verifyBase() {
        assertThat(base)
                .as("call Meecrowave.config(Meecrowave.Builder) from your @BeforeEach method")
                .isNotNull();
    }

    /**
     * Make an HTTP GET request.
     *
     * @param path the Query Path
     * @return the server response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *                     problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                     remote server accepted the request before the failure.
     */
    protected Response get(final String path) throws IOException {
        return submit(request(path).get().build());
    }

    /**
     * Make an HTTP POST request.
     *
     * @param path      the Query Path
     * @param body      the request body to submit
     * @param mediaType the media type of the request body
     * @return the server response
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *                     problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                     remote server accepted the request before the failure.
     */
    protected Response post(final String path, final String body, final MediaType mediaType) throws IOException {
        return submit(request(path).post(requestBody(body, mediaType)).build());
    }

    private RequestBody requestBody(final String body, final MediaType mediaType) {
        return RequestBody.create(mediaType, body);
    }

    private Request.Builder request(final String path) {
        verifyBase();
        return new Request.Builder().url(base + path);
    }

    private Response submit(final Request request) throws IOException {
        return OK_HTTP.newCall(request).execute();
    }

    /**
     * Returns a media type for {@code string}.
     *
     * @param string the media type
     * @return the MediaType
     */
    protected MediaType mediaType(final String string) {
        return MediaType.get(string);
    }

    /**
     * Performs the test for Get All Vets.
     *
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *                     problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                     remote server accepted the request before the failure.
     */
    protected void doGetAllVetsTest() throws IOException {
        //given
        final int numberOfVets = 10;
        //when
        final Response response = get("/vets/");
        //then
        assertThat(response).returns(HttpServletResponse.SC_OK, Response::code);

        final ResponseBody body = response.body();
        assertThat(body).isNotNull();
        assertThat(body.contentType()).isEqualTo(mediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON));

        final String json = body.string();
        assertSoftly(s -> {
            IntStream.rangeClosed(1, numberOfVets).forEach(i ->
                    s.assertThat(json).contains(String.format("\"id\":%d", i)));
        });
    }

    /**
     * Performs the test for Add a Vet.
     *
     * @throws IOException if the request could not be executed due to cancellation, a connectivity
     *                     problem or timeout. Because networks can fail during an exchange, it is possible that the
     *                     remote server accepted the request before the failure.
     */
    protected void doAddVetTest() throws IOException {
        //given
        final String body = "{\"name\":\"new name\",\"specialisations\":[\"SURGERY\",\"RADIOLOGY\"]}";
        //when
        final Response response = post("/vet", body, mediaType(javax.ws.rs.core.MediaType.APPLICATION_JSON));
        //then
        assertThat(response).returns(HttpServletResponse.SC_CREATED, Response::code);

        final Headers headers = response.headers();
        assertThat(headers.names()).contains(HttpHeaders.LOCATION);
        assertThat(headers.get(HttpHeaders.LOCATION)).endsWith("/vet/11");
    }
}
