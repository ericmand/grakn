/*
 * Grakn - A Distributed Semantic Database
 * Copyright (C) 2016-2018 Grakn Labs Limited
 *
 * Grakn is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Grakn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Grakn. If not, see <http://www.gnu.org/licenses/agpl.txt>.
 */

package ai.grakn.graql.internal.analytics;

import ai.grakn.util.CommonUtil;
import org.apache.tinkerpop.gremlin.process.computer.Memory;
import org.apache.tinkerpop.gremlin.process.computer.MessageScope;
import org.apache.tinkerpop.gremlin.process.computer.Messenger;
import org.apache.tinkerpop.gremlin.process.computer.VertexComputeKey;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Collections;
import java.util.Set;

/**
 * The vertex program for counting concepts.
 * <p>
 *
 * @author Jason Liu
 */

public class CountVertexProgram extends GraknVertexProgram<Long> {

    public static final String EDGE_COUNT = "countVertexProgram.edgeCount";

    // Needed internally for OLAP tasks
    public CountVertexProgram() {
    }

    @Override
    public Set<VertexComputeKey> getVertexComputeKeys() {
        return Collections.singleton(VertexComputeKey.of(EDGE_COUNT, false));
    }

    @Override
    public Set<MessageScope> getMessageScopes(final Memory memory) {
        return memory.isInitialIteration() ? messageScopeSetInAndOut : Collections.emptySet();
    }

    @Override
    public void safeExecute(final Vertex vertex, Messenger<Long> messenger, final Memory memory) {
        switch (memory.getIteration()) {
            case 0:
                messenger.sendMessage(messageScopeOut, 1L);
                break;
            case 1:
                if (messenger.receiveMessages().hasNext()) {
                    vertex.property(EDGE_COUNT, getMessageCount(messenger));
                }
                break;
            default:
                throw CommonUtil.unreachableStatement("Exceeded expected maximum number of iterations");
        }
    }

    @Override
    public boolean terminate(final Memory memory) {
        LOGGER.debug("Finished Count Iteration " + memory.getIteration());
        return memory.getIteration() == 1;
    }
}
