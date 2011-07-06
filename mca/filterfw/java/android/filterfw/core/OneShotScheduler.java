/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package android.filterfw.core;

import android.filterfw.core.Filter;
import android.filterfw.core.Scheduler;
import android.filterfw.core.RoundRobinScheduler;

import java.util.HashMap;

// This OneShotScheduler only schedules filters at most once.
/**
 * @hide
 */
public class OneShotScheduler extends RoundRobinScheduler {
    private HashMap <String, Integer> scheduled = new HashMap<String, Integer>();

    public OneShotScheduler(FilterGraph graph) {
        super(graph);
    }

    @Override
    public void reset() {
        super.reset();
        scheduled.clear();
    }

    @Override
    public Filter scheduleNextNode() {
        Filter first = null;
        // return the first filter that is not scheduled before.
        while (true) {
            Filter filter = super.scheduleNextNode();
            if (filter == null) return null;
            if (!scheduled.containsKey(filter.getName())) {
                scheduled.put(filter.getName(),1);
                return filter;
            }
            // if loop back, nothing available
            if (first == filter) break;
            // save the first scheduled one
            if (first == null) first = filter;
        }
        return null;
    }
}