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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

/**
 * die Mitarbeiter einer Abteilung.
 */
@Builder
@Getter
@Setter
@ToString
@SuppressWarnings({"JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup"})
public class Mitarbeiter {
    /**
     * Muster für den Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";

    /**
     * Kleinster Wert für Arbeitsstunden.
     */
    public static final int MIN_ARBEITSSTUNDEN = 20;

    /**
     * Maximaler Wert für Arbeitsstunden.
     */
    public static final int MAX_ARBEITSSTUNDEN = 45;

    /**
     * Der Nachname des Abteilungsleiters.
     * @param nachname Der Nachname.
     * @return Der Nachname.
     */
    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    private String nachname;

    /**
     * Die ID des Mitarbeiters.
     */
    @EqualsAndHashCode.Include
    private String id;

    /**
     * Die anzahl an Arbeitsstunden des Mitarbeiters.
     */
    @Min(MIN_ARBEITSSTUNDEN)
    @Max(MIN_ARBEITSSTUNDEN)
    private int arbeitsstunden;
}
