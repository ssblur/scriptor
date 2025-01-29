package com.ssblur.scriptor.resources

import com.google.gson.JsonObject
import com.ssblur.scriptor.ScriptorMod
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.getBinding
import com.ssblur.scriptor.registry.TokenGeneratorRegistry.registerBinding
import com.ssblur.unfocused.data.DataLoaderRegistry.registerDataLoader
import net.minecraft.resources.ResourceLocation

object GeneratorBindings {
    data class GeneratorBinding(val word: String, val parameters: JsonObject?)
    data class GeneratorBindings(val generator: String, val bindings: List<GeneratorBinding>)

    init {
        ScriptorMod.registerDataLoader("scriptor/bindings", GeneratorBindings::class) { bindings, location ->
            bindings.bindings.forEach { binding ->
                if (getBinding(binding.word) == null || getBinding(binding.word)!!.namespace == "scriptor" || location.namespace != "scriptor")
                    registerBinding(binding.word, ResourceLocation.parse(bindings.generator), binding.parameters)
            }
        }
    }
}