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

import cs492.obsecurefinal.metaintelligence.bean.MetaRuleSet;

/**
 *<ruleSet name="opencyc">
    <category name="LOCATION">
        <rule name="namedLocation" type="OPENCYC" purpose="GENERALIZATION">
            <filter type="adaptive" value="3">LocationFilter</filter>
            <metric>
		<weight value="1">City</weight>
		<weight value="2">State-UnitedStates</weight>
            </metric>
            <criteria>
                <condition type="isa">ANY</condition>
            </criteria>
        </rule>
    </category>
  </ruleSet>
 * @author Benjamin Arnold
 */

public class XmlDocument {
    private final String contents;
    
    public XmlDocument(String contents) {
	this.contents = contents;
    }
    
    public RuleTreeComponent createParseTree() {
	XmlTag start = XmlTag.RULE_SET;
	boolean hasOverride = checkForOverride(contents, start);
	
	if (hasOverride) {
	    start = XmlTag.OVERRIDE;
	}
	
	RuleTreeComponent root = start.parse(contents);
	return root;
    }

    public MetaRuleSet createRuleSet() {
	RuleTreeComponent root = createParseTree();
	Visitor ruleSetVisitor = new MetaCreateVisitor();
	MetaRuleSet metaRuleSet = (MetaRuleSet) root.accept(ruleSetVisitor);
	
	return metaRuleSet;
    }
    
    private boolean checkForOverride(String sample, XmlTag tag) {
	boolean isOverriden = false;
	int indexOverride = XmlTag.OVERRIDE.location(sample);  //not found => -1; otherwise index
	if (indexOverride > -1) {
	    int indexTag = tag.location(sample);
	    isOverriden = indexTag > indexOverride;
	}
	return isOverriden;
    }
    
}
