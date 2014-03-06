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

package cs492.obsecurefinal.cluster;

/**
 *
 * @author Benjamin Arnold
 */
public class BrownClusters {
    private static final BrownClusters instance = new BrownClusters();
    
    private BrownClusters() {
	//singleton
	init();
    }
    
    public static final BrownClusters getInstance() {
	return instance;
    }
    
    private void init() {
	BrownClusters.class.getResourceAsStream("clusterKey.csv");
	
    }
    
}
