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

import cs492.obsecurefinal.common.DataSourceNames;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author Benjamin Arnold
 */
public class ClusterLoadingStrategy implements ClusterLoadStrategy {

    @Override
    public void load(BrownClusters clusters) throws IOException, ClassNotFoundException {
	String filename = String.format("src/main/resources/%s", DataSourceNames.CLUSTERING_DATA_VETTED);
	ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
	PrimaryCluster cluster = (PrimaryCluster) in.readObject();
	clusters.setPrimaryCluster(cluster);
	in.close();
    }
}
