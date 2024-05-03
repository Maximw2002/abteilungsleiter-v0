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

import lombok.Getter;

/**
 * Exception, falls die Modellnummer bereits existiert.
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 * @author <a href="mailto:peju1017@h-ka.de">Justin Perrone</a>
 */
@Getter
public class NachnameExistsException extends RuntimeException {
    /**
     * Bereits vorhandene id.
     */
    private final String nachname;

    NachnameExistsException(@SuppressWarnings("ParameterHidesMemberVariable") final String nachname) {
        super(STR."Der Nachname \{nachname} existiert bereits");
        this.nachname = nachname;
    }
}
