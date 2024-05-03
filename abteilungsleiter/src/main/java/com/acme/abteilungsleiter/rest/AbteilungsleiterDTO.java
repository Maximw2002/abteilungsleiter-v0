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

import java.util.List;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Abteilungsleiters. Beim Lesen wird die Klasse
 * AbteilungsleiterModel für die Ausgabe verwendet.
 * @param nachname Gültiger Nachname eines Kunden, d.h. mit einem geeigneten Muster.
 * @param abteilung Die Abteilung eines Abteilungsleiters.
 * @param mitarbeiter Die Mitarbeiter eines Abteilungsleiters.
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@SuppressWarnings("RecordComponentNumber")
record AbteilungsleiterDTO(
    String nachname,
    AbteilungDTO abteilung,
    List<MitarbeiterDTO> mitarbeiter
) {
}
