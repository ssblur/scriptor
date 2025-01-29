package com.ssblur.scriptor.helpers.generators

import com.google.gson.JsonObject

class DebugGenerator(obj: JsonObject?) : TokenGenerator() {
    override fun canBeDefault(): Boolean {
        return true
    }

    override fun generateToken(key: String, parameters: JsonObject?): String {
        return key
    }
}
