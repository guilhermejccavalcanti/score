/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.eclipse.score.engine.partitions.services;

import org.eclipse.score.engine.partitions.entities.PartitionGroup;

/**
 * Date: 4/22/12
 *
 * @author
 */
//TODO: Add Javadoc
public interface PartitionService {
    void createPartitionGroup(String groupName, int groupSize, long timeThreshold, long sizeThreshold);
    void updatePartitionGroup(String groupName, int groupSize, long timeThreshold, long sizeThreshold);
	PartitionGroup readPartitionGroup(String groupName);
	boolean rollPartitions(String groupName);
}