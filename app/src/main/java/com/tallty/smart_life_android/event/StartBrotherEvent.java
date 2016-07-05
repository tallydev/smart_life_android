package com.tallty.smart_life_android.event;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by kang on 16/7/5.
 *
 */
public class StartBrotherEvent {
    public SupportFragment targetFragment;

    public StartBrotherEvent(SupportFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
