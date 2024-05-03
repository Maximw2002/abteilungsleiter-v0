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
package com.acme.abteilungsleiter.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
//import org.hibernate.validator.constraints.UniqueElements;
import java.util.UUID;

/**
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * <a href="mailto:wema1123@h-ka.de">Maxim Weidler</a>
 */
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@SuppressWarnings({"ClassFanOutComplexity", "JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup"})
public class Abteilungsleiter {
    /**
     * Muster für den Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Die ID des Abteilungsleiters.
     * @param id Die ID.
     * @return Die ID.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Nachname des Abteilungsleiters.
     * @param nachname Der Nachname.
     * @return Der Nachname.
     */
    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    private String nachname;

    /**
     * Die Abteilung des Abteilungsleiters.
     * @param abteilung Die Abteilung.
     * @return Die Abteilung.
     */
    @Valid
    @NotNull
    @ToString.Exclude
    private Abteilung abteilung;

    /**
     * die Mitarbeiter des Abteilungsleiters.
     * @param mitarbeiter Die Mitarbeiter.
     * @return Die Mitarbeiter.
     */
    @ToString.Exclude
    private List<Mitarbeiter> mitarbeiter;
}
