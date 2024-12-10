package com.ssblur.scriptor.error

import net.minecraft.resources.ResourceLocation

class InvalidGeneratorException(generator: String?, location: ResourceLocation?) : RuntimeException(
    String.format(
        "Generator '%s' does not exist or is not registered! (Used by %s)",
        generator,
        location
    )
)
