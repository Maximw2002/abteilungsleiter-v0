/*
 * Copyright (C) 2023 - present Juergen Zimmermann, Hochschule Karlsruhe
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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.acme.abteilungsleiter.graphql;

import com.acme.abteilungsleiter.entity.Abteilung;
import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.entity.Mitarbeiter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen. Siehe
 * build\generated\sources\annotationProcessor\java\...\AbteilungsleiterInputMapperImpl.java.
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface AbteilungsleiterInputMapper {
    /**
     * Ein AbteilungsleiterInput-Objekt in ein Objekt für Abteilungsleiter konvertieren.
     * @param input AbteilungsleiterInput ohne ID
     * @return Konvertiertes Abteilungsleiter-Objekt
     */
    @Mapping(target = "id", ignore = true)
    Abteilungsleiter toAbteilungsleiter(AbteilungsleiterInput input);

    /**
     * Ein AbteilungInput-Objekt in ein Objekt für Abteilung konvertieren.
     * @param input AbteilungInput ohne Abteilungsleiter
     * @return Konvertiertes Abteilung-Objekt
     */
    Abteilung toAbteilung(AbteilungInput input);

    /**
     * Ein MitarbeiterInput-Objekt in ein Objekt für Mitarbeiter konvertieren.
     * @param input MitarbeiterInput-Objekt ohne Abteilungsleiter
     * @return Konvertiertes Mitarbeiter-Objekt
     */
    Mitarbeiter toMitarbeiter(MitarbeiterInput input);
}
