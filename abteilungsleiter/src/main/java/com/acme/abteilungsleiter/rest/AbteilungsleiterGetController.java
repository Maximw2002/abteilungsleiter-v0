/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.abteilungsleiter.rest;

import com.acme.abteilungsleiter.service.AbteilungsleiterReadService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.acme.abteilungsleiter.rest.AbteilungsleiterGetController.REST_PATH;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die Methoden
 * der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/AbteilungsleiterGetController.svg" alt="Klassendiagramm">
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * <a href="mailto:wema1123@h-ka.de">Maxim Weidler</a>
 */
@RestController
@RequestMapping(REST_PATH)
@OpenAPIDefinition(info = @Info(title = "Abteilungsleiter API", version = "v3"))
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("java:S1075")
class AbteilungsleiterGetController {

    /**
     * Basispfad für die REST-Schnittstelle.
     */
    static final String REST_PATH = "/rest";

    /**
     * Pfad, um Nachnamen abzufragen.
     */
    static final String NACHNAME_PATH = "/nachname";

    /**
     * Muster für eine UUID. [\dA-Fa-f]{8}{8}-([\dA-Fa-f]{8}{4}-){3}[\dA-Fa-f]{8}{12} enthält eine "capturing group" und
     * ist nicht zulässig.
     */
    static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    /**
     * Pfad, um Nachnamen abzufragen.
     */
    private final AbteilungsleiterReadService service;

    private final UriHelper uriHelper;

    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-methods
    // https://localhost:8080/swagger-ui.html

    /**
     * Suche anhand der Kunde-ID als Pfad-Parameter.
     * @param id ID des zu suchenden Abteilungsleiter
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundener Kunde mit Atom-Links.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit der Abteilungsleiter-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Abteilungsleiter gefunden")
    @ApiResponse(responseCode = "404", description = "Abteilungsleiter nicht gefunden")
    AbteilungsleiterModel getById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("getById: id={}, Thread={}", id, Thread.currentThread().getName());

        // Geschaeftslogik bzw. Anwendungskern
        final var abteilungsleiter = service.findById(id);

        // HATEOAS
        final var model = new AbteilungsleiterModel(abteilungsleiter);
        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = STR."\{baseUri}/\{abteilungsleiter.getId()}";
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        model.add(selfLink, listLink, addLink, updateLink);

        log.debug("getById: {}", model);
        return model;
    }

    /**
     * Suche mit diversen Suchkriterien als Query-Parameter.
     * @param suchkriterien Query-Parameter als Map.
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Gefundene Abteilungsleiter als CollectionModel.
     */
    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mit den Abteilungsleitern")
    @ApiResponse(responseCode = "404", description = "Keine Abteilungsleiter gefunden")
    CollectionModel<AbteilungsleiterModel> get(
        @RequestParam @NonNull final MultiValueMap<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("get: suchkriterien={}", suchkriterien);

        final var baseUri = uriHelper.getBaseUri(request).toString();

        // Geschaeftslogik bzw. Anwendungskern
        final var models = service.find(suchkriterien)
            .stream()
            .map(abteilungsleiter -> {
                final var model = new AbteilungsleiterModel(abteilungsleiter);
                model.add(Link.of(STR."\{baseUri}/\{abteilungsleiter.getId()}"));
                return model;
            })
            .toList();

        log.debug("get: {}", models);
        return CollectionModel.of(models);
    }

    /**
     * Abfrage, welche Nachnamen es zu einem Präfix gibt.
     * @param prefix Nachnamen-Präfix als Pfadvariable.
     * @return Die passenden Nachnamen oder Statuscode 404, falls es keine gibt.
     */
    @GetMapping(path = NACHNAME_PATH + "/{prefix}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche Nachname mit Praefix", tags = "Suchen")
    String getNachnameByPrefix(@PathVariable final String prefix) {
        log.debug("getNachnameByPrefix: {}", prefix);
        final var nachnamen = service.findNachnameByPrefix(prefix);
        log.debug("getNachnameByPrefix: {}", nachnamen);
        return nachnamen.stream()
            .map(nachname -> STR."\"\{nachname}\"")
            .toList()
            .toString();
    }
}
