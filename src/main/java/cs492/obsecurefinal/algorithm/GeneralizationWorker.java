/*
 * Copyright (C) 2014 Drexel University
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
package cs492.obsecurefinal.algorithm;

import cs492.obsecurefinal.common.GeneralizationResult;
import cs492.obsecurefinal.common.NamedEntity;
import cs492.obsecurefinal.generalization.GeneralizationManager;
import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;

/**
 *
 * @author JOEL
 */
public class GeneralizationWorker extends SwingWorker<Map<NamedEntity, GeneralizationResult>, Void>
{
    private List<NamedEntity> entityList;
    
    public GeneralizationWorker(List<NamedEntity> list)
    {
        entityList = list;
    }

    @Override
    protected Map<NamedEntity, GeneralizationResult> doInBackground() throws Exception
    {
        Map<NamedEntity, GeneralizationResult> generalizedResults = GeneralizationManager.generalize(entityList);
        
        return generalizedResults;
    }
}
