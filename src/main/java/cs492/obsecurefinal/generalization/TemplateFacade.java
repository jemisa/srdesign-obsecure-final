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

package cs492.obsecurefinal.generalization;

import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Benjamin Arnold
 */
public class TemplateFacade implements GeneralizationFacade {
    private static final TemplateFacade instance = new TemplateFacade();
    
    private TemplateFacade() {
	//singleton
    }
    
    public static TemplateFacade getInstance() {
	return instance;
    }

    @Override
    public Map<NamedEntity, GeneralizationResult> generalize(List<NamedEntity> sensitiveEntities) {
	Map<NamedEntity, GeneralizationResult> results = new TreeMap<>();
	for (NamedEntity entity : sensitiveEntities) {
	    GeneralizationResult result = new GeneralizationResult();
	    TemplateStrategy strategy = TemplateStrategyFactory.lookupStrategy(entity.getType());
	    List<String> hints = strategy.exec(entity.getText());
	    result.addAll(hints);
	}
	return results;
    }
}
