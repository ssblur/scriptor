package com.ssblur.scriptor.registry.words

import com.ssblur.scriptor.registry.words.WordRegistry.register
import com.ssblur.scriptor.word.descriptor.color.ColorDescriptor

@Suppress("unused")
object ColorDescriptors {
    val WHITE = register("white",
        ColorDescriptor(0xe4e4e4)
    )
    val LIGHT_GRAY = register("light_gray",
        ColorDescriptor(0xa0a7a7)
    )
    val DARK_GRAY = register("dark_gray",
        ColorDescriptor(0x414141)
    )
    val BLACK = register("black",
        ColorDescriptor(0x181414)
    )
    val RED = register("red", ColorDescriptor(0x9e2b27))
    val ORANGE = register("orange",
        ColorDescriptor(0xea7e35)
    )
    val YELLOW = register("yellow",
        ColorDescriptor(0xc2b51c)
    )
    val LIME_GREEN = register("lime_green",
        ColorDescriptor(0x39ba2e)
    )
    val GREEN = register("green",
        ColorDescriptor(0x364b18)
    )
    val LIGHT_BLUE = register("light_blue",
        ColorDescriptor(0x6387d2)
    )
    val CYAN = register("cyan", ColorDescriptor(0x267191))
    val BLUE = register("blue", ColorDescriptor(0x253193))
    val PURPLE = register("purple",
        ColorDescriptor(0x7e34bf)
    )
    val MAGENTA = register("magenta",
        ColorDescriptor(0xbe49c9)
    )
    val PINK = register("pink", ColorDescriptor(0xd98199))
    val BROWN = register("brown",
        ColorDescriptor(0x56331c)
    )

    val RAINBOW = register(
        "rainbow",
        ColorDescriptor(-1)
    )
}
