package com.ssblur.scriptor.error

import net.minecraft.resources.ResourceLocation

class MissingRequiredElementException : RuntimeException {
    constructor(
        element: String?,
        location: ResourceLocation?
    ) : super(String.format("Missing required element %s when attempting to load %s", element, location))

    constructor(element: String?, message: String?) : super(
        String.format(
            "Missing required element %s: %s",
            element,
            message
        )
    )
}
