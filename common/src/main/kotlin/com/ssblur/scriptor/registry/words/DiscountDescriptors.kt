package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.discount.*

@Suppress("unused")
object DiscountDescriptors {
    val BLOOD_COST = register("blood_cost",
        BloodCostDescriptor()
    )
    val CHEAP = register("cheap", CheapDescriptor())
    val HEALTHY = register("healthy",
        HealthyDescriptor()
    )
    val POISONED = register("poisoned",
        PoisonDescriptor()
    )
    val WEAKENED = register("weakened",
        WeakDescriptor()
    )
    val ON_FIRE = register("on_fire",
        OnFireDescriptor()
    )
    val CRITICAL = register("critical",
        CriticalDescriptor()
    )
    val NIGHT = register("night",
        NightDiscountDescriptor()
    )
    val RAIN = register("rain",
        RainDiscountDescriptor()
    )
    val CLEAR_SKIES = register("clear_skies",
        ClearDiscountDescriptor()
    )
}
