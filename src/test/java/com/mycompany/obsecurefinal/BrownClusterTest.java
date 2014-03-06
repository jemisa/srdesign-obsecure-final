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

package com.mycompany.obsecurefinal;

import cs492.obsecurefinal.cluster.BrownClusters;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 *
 * @author Benjamin Arnold
 */
public class BrownClusterTest {
    
    @Test
    public void simpleCluster() throws Exception {
	BrownClusters cluster = BrownClusters.getInstance();
	String word = "rathr";
	String expectedCluster = "rather";
	
	assertEquals(expectedCluster, cluster.cluster(word));
    }
    
    
}
