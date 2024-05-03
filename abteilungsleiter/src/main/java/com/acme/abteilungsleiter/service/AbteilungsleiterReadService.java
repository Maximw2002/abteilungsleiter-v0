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
package com.acme.abteilungsleiter.service;

import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.repository.AbteilungsleiterRepository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Anwendungslogik für Abteilungsleiter.
 * <img src="../../../../../asciidoc/AbteilungsleiterReadService.svg" alt="Klassendiagramm">
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * <a href="mailto:wema1123@h-ka.de">Maxim Weidler</a>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AbteilungsleiterReadService {
    private final AbteilungsleiterRepository repo;

    /**
     * Einen Abteilungsleiter anhand seiner ID suchen.
     * @param id Die Id des gesuchten Abteilungsleiters
     * @return Der gefundene Abteilungsleiter
     * @throws NotFoundException Falls kein Abteilungsleiter gefunden wurde
     */
    @SuppressWarnings("usage")
    public @NonNull Abteilungsleiter findById(final UUID id) {
        log.debug("findById: id={}", id);
        final var abteilungsleiter = repo.findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.debug("findById: {}", abteilungsleiter);
        return abteilungsleiter;
    }

    /**
     * Abteilungsleiter anhand von Suchkriterien als Collection suchen.
     * @param suchkriterien Die Suchkriterien
     * @return Die gefundenen Abteilungsleiter oder eine leere Liste
     * @throws NotFoundException Falls keine Flugzeuge gefunden wurden
     */
    @SuppressWarnings({"ReturnCount", "NestedIfDepth"})
    public @NonNull Collection<Abteilungsleiter> find(@NonNull final Map<String, List<String>> suchkriterien) {
        log.debug("find: suchkriterien={}", suchkriterien);

        if(suchkriterien.isEmpty()) {
            return repo.findAll();
        }

        if(suchkriterien.size() == 1) {

            final var nachnamen = suchkriterien.get("nachnamen");
            if(nachnamen != null && nachnamen.size() == 1) {
                final var abteilungsleiter = repo.findByNachname(nachnamen.getFirst());
                if(abteilungsleiter.isEmpty()) {
                    throw new NotFoundException(suchkriterien);
                }
                final var mehrereAbteilungsleiter = List.of(abteilungsleiter.get());
                log.debug("find (abteilungsleiter): {}", abteilungsleiter);
                return mehrereAbteilungsleiter;
            }
        }

        final var mehrereAbteilungsleiter = repo.find(suchkriterien);
        if(mehrereAbteilungsleiter.isEmpty()) {
            throw new NotFoundException(suchkriterien);
        }
        log.debug("find: {}", mehrereAbteilungsleiter);
        return mehrereAbteilungsleiter;
    }

    /**
     * Abfrage, welchen nachnamen es zu einem Präfix gibt.
     * @param prefix Nachname-Präfix.
     * @return Der passende Nachname.
     * @throws NotFoundException Falls kein Nachname gefunden wurde.
     */
    public Collection<String> findNachnameByPrefix(final String prefix) {
        final var nachnamen = repo.findNachnamenByPrefix(prefix);
        if(nachnamen.isEmpty()) {
            //noinspection NewExceptionWithoutArguments
            throw new NotFoundException();
        }
        return nachnamen;
    }
}
