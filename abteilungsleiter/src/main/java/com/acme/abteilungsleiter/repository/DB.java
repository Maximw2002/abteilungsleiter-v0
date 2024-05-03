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

import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.entity.Mitarbeiter;
import com.acme.abteilungsleiter.entity.Abteilung;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Emulation der Datenbasis für persistente Flugzeuge.
 */
@SuppressWarnings({"UtilityClassCanBeEnum", "UtilityClass", "MagicNumber", "RedundantSuppression",
    "java:S1192", "unused"})
final class DB {
    /**
     * Liste der Flugzeuge zur Emulation der DB.
     */
    @SuppressWarnings("StaticCollection")
    static final List<com.acme.abteilungsleiter.entity.Abteilungsleiter> ABTEILUNGSLEITER;

    static {
        // Helper-Methoden ab Java 9: List.of(), Set.of, Map.of, Stream.of
        // List.of() baut eine unveraenderliche Liste: kein Einfuegen, Aendern, Loeschen
        ABTEILUNGSLEITER = Stream.of(
                // admin
                Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000000"))
                    .nachname("Admin")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung1").standort("Karlsruhe").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("001").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("002").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("003").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build(),
                // HTTP GET
                Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                    .nachname("Steinmeyer")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung2").standort("Stuttgart").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("004").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("005").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("006").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build(),
                com.acme.abteilungsleiter.entity.Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                    .nachname("Spode")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung3").standort("Frankfurt").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("007").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("008").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("009").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build(),
                // HTTP PUT
                Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000003"))
                    .nachname("Sommer")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung4").standort("Darmstadt").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("010").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("011").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("012").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build(),
                // HTTP PATCH
                Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000004"))
                    .nachname("Perrone")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung5").standort("Heidelberg").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("013").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("014").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("015").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build(),
                // HTTP DELETE
                Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000005"))
                    .nachname("Weidler")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung6").standort("Mannheim").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("016").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("017").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("018").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build(),
                // zur freien Verfuegung
                Abteilungsleiter.builder()
                    .id(UUID.fromString("00000000-0000-0000-0000-000000000006"))
                    .nachname("Yazici")
                    .abteilung(Abteilung.builder().abteilungsname("abteilung7").standort("Freiburg").build())
                    .mitarbeiter(Stream.of(
                        Mitarbeiter.builder().id("019").nachname("Walter").arbeitsstunden(30).build(),
                        Mitarbeiter.builder().id("020").nachname("Werner").arbeitsstunden(20).build(),
                        Mitarbeiter.builder().id("021").nachname("Meyer").arbeitsstunden(25).build()
                    ).collect(Collectors.toList()))
                    .build()
            )
            // CAVEAT Stream.toList() erstellt eine "immutable" List
            .collect(Collectors.toList());
    }

    private DB() {
    }
}
