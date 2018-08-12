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

package net.kemitix.naolo.gateway.data.deltaspike;

import lombok.extern.slf4j.Slf4j;
import net.kemitix.naolo.gateway.data.common.GatewayPersistenceUnitName;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Configuration for Data module.
 *
 * @author Paul Campbell (pcampbell@kemitix.net)
 */
@Slf4j
@ApplicationScoped
public class DataConfig {

    /**
     * Produces the Persistence Unit Name.
     *
     * @return the Persistence Unit Name
     */
    @Produces
    @GatewayPersistenceUnitName
    public String gatewayUnitName() {
        final String unitName = getClass().getPackage().getName();
        log.debug("Define Gateway Unit Name: {}", unitName);
        return unitName;
    }

}
