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
public abstract class RuleTreeComponent {
    private final XmlTag tag;
    private String name;
    private String type;
    private String purpose;
    private String value;
    
    public RuleTreeComponent(XmlTag tag) {
	this.tag = tag;
	this.name = tag.getIdentifier();
    }
    
    public XmlTag getXmlTag() {
	return tag;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getPurpose() {
	return purpose;
    }

    public void setPurpose(String purpose) {
	this.purpose = purpose;
    }
    
    public abstract MetaNode accept(Visitor visitor);

    void setAppropriate(String argName, String argValue) {
	
	switch(argName) {
	    case "name" : setName(argValue); break;
	    case "type" : setType(argValue); break;
	    case "purpose" : setPurpose(argValue); break;
	    case "value" : setValue(argValue); break;
	    default: //do nothing
	}
    }
}
