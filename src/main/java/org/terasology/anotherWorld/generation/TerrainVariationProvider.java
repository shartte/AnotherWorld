/*
 * Copyright 2014 MovingBlocks
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
package org.terasology.anotherWorld.generation;

import org.terasology.anotherWorld.util.alpha.IdentityAlphaFunction;
import org.terasology.anotherWorld.util.alpha.UniformNoiseAlpha;
import org.terasology.math.Vector3i;
import org.terasology.utilities.procedural.Noise3D;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;

@Produces(TerrainVariationFacet.class)
public class TerrainVariationProvider implements FacetProvider {
    private UniformNoiseAlpha alpha = new UniformNoiseAlpha(IdentityAlphaFunction.singleton());
    private Noise3D noise;

    @Override
    public void setSeed(long seed) {
        noise = new SimplexNoise(seed + 2349873);
    }

    @Override
    public void process(GeneratingRegion region) {
        TerrainVariationFacet facet = new TerrainVariationFacet(region.getRegion(), region.getBorderForFacet(TerrainVariationFacet.class));

        for (Vector3i position : facet.getWorldRegion()) {
            float noiseValue = alpha.apply((float) (1 + noise.noise(position.x * 0.01f, position.y * 0.01f, position.z * 0.01f)) / 2f);
            facet.setWorld(position, noiseValue);
        }

        region.setRegionFacet(TerrainVariationFacet.class, facet);
    }
}
