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
package org.apache.tinkerpop.gremlin.structure.strategy;

import org.apache.tinkerpop.gremlin.AbstractGremlinTest;
import org.apache.tinkerpop.gremlin.FeatureRequirement;
import org.apache.tinkerpop.gremlin.FeatureRequirementSet;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.apache.tinkerpop.gremlin.util.function.ThrowingConsumer;
import org.junit.Test;

import static org.apache.tinkerpop.gremlin.structure.Graph.Features.EdgeFeatures.FEATURE_ADD_EDGES;
import static org.apache.tinkerpop.gremlin.structure.Graph.Features.VariableFeatures.FEATURE_VARIABLES;
import static org.apache.tinkerpop.gremlin.structure.Graph.Features.VertexFeatures.FEATURE_ADD_VERTICES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public class ReadOnlyStrategyTest extends AbstractGremlinTest {
    public ReadOnlyStrategyTest() {
    }

    @Test
    @FeatureRequirement(featureClass = Graph.Features.VertexFeatures.class, feature = FEATURE_ADD_VERTICES)
    public void shouldNotAllowAddVertex() {
        assertException(g -> g.addVertex());
    }

    @Test
    @FeatureRequirement(featureClass = Graph.Features.VertexFeatures.class, feature = FEATURE_ADD_VERTICES)
    public void shouldNotAllowRemoveVertex() {
        g.addVertex();
        assertException(g -> g.V().next().remove());
    }

    @Test
    @FeatureRequirement(featureClass = Graph.Features.VertexFeatures.class, feature = FEATURE_ADD_VERTICES)
    @FeatureRequirement(featureClass = Graph.Features.EdgeFeatures.class, feature = FEATURE_ADD_EDGES)
    public void shouldNotAllowRemoveEdge() {
        final Vertex v = g.addVertex();
        v.addEdge("friend", v);
        assertException(g -> {
            final Edge e = g.E().next();
            e.remove();
        });
    }

    @Test
    @FeatureRequirement(featureClass = Graph.Features.VertexFeatures.class, feature = FEATURE_ADD_VERTICES)
    @FeatureRequirement(featureClass = Graph.Features.EdgeFeatures.class, feature = FEATURE_ADD_EDGES)
    public void shouldNotAllowAddEdge() {
        g.addVertex();
        assertException(g -> {
            final Vertex v = g.V().next();
            v.addEdge("friend", v);
        });
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.VERTICES_ONLY)
    public void shouldNotAllowVertexSetProperties() {
        g.addVertex();
        assertException(g -> g.V().next().<String>property("test", "test"));
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.VERTICES_ONLY)
    public void shouldNotAllowVertexSetProperty() {
        g.addVertex();
        assertException(g -> g.V().next().<String>property("test", "test"));
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.VERTICES_ONLY)
    @FeatureRequirement(featureClass = Graph.Features.VertexFeatures.class, feature = Graph.Features.VertexFeatures.FEATURE_META_PROPERTIES)
    public void shouldNotAllowVertexPropertySetProperty() {
        g.addVertex();
        assertException(g -> {
            final VertexProperty p = g.V().next().<String>property("test", "test");
            p.property("property", "on-a-property");
        });
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.SIMPLE)
    public void shouldNotAllowEdgeSetProperties() {
        final Vertex v = g.addVertex();
        v.addEdge("friend", v);
        assertException(g -> g.E().next().<String>property("test", "test"));
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.SIMPLE)
    public void shouldNotAllowEdgeSetProperty() {
        final Vertex v = g.addVertex();
        v.addEdge("friend", v);
        assertException(g -> g.E().next().<String>property("test", "test"));
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.SIMPLE)
    public void shouldNotAllowVertexPropertyRemoval() {
        final Vertex v = g.addVertex();
        v.property("test", "test");
        final Property<String> p = v.property("test");
        assertEquals("test", p.value());

        assertException(g -> {
            final VertexProperty<Object> prop = g.V().next().iterators().propertyIterator("test").next();
            prop.remove();
        });
    }

    @Test
    @FeatureRequirementSet(FeatureRequirementSet.Package.SIMPLE)
    public void shouldNotAllowEdgePropertyRemoval() {
        final Vertex v = g.addVertex();
        final Edge e = v.addEdge("friend", v);
        e.property("test", "test");
        final Property<String> p = e.property("test");
        assertEquals("test", p.value());

        assertException(g -> {
            final Property<String> prop = g.E().next().property("test");
            prop.remove();
        });
    }

    @Test
    @FeatureRequirement(featureClass = Graph.Features.VariableFeatures.class, feature = FEATURE_VARIABLES)
    public void shouldNotAllowVariableModifications() {
        assertException(g -> {
            g.variables().set("will", "not work");
        });
    }

    @Test(expected = UnsupportedOperationException.class)
    @FeatureRequirement(featureClass = Graph.Features.VariableFeatures.class, feature = FEATURE_VARIABLES)
    public void shouldNotAllowVariableAsMapModifications() {
        g.variables().set("will", "be read-only");
        final StrategyGraph swg = g.strategy(ReadOnlyStrategy.instance());
        swg.variables().asMap().put("will", "not work");
    }

    private void assertException(final ThrowingConsumer<Graph> stt) {
        try {
            final StrategyGraph swg = g.strategy(ReadOnlyStrategy.instance());
            stt.accept(swg);
            fail();
        } catch (Exception ex) {
            final Exception expectedException = ReadOnlyStrategy.Exceptions.graphUsesReadOnlyStrategy();
            assertEquals(expectedException.getClass(), ex.getClass());
            assertEquals(expectedException.getMessage(), ex.getMessage());
        }
    }
}
