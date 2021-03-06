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

import org.terasology.math.Region3i;
import org.terasology.math.Vector3i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.BaseFieldFacet2D;

public class SeaLevelHumidityFacet extends BaseFieldFacet2D {

    public SeaLevelHumidityFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }

    public float calculateHumidityWorld(Vector3i position, int seaLevel, int maxLevel) {
        float humidityBase = getWorld(position.x, position.z);
        if (position.y <= seaLevel) {
            return humidityBase;
        } else if (position.y >= maxLevel) {
            return 0;
        } else {
            // The higher above see level - the less humid
            return humidityBase * (1f * (maxLevel - position.y) / (maxLevel - seaLevel));
        }
    }
}
