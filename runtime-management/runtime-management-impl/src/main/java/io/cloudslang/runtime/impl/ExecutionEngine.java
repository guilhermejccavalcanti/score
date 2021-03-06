/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package io.cloudslang.runtime.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Genadi Rabinovich, genadi@hpe.com on 05/05/2016.
 */
public abstract class ExecutionEngine {
    private static final String NO_DEPENDENCIES_KEY = "";

    protected String generatedDependenciesKey(Set<String> dependencies) {
        // optimistic - this is java (which has only one dependency) or python with on dependency
        if(dependencies.size() == 1) {
            return dependencies.iterator().next();
        }
        // optimistic - this is old content - no dependencies
        if(dependencies.isEmpty()) {
            return NO_DEPENDENCIES_KEY;
        }

        List<String> actionDependencies = new ArrayList<>();
        actionDependencies.addAll(dependencies);
        Collections.sort(actionDependencies);
        StringBuilder sb = new StringBuilder();
        for (String dependency : actionDependencies) {
            sb.append(dependency).append(";");
        }
        return sb.toString();
    }
}
