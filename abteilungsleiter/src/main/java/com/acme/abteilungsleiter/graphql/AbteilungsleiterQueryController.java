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
package com.acme.abteilungsleiter.graphql;

import com.acme.abteilungsleiter.service.AbteilungsleiterReadService;
import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.entity.Mitarbeiter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import static java.util.Collections.emptyMap;

/**
 * Eine Controller-Klasse für das Lesen mit der GraphQL-Schnittstelle und den Typen aus dem GraphQL-Schema.
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Controller
@RequiredArgsConstructor
@Slf4j
class AbteilungsleiterQueryController {
    private final AbteilungsleiterReadService service;

    /**
     * Suche anhand der Abteilungsleiter-ID.
     * @param id ID des zu suchenden Abteilungsleiters
     * @return Der gefundene Abteilungsleiter
     */
    @QueryMapping("abteilungsleiter")
    Abteilungsleiter findById(@Argument final UUID id) {
        log.debug("findById: id={}", id);
        final var abteilungsleiter = service.findById(id);
        log.debug("findById: abteilungsleiter={}", abteilungsleiter);
        return abteilungsleiter;
    }

    /**
     * Suche mit diversen Suchkriterien.
     * @param input Suchkriterien und ihre Werte, z.B. `nachname` und `Alpha`
     * @return Die gefundenen Abteilungsleiter als Collection
     */
    @QueryMapping("mehrereAbteilungsleiter")
    Collection<Abteilungsleiter> find(@Argument final Optional<Suchkriterien> input) {
        log.debug("find: suchkriterien={}", input);
        final var suchkriterien = input.map(Suchkriterien::toMap).orElse(emptyMap());
        final var mehrereAbteilungsleiter = service.find(suchkriterien);
        log.debug("find: mehrereAbteilungsleiter={}", mehrereAbteilungsleiter);
        return mehrereAbteilungsleiter;
    }

    /**
     * Handler-Methode für das GraphQL-Field "mitarbeiter" für den GraphQL-Type "Abteilungsleiter".
     * @param abteilungsleiter Injiziertes Objekt vom GraphQL-Type "Abteilungsleiter", zu dem diese Handler-Methode
     * aufgerufen wird.
     * @param first die ersten Mitarbeiterdaten in der Liste der Mitarbeiter zum gefundenen Abteilungsleiter
     * @return Liste mit Mitarbeiter-Objekten, die für das Field "mitarbeiter" beim gefundenen Abteilungsleiter-Objekt
     * verwendet werden.
     */
    @SchemaMapping
    List<Mitarbeiter> mehrereMitarbeiter(final Abteilungsleiter abteilungsleiter, @Argument final int first) {
        log.debug("mehrereMitarbeiter: abteilungsleiter={}, mehrereMitarbeiter={}, first={}", abteilungsleiter,
            abteilungsleiter.getMitarbeiter(), first);
        if(first <= 0) {
            return List.of();
        }

        final var anzahlMehrereMitarbeiter = abteilungsleiter.getMitarbeiter().size();
        final var end = first <= anzahlMehrereMitarbeiter ? first : anzahlMehrereMitarbeiter;
        return abteilungsleiter.getMitarbeiter().subList(0, end);
    }
}
