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
package com.acme.abteilungsleiter.service;

import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.repository.AbteilungsleiterRepository;
import jakarta.validation.Validator;

import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Abteilungsleiter auch mit Bean Validation.
 * <img src="../../../../../asciidoc/AbteilungsleiterWriteService.svg" alt="Klassendiagramm">
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * <a href="mailto:wema1123@h-ka.de">Maxim Weidler</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AbteilungsleiterWriteService {
    private final AbteilungsleiterRepository repo;

    // https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#validation-beanvalidation
    private final Validator validator;

    /**
     * Einen neuen Abteilungsleiter anlegen.
     * @param abteilungsleiter Das Objekt des neu anzulegenden Abteilungsleiters.
     * @return Der neu angelegte Abteilungsleiter mit generierter ID
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     */
    public Abteilungsleiter create(final Abteilungsleiter abteilungsleiter) {
        log.debug("create: {}", abteilungsleiter);

        final var violations = validator.validate(abteilungsleiter);
        if(!violations.isEmpty()) {
            log.debug("create: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }
        if(repo.isNachnameExisting(abteilungsleiter.getNachname())) {
            throw new NachnameExistsException(abteilungsleiter.getNachname());
        }

        final var abteilungsleiterDB = repo.create(abteilungsleiter);
        log.debug("create: {}", abteilungsleiterDB);
        return abteilungsleiterDB;
    }

    /**
     * Einen vorhandenen Abteilungsleiter aktualisieren.
     * @param abteilungsleiter Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Abteilungsleiters
     * @throws ConstraintViolationsException Falls mindestens ein Constraint verletzt ist.
     * @throws NotFoundException Kein Abteilungsleiter zur ID vorhanden.
     */
    public void update(final Abteilungsleiter abteilungsleiter, final UUID id) {
        log.debug("update: {}", abteilungsleiter);
        log.debug("update: id={}", id);

        final var violations = validator.validate(abteilungsleiter);
        if(!violations.isEmpty()) {
            log.debug("update: violations={}", violations);
            throw new ConstraintViolationsException(violations);
        }

        final var abteilungsleiterDbOptional = repo.findById(id);
        if(abteilungsleiterDbOptional.isEmpty()) {
            throw new NotFoundException(id);
        }

        final var nachname = abteilungsleiter.getNachname();
        final var abteilungsleiterDb = abteilungsleiterDbOptional.get();
        // Ist der neue Nachname bei einem *ANDEREN* Abteilungsleiter vorhanden?
        if(!Objects.equals(nachname, abteilungsleiterDb.getNachname()) && repo.isNachnameExisting(nachname)) {
            log.debug("update: nachname {} existiert", nachname);
            throw new com.acme.abteilungsleiter.service.NachnameExistsException(nachname);
        }
        abteilungsleiter.setId(id);
        repo.update(abteilungsleiter);
    }
}
