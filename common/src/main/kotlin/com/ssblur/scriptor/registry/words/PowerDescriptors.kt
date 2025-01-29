package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.power.BloodPowerDescriptor
import com.ssblur.scriptor.word.descriptor.power.OverwhelmingStrengthDescriptor
import com.ssblur.scriptor.word.descriptor.power.SimpleStrengthDescriptor
import com.ssblur.scriptor.word.descriptor.power.SolarPowerDescriptor

@Suppress("unused")
object PowerDescriptors {
    val BLOOD_POWER = register("blood_power",
        BloodPowerDescriptor()
    )
    val STRONG = register("strong",
        SimpleStrengthDescriptor(2, 1.0)
    )
    val POWERFUL = register("powerful",
        SimpleStrengthDescriptor(6, 4.0)
    )
    val STACKING_STRONG =
        register("stacking_strong", SimpleStrengthDescriptor(
            4,
            1.0
        ).allowDuplication())
    val MASSIVE_STRONG = register("massive_strong",
        SimpleStrengthDescriptor(120, 30.0)
    )
    val OVERWHELMING_STRENGTH = register("overwhelming",
        OverwhelmingStrengthDescriptor()
    )
    val SOLAR_POWER = register("solar_power",
        SolarPowerDescriptor()
    )
}
