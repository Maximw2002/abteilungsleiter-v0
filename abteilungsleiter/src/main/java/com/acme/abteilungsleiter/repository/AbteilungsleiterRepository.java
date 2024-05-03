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
package com.acme.abteilungsleiter.repository;

import java.util.*;

import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.acme.abteilungsleiter.repository.DB.ABTEILUNGSLEITER;
import static java.util.Collections.emptyList;

import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;

/**
 * Repository für den DB-Zugriff bei Flugzeuge
 */
@Repository
@Slf4j
@SuppressWarnings("PublicConstructor")
public class AbteilungsleiterRepository {
    /**
     * Ein Flugzeug anhand seiner ID suchen.
     * @param id Die Id des gesuchten Flugzeugs
     * @return Optional mit dem gefundenen Flugzeug oder leeres Optional
     */

    public Optional<Abteilungsleiter> findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var result = ABTEILUNGSLEITER.stream()
            .filter(abteilungsleiter -> Objects.equals(abteilungsleiter.getId(), id))
            .findFirst();
        log.debug("findById: {}", result);
        return result;
    }

    /**
     * Flugzeug anhand von Suchkriterien ermitteln. Z.B. mit GET
     * https://localhost:8080/api?modellnummer=A&amp;baujahr=1998
     * @param suchkriterien Suchkriterien.
     * @return Gefundene Flugzeuge oder leere Collection.
     */
    @SuppressWarnings({"ReturnCount", "JavadocLinkAsPlainText", "unused"})
    public @NonNull Collection<Abteilungsleiter>
    find(final Map<String, ? extends List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if(suchkriterien.isEmpty()) {
            return findAll();
        }

        // for-Schleife statt "forEach" wegen return
        for(final var entry: suchkriterien.entrySet()) {
            switch(entry.getKey()) {
                case "nachname" -> {
                    final var AbteilungsleiterOpt = findByNachname(entry.getValue().getFirst());
                    return AbteilungsleiterOpt.map(List::of).orElse(emptyList());
                }
                default -> {
                    log.debug("find: ungueltiges Suchkriterium={}", entry.getKey());
                    return emptyList();
                }
            }
        }

        return emptyList();
    }

    /**
     * Alle Flugzeuge als Collection ermitteln, wie sie später auch von der DB kommen.
     * @return Alle Flugzeuge
     */
    public @NonNull Collection<Abteilungsleiter> findAll() {
        return ABTEILUNGSLEITER;
    }

    /**
     * Flugzeug zu gegebener Modellnummer aus der DB ermitteln.
     * @param nachname Modellnummer für die Suche
     * @return Gefundenes Flugzeug oder leeres Optional
     */
    public Optional<Abteilungsleiter> findByNachname(final String nachname) {
        log.debug("findByNachname: {}", nachname);
        final var result = ABTEILUNGSLEITER.stream()
            .filter(abteilungsleiter -> Objects.equals(abteilungsleiter.getNachname(), nachname))
            .findFirst();
        log.debug("findByNachname: {}", result);
        return result;
    }

    /**
     * Abfrage, ob es ein Flugzeug mit gegebener Modellnummer gibt.
     * @param nachname Modellnummer für die Suche
     * @return true, falls es ein solches Flugzeug gibt, sonst false
     */
    @SuppressWarnings("unused")
    public boolean isNachnameExisting(final String nachname) {
        log.debug("isNachnameExisting: nachname={}", nachname);
        final var count = ABTEILUNGSLEITER.stream()
            .filter(abteilungsleiter -> Objects.equals(abteilungsleiter.getNachname(), nachname))
            .count();
        log.debug("isNachnameExisting: count={}", count);
        return count > 0L;
    }

    /**
     * Abfrage, welche Modellnummern es zu einem Präfix gibt.
     * @param prefix Modellnummern-Präfix.
     * @return Die passenden Modellnummern oder eine leere Collection.
     */
    @SuppressWarnings("unused")
    public @NonNull Collection<String> findNachnamenByPrefix(final @NonNull String prefix) {
        log.debug("findByNachname: prefix={}", prefix);
        final var mehrereAbteilungsleiter = ABTEILUNGSLEITER.stream()
            .map(com.acme.abteilungsleiter.entity.Abteilungsleiter::getNachname)
            .filter(nachname -> nachname.startsWith(prefix))
            .distinct()
            .toList();
        log.debug("findByNachname: Nachname={}", mehrereAbteilungsleiter);
        return mehrereAbteilungsleiter;
    }

    /**
     * Einen neuen Kunden anlegen.
     * @param abteilungsleiter Das Objekt des neu anzulegenden Kunden.
     * @return Der neu angelegte Kunde mit generierter ID
     */
    public @NonNull Abteilungsleiter create(final @NonNull Abteilungsleiter abteilungsleiter) {
        log.debug("create: {}", abteilungsleiter);
        abteilungsleiter.setId(randomUUID());
        ABTEILUNGSLEITER.add(abteilungsleiter);
        log.debug("create: {}", abteilungsleiter);
        return abteilungsleiter;
    }

    /**
     * Einen vorhandenen Kunden aktualisieren.
     * @param abteilungsleiter Das Objekt mit den neuen Daten
     */
    public void update(final @NonNull Abteilungsleiter abteilungsleiter) {
        log.debug("update: {}", abteilungsleiter);
        final OptionalInt index = IntStream
            .range(0, ABTEILUNGSLEITER.size())
            .filter(i -> Objects.equals(ABTEILUNGSLEITER.get(i).getId(), abteilungsleiter.getId()))
            .findFirst();
        log.trace("update: index={}", index);
        if(index.isEmpty()) {
            return;
        }
        ABTEILUNGSLEITER.set(index.getAsInt(), abteilungsleiter);
        log.debug("update: {}", abteilungsleiter);
    }
}
