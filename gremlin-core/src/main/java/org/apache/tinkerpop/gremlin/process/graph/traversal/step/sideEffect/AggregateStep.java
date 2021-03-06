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
package org.apache.tinkerpop.gremlin.process.graph.traversal.step.sideEffect;

import org.apache.tinkerpop.gremlin.process.Traversal;
import org.apache.tinkerpop.gremlin.process.computer.MapReduce;
import org.apache.tinkerpop.gremlin.process.graph.traversal.step.SideEffectCapable;
import org.apache.tinkerpop.gremlin.process.graph.traversal.step.sideEffect.mapreduce.AggregateMapReduce;
import org.apache.tinkerpop.gremlin.process.graph.traversal.step.util.CollectingBarrierStep;
import org.apache.tinkerpop.gremlin.process.traversal.lambda.IdentityTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.step.MapReducer;
import org.apache.tinkerpop.gremlin.process.traversal.step.Reversible;
import org.apache.tinkerpop.gremlin.process.traversal.step.SideEffectRegistrar;
import org.apache.tinkerpop.gremlin.process.traversal.step.TraversalParent;
import org.apache.tinkerpop.gremlin.process.traversal.util.TraversalHelper;
import org.apache.tinkerpop.gremlin.process.traversal.util.TraversalUtil;
import org.apache.tinkerpop.gremlin.process.traverser.TraverserRequirement;
import org.apache.tinkerpop.gremlin.process.util.TraverserSet;
import org.apache.tinkerpop.gremlin.util.function.BulkSetSupplier;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class AggregateStep<S> extends CollectingBarrierStep<S> implements SideEffectRegistrar, SideEffectCapable, Reversible, TraversalParent, MapReducer<MapReduce.NullObject, Object, MapReduce.NullObject, Object, Collection> {

    private Traversal.Admin<S, Object> aggregateTraversal = new IdentityTraversal<>();
    private String sideEffectKey;

    public AggregateStep(final Traversal.Admin traversal, final String sideEffectKey) {
        super(traversal);
        this.sideEffectKey = sideEffectKey;
    }

    @Override
    public void registerSideEffects() {
        if (null == this.sideEffectKey) this.sideEffectKey = this.getId();
        this.getTraversal().asAdmin().getSideEffects().registerSupplierIfAbsent(this.sideEffectKey, BulkSetSupplier.instance());
    }

    @Override
    public String getSideEffectKey() {
        return this.sideEffectKey;
    }

    @Override
    public String toString() {
        return TraversalHelper.makeStepString(this, this.sideEffectKey, this.aggregateTraversal);
    }

    @Override
    public MapReduce<MapReduce.NullObject, Object, MapReduce.NullObject, Object, Collection> getMapReduce() {
        return new AggregateMapReduce(this);
    }

    @Override
    public void addLocalChild(final Traversal.Admin<?, ?> traversal) {
        this.aggregateTraversal = this.integrateChild(traversal, TYPICAL_LOCAL_OPERATIONS);
    }

    @Override
    public List<Traversal.Admin<S, Object>> getLocalChildren() {
        return Collections.singletonList(this.aggregateTraversal);
    }

    @Override
    public void barrierConsumer(final TraverserSet<S> traverserSet) {
        traverserSet.forEach(traverser ->
                TraversalHelper.addToCollection(
                        traverser.getSideEffects().get(this.sideEffectKey),
                        TraversalUtil.apply(traverser, this.aggregateTraversal),
                        traverser.bulk()));
    }

    @Override
    public Set<TraverserRequirement> getRequirements() {
        return this.getSelfAndChildRequirements(TraverserRequirement.BULK, TraverserRequirement.SIDE_EFFECTS);
    }

    @Override
    public AggregateStep<S> clone() throws CloneNotSupportedException {
        final AggregateStep<S> clone = (AggregateStep<S>) super.clone();
        clone.aggregateTraversal = this.integrateChild(this.aggregateTraversal.clone(), TYPICAL_LOCAL_OPERATIONS);
        return clone;
    }
}
