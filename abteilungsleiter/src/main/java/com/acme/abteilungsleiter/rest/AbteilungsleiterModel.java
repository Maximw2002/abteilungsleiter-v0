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

import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.entity.Abteilung;
import com.acme.abteilungsleiter.entity.Mitarbeiter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 * <img src="../../../../../asciidoc/AbteilungsleiterModel.svg" alt="Klassendiagramm">
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@JsonPropertyOrder({
    "nachname", "abteilung", "mitarbeiter"
})
@Relation(collectionRelation = "mehrereAbteilungsleiter", itemRelation = "abteilungsleiter")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
class AbteilungsleiterModel extends RepresentationModel<AbteilungsleiterModel> {

    @EqualsAndHashCode.Include
    private final String nachname;
    private final Abteilung abteilung;
    private final List<Mitarbeiter> mitarbeiter;

    AbteilungsleiterModel(final Abteilungsleiter abteilungsleiter) {
        nachname = abteilungsleiter.getNachname();
        abteilung = abteilungsleiter.getAbteilung();
        mitarbeiter = abteilungsleiter.getMitarbeiter();
    }
}
