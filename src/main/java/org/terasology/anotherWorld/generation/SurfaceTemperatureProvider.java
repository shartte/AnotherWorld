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

import org.terasology.math.Vector2i;
import org.terasology.math.Vector3i;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;

/**
 * @author Immortius
 */
@Produces(SurfaceTemperatureFacet.class)
@Requires({@Facet(SurfaceHeightFacet.class), @Facet(SeaLevelTemperatureFacet.class), @Facet(SeaLevelFacet.class)})
public class SurfaceTemperatureProvider implements FacetProvider {

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceTemperatureFacet facet = new SurfaceTemperatureFacet(region.getRegion(), region.getBorderForFacet(SurfaceTemperatureFacet.class));
        SeaLevelTemperatureFacet seaLevelTemperatureFacet = region.getRegionFacet(SeaLevelTemperatureFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = region.getRegionFacet(SeaLevelFacet.class);
        int seaLevel = seaLevelFacet.getSeaLevel();
        MaxLevelFacet maxLevelFacet = region.getRegionFacet(MaxLevelFacet.class);
        int maxLevel = maxLevelFacet.getMaxLevel();

        for (Vector2i position : facet.getWorldRegion()) {
            float height = surfaceHeightFacet.getWorld(position);
            float temperature = seaLevelTemperatureFacet.calculateTemperatureWorld(new Vector3i(position.x, height, position.y), seaLevel, maxLevel);
            facet.setWorld(position, temperature);
        }

        region.setRegionFacet(SurfaceTemperatureFacet.class, facet);
    }
}
