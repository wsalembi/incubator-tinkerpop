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
package org.apache.tinkerpop.gremlin.process.graph.traversal.step.map;

import org.apache.tinkerpop.gremlin.process.Traversal;
import org.apache.tinkerpop.gremlin.process.Traverser;
import org.apache.tinkerpop.gremlin.process.graph.traversal.step.util.ReducingBarrierStep;
import org.apache.tinkerpop.gremlin.process.traversal.step.Reducing;
import org.apache.tinkerpop.gremlin.process.traverser.TraverserRequirement;
import org.apache.tinkerpop.gremlin.util.function.MeanNumberSupplier;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class MeanGlobalStep<S extends Number, E extends Number> extends ReducingBarrierStep<S, E> implements Reducing<E, Traverser<S>> {

    private static final Set<TraverserRequirement> REQUIREMENTS = EnumSet.of(TraverserRequirement.OBJECT, TraverserRequirement.BULK);

    public MeanGlobalStep(final Traversal.Admin traversal) {
        super(traversal);
        this.setSeedSupplier((Supplier) MeanNumberSupplier.instance());
        this.setBiFunction((BiFunction) MeanBiFunction.instance());
    }

    @Override
    public Set<TraverserRequirement> getRequirements() {
        return REQUIREMENTS;
    }

    @Override
    public Reducer<E, Traverser<S>> getReducer() {
        return new Reducer<>(this.getSeedSupplier(), this.getBiFunction(), true);
    }

    /////

    private static class MeanBiFunction<S extends Number> implements BiFunction<S, Traverser<S>, S>, Serializable {

        private static final MeanBiFunction INSTANCE = new MeanBiFunction();

        private MeanBiFunction() {

        }

        @Override
        public S apply(final S mutatingSeed, final Traverser<S> traverser) {
            return (S) ((MeanNumber) mutatingSeed).add(traverser.get(), traverser.bulk());
        }

        public final static <S extends Number> MeanBiFunction<S> instance() {
            return INSTANCE;
        }
    }

    ///

    public static final class MeanNumber extends Number implements Comparable<Number>, FinalGet<Double> {

        private long count = 0l;
        private double sum = 0.0d;

        public MeanNumber add(final Number amount, final long count) {
            this.count = this.count + count;
            this.sum = this.sum + (amount.doubleValue() * count);
            return this;
        }

        @Override
        public int intValue() {
            return (int) (this.sum / this.count);
        }

        @Override
        public long longValue() {
            return (long) (this.sum / this.count);
        }

        @Override
        public float floatValue() {
            return (float) (this.sum / this.count);
        }

        @Override
        public double doubleValue() {
            return this.sum / this.count;
        }

        @Override
        public String toString() {
            return Double.toString(this.doubleValue());
        }

        @Override
        public int compareTo(final Number number) {
            return Double.valueOf(this.doubleValue()).compareTo(number.doubleValue());
        }

        @Override
        public boolean equals(final Object object) {
            return object instanceof Number && Double.valueOf(this.doubleValue()).equals(((Number) object).doubleValue());
        }

        @Override
        public int hashCode() {
            return Double.valueOf(this.doubleValue()).hashCode();
        }

        @Override
        public Double getFinal() {
            return this.doubleValue();
        }
    }

}