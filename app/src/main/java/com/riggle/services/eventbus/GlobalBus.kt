package com.riggle.services.eventbus

import org.greenrobot.eventbus.EventBus

/**
 * Created by anshulpatro on 09/04/18.
 */
object GlobalBus {
    private var sBus: EventBus? = null
    @JvmStatic
    val bus: EventBus?
        get() {
            if (sBus == null) sBus = EventBus.getDefault()
            return sBus
        }
}