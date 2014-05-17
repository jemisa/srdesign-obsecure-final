/*
 * Copyright (C) 2014 Benjamin Arnold
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

package cs492.obsecurefinal.metaintelligence.parsetree;

/**
 *
 * @author Benjamin Arnold
 */
interface XmlConstants {
    public static final String XML_TAG_OPEN = "<";
    public static final String XML_TAG_CLOSE = ">";
    public static final String TAG_OPEN_FORMAT = "<%s( \\S+=\"\\S+\")*>";
    public static final String TAG_END_FORMAT = "</%s>";
    public static final String XML_COMMENT_START = "<!";
    public static final String XML_DECLARATION_START = "<?";
    public static final String XML_TAG_END_START = "</";
    public static final String ARGUMENT_SEPARATOR = " ";
}
