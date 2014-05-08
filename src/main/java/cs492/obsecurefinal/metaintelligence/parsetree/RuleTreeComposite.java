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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benjamin Arnold
 */
public class RuleTreeComposite extends RuleTreeComponent {
    private List<RuleTreeComponent> children = new ArrayList<>();
    
    public RuleTreeComposite(XmlTag tag) {
	super(tag);
    }
    

    @Override
    public MetaNode accept(Visitor visitor) {
	return visitor.visit(this);
    }

    public void addChild(RuleTreeComponent child) {
	children.add(child);
    }
    
    public List<RuleTreeComponent> getChildren() {
	return children;
    }
    
}
