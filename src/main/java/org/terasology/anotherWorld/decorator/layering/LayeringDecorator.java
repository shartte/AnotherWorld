/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.anotherWorld.decorator.layering;

import org.terasology.anotherWorld.AnotherWorldBiome;
import org.terasology.anotherWorld.ChunkDecorator;
import org.terasology.anotherWorld.generation.BiomeFacet;
import org.terasology.math.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.biomes.BiomeRegistry;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class LayeringDecorator implements ChunkDecorator {
    private Map<String, LayersDefinition> biomeLayers = new HashMap<>();
    private LayeringConfig layeringConfig;

    public LayeringDecorator(LayeringConfig layeringConfig) {
        this.layeringConfig = layeringConfig;
        loadLayers();
    }

    public void addBiomeLayers(LayersDefinition layersDefinition) {
        biomeLayers.put(layersDefinition.getBiomeId(), layersDefinition);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        BiomeFacet biomeFacet = chunkRegion.getFacet(BiomeFacet.class);
        BiomeRegistry biomeRegistry = CoreRegistry.get(BiomeRegistry.class);

        for (Vector3i position : chunk.getRegion()) {
            AnotherWorldBiome biome = biomeFacet.getWorld(position.x, position.z);
            LayersDefinition matchingLayers = findMatchingLayers(biomeRegistry, biome);
            if (matchingLayers != null) {
                /// Todo: what to do with the seed value here there is no chunk specific seed
                matchingLayers.generateInChunk(chunkRegion.hashCode(), chunk, chunkRegion, position.x, position.z, layeringConfig);
            }
        }
    }

    private LayersDefinition findMatchingLayers(BiomeRegistry biomeRegistry, AnotherWorldBiome biome) {
        LayersDefinition layersDefinition = biomeLayers.get(biome.getId());
        if (layersDefinition != null) {
            return layersDefinition;
        }
        String biomeParentId = biome.getBiomeParent();
        if (biomeParentId != null) {
            AnotherWorldBiome parentBiome = biomeRegistry.getBiomeById(biomeParentId, AnotherWorldBiome.class);
            if (parentBiome != null) {
                return findMatchingLayers(biomeRegistry, parentBiome);
            }
        }
        return null;
    }

    private void loadLayers() {
        WorldGeneratorPluginLibrary pluginLibrary = CoreRegistry.get(WorldGeneratorPluginLibrary.class);
        List<LayersDefinition> loadedLayersDefinitions = pluginLibrary.instantiateAllOfType(LayersDefinition.class);
        for (LayersDefinition layersDefinition : loadedLayersDefinitions) {
            addBiomeLayers(layersDefinition);
        }
    }
}
