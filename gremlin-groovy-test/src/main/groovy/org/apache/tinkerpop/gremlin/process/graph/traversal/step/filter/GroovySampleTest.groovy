/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tinkerpop.gremlin.process.graph.traversal.step.filter

import org.apache.tinkerpop.gremlin.process.ComputerTestHelper
import org.apache.tinkerpop.gremlin.process.Scope
import org.apache.tinkerpop.gremlin.process.T
import org.apache.tinkerpop.gremlin.process.Traversal
import org.apache.tinkerpop.gremlin.process.graph.traversal.__
import org.apache.tinkerpop.gremlin.process.traversal.engine.StandardTraversalEngine
import org.apache.tinkerpop.gremlin.structure.Edge
import org.apache.tinkerpop.gremlin.structure.Vertex

import static org.apache.tinkerpop.gremlin.process.graph.traversal.__.bothE
import static org.apache.tinkerpop.gremlin.process.graph.traversal.__.sample

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public abstract class GroovySampleTest {

    public static class StandardTest extends SampleTest {

        @Override
        public Traversal<Edge, Edge> get_g_E_sampleX1X() {
            g.E.sample(1)
        }

        @Override
        public Traversal<Edge, Edge> get_g_E_sampleX2X_byXweightX() {
            g.E.sample(2).by('weight')
        }

        @Override
        public Traversal<Vertex, Edge> get_g_V_localXoutE_sampleX1X_byXweightXX() {
            g.V.local(__.outE.sample(1).by('weight'))
        }

        @Override
        Traversal<Vertex, Map<String, Collection<Double>>> get_g_V_group_byXlabelX_byXbothE_valuesXweightX_foldX_byXsampleXlocal_2XX() {
            g.V().group().by(T.label).by(bothE().values('weight').fold()).by(sample(Scope.local, 2)).cap()
        }

        @Override
        Traversal<Vertex, Map<String, Collection<Double>>> get_g_V_group_byXlabelX_byXbothE_valuesXweightX_foldX_byXsampleXlocal_5XX() {
            g.V().group().by(T.label).by(bothE().values('weight').fold()).by(sample(Scope.local, 5)).cap()
        }
    }

    public static class ComputerTest extends SampleTest {

        @Override
        public Traversal<Edge, Edge> get_g_E_sampleX1X() {
            g.engine(StandardTraversalEngine.standard);
            g.E.sample(1) // TODO: makes no sense when its global
        }

        @Override
        public Traversal<Edge, Edge> get_g_E_sampleX2X_byXweightX() {
            g.engine(StandardTraversalEngine.standard);
            g.E.sample(2).by('weight') // TODO: makes no sense when its global
        }

        @Override
        public Traversal<Vertex, Edge> get_g_V_localXoutE_sampleX1X_byXweightXX() {
            ComputerTestHelper.compute("g.V.local(__.outE.sample(1).by('weight'))", g)
        }

        @Override
        Traversal<Vertex, Map<String, Collection<Double>>> get_g_V_group_byXlabelX_byXbothE_valuesXweightX_foldX_byXsampleXlocal_2XX() {
            ComputerTestHelper.compute("g.V().group().by(T.label).by(bothE().values('weight').fold()).by(sample(Scope.local, 2)).cap()", g)
        }

        @Override
        Traversal<Vertex, Map<String, Collection<Double>>> get_g_V_group_byXlabelX_byXbothE_valuesXweightX_foldX_byXsampleXlocal_5XX() {
            ComputerTestHelper.compute("g.V().group().by(T.label).by(bothE().values('weight').fold()).by(sample(Scope.local, 5)).cap()", g)
        }
    }
}
