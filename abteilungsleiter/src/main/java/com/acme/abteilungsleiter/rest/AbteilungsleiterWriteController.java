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
import com.acme.abteilungsleiter.service.ConstraintViolationsException;
import com.acme.abteilungsleiter.service.AbteilungsleiterWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.acme.abteilungsleiter.rest.AbteilungsleiterGetController.ID_PATTERN;
import static com.acme.abteilungsleiter.rest.AbteilungsleiterGetController.REST_PATH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Eine Controller-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die Methoden
 * der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/AbteilungsleiterWriteController.svg" alt="Klassendiagramm">
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * <a href="mailto:wema1123@h-ka.de">Maxim Weidler</a>
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
class AbteilungsleiterWriteController {
    private static final String PROBLEM_PATH = "/problem/";
    private final AbteilungsleiterWriteService service;
    private final AbteilungsleiterMapper mapper;
    private final UriHelper uriHelper;

    /**
     * Einen neuen Abteilungsleiter-Datensatz anlegen.
     * @param abteilungsleiterDTO Das Abteilungsleiterobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422, falls Constraints
     * verletzt sind oder Statuscode 400, falls syntaktische Fehler im Request-Body vorliegen.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Abteilungsleiter anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Abteilungsleiter neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte vorhanden")
    ResponseEntity<Void> post(@RequestBody final AbteilungsleiterDTO abteilungsleiterDTO,
                              final HttpServletRequest request) {
        log.debug("post: {}", abteilungsleiterDTO);

        final var abteilungsleiterInput = mapper.toAbteilungsleiter(abteilungsleiterDTO);
        final var abteilungsleiter = service.create(abteilungsleiterInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(STR."\{baseUri}/\{abteilungsleiter.getId()}");
        return created(location).build();
    }

    /**
     * Einen vorhandenen Abteilungsleiter-Datensatz überschreiben.
     * @param id ID des zu aktualisierenden Abteilungsleiters.
     * @param abteilungsleiterDTO Das Abteilungsleiterobjekt aus dem eingegangenen Request-Body.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Einen Abteilungsleiter mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Abteilungsleiter nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte vorhanden")
    void put(@PathVariable final UUID id, @RequestBody final AbteilungsleiterDTO abteilungsleiterDTO) {
        log.debug("put: id={}, {}", id, abteilungsleiterDTO);
        final var abteilungsleiterInput = mapper.toAbteilungsleiter(abteilungsleiterDTO);
        service.update(abteilungsleiterInput, id);
    }

    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final ConstraintViolationsException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var abteilungsleiterViolations = ex.getViolations()
            .stream()
            .map(violation -> STR."\{violation.getPropertyPath()}: " +
                STR."\{violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName()} " +
                violation.getMessage())
            .toList();
        log.trace("onConstraintViolations: {}", abteilungsleiterViolations);
        final String detail;
        if(abteilungsleiterViolations.isEmpty()) {
            detail = "N/A";
        } else {
            // [ und ] aus dem String der Liste entfernen
            final var violationsStr = abteilungsleiterViolations.toString();
            detail = violationsStr.substring(1, violationsStr.length() - 2);
        }

        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}\{ProblemType.CONSTRAINTS.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        log.debug("onMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(STR."\{PROBLEM_PATH}\{ProblemType.BAD_REQUEST.getValue()}"));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}
