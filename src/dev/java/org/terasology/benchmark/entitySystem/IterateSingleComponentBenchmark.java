/*
 * Copyright 2013 MovingBlocks
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

package org.terasology.benchmark.entitySystem;

import com.google.common.collect.Lists;
import org.terasology.benchmark.AbstractBenchmark;
import org.terasology.entitySystem.Component;
import org.terasology.entitySystem.EntityRef;
import org.terasology.entitySystem.internal.PojoEntityManager;
import org.terasology.logic.inventory.InventoryComponent;
import org.terasology.logic.location.LocationComponent;
import org.terasology.rendering.logic.MeshComponent;
import org.terasology.utilities.procedural.FastRandom;
import org.terasology.world.block.BlockComponent;

import java.util.List;

/**
 *
 */
public class IterateSingleComponentBenchmark extends AbstractBenchmark {
    private List<List<Component>> rawEntityData;
    private PojoEntityManager entityManager;

    public IterateSingleComponentBenchmark() {
        super("Iterate Entities Single Component", 10000, new int[]{10000});
    }

    @Override
    public void setup() {
        FastRandom rand = new FastRandom(0L);
        rawEntityData = Lists.newArrayList();
        for (int i = 0; i < 1000; ++i) {
            List<Component> entityData = Lists.newArrayList();
            if (rand.randomFloat() < 0.75f) {
                entityData.add(new LocationComponent());
            }
            if (rand.randomFloat() < 0.5f) {
                entityData.add(new MeshComponent());
            }
            if (rand.randomFloat() < 0.5f) {
                entityData.add(new InventoryComponent());
            }
            if (rand.randomFloat() < 0.25f) {
                entityData.add(new BlockComponent());
            }
            rawEntityData.add(entityData);
        }

        entityManager = new PojoEntityManager();
        for (List<Component> rawEntity : rawEntityData) {
            entityManager.create(rawEntity);
        }
    }

    @Override
    public void run() {
        for (EntityRef entity : entityManager.getEntitiesWith(LocationComponent.class)) {
            LocationComponent loc = entity.getComponent(LocationComponent.class);
            loc.getLocalPosition();
        }
    }

    @Override
    public void finish(boolean aborted) {
        rawEntityData = null;
        entityManager = null;
    }
}