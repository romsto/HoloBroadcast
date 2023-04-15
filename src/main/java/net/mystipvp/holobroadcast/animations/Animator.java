/*
 * Copyright (c) 2020-2023.
 * This project (HoloBroadcast) and this file is part of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy). It is under GPLv3 License.
 * Some contributors may have contributed to this file.
 *
 * HoloBroadcast cannot be copied and/or distributed without the express premission of Romain Storaï (_Rolyn) and Nathan Djian-Martin (DevKrazy)
 */

package net.mystipvp.holobroadcast.animations;

import java.util.ArrayList;
import java.util.List;

public class Animator {

    private final List<String> frames;
    private final int refreshRate;
    private int cursor = 1;
    private int refreshCounter;

    public Animator(ArrayList<String> frames, int refreshRate) {
        this.frames = frames;
        this.refreshRate = refreshRate;
        this.refreshCounter = refreshRate;
    }

    /**
     * @return the next frame of the animation
     */
    public String nextFrame() {
        if (refreshCounter >= refreshRate) {
            refreshCounter = 0;
            if (cursor >= frames.size())
                cursor = 0;
            cursor++;
        }
        refreshCounter++;
        return frames.get(cursor - 1);
    }
}
