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
package com.acme.abteilungsleiter.rest;

import com.acme.abteilungsleiter.entity.Abteilungsleiter;
import com.acme.abteilungsleiter.entity.Abteilung;
import com.acme.abteilungsleiter.entity.Mitarbeiter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen. Siehe
 * build\generated\sources\annotationProcessor\java\main\...\AbteilungsleiterMapperImpl.java.
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface AbteilungsleiterMapper {
    /**
     * Ein DTO-Objekt von AbteilungsleiterDTO in ein Objekt für Abteilungsleiter konvertieren.
     * @param dto DTO-Objekt für AbteilungsleiterDTO ohne ID
     * @return Konvertiertes Abteilungsleiter-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    Abteilungsleiter toAbteilungsleiter(AbteilungsleiterDTO dto);

    /**
     * Ein DTO-Objekt von AbteilungDTO in ein Objekt für Abteilung konvertieren.
     * @param dto DTO-Objekt für AbteilungDTO ohne Abteilungsleiter
     * @return Konvertiertes Abteilung-Objekt
     */
    Abteilung toAbteilung(AbteilungDTO dto);

    /**
     * Ein DTO-Objekt von MitarbeiterDTO in ein Objekt für Mitarbeiter konvertieren.
     * @param dto DTO-Objekt für MitarbeiterDTO ohne Abteilungsleiter
     * @return Konvertiertes Mitarbeiter-Objekt
     */
    Mitarbeiter toMitarbeiter(MitarbeiterDTO dto);
}
