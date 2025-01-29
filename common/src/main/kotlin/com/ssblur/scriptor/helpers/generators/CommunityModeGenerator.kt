package com.ssblur.scriptor.helpers.generators

import com.google.gson.JsonObject
import java.util.*

class CommunityModeGenerator(obj: JsonObject?) : TokenGenerator() {
    override fun generateToken(key: String, parameters: JsonObject?): String {
        val random = Random((key.hashCode() + 0x055b10b0).toLong())
        val builder = StringBuilder()
        val consonantFirst = random.nextBoolean()
        for (i in 0 until 3 + random.nextInt(4)) {
            if (consonantFirst xor (i % 2 == 1)) builder.append(vowelGroup[random.nextInt(vowelGroup.size)])
            else builder.append(consonantGroup[random.nextInt(consonantGroup.size)])
        }

        return builder.toString()
    }

    override fun canBeDefault(): Boolean {
        return true
    }

    companion object {
        protected var consonantGroup: Array<String> = arrayOf(
            "b",
            "c",
            "d",
            "e",
            "f",
            "g",
            "h",
            "j",
            "k",
            "l",
            "m",
            "n",
            "p",
            "r",
            "s",
            "t",
            "v",
            "w",
            "x",
            "z",
            "st",
            "th",
            "ld",
            "qu",
            "pr",
            "lk",
            "mm",
            "pp",
            "ck",
            "rd",
            "pt"
        )
        protected var vowelGroup: Array<String> = arrayOf(
            "a",
            "e",
            "i",
            "o",
            "u",
            "ou",
            "ao",
            "io",
            "ee"
        )
    }
}
