package com.ssblur.scriptor.advancement

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import com.ssblur.scriptor.ScriptorMod.LOGGER
import net.minecraft.advancements.critereon.ContextAwarePredicate
import net.minecraft.advancements.critereon.SimpleCriterionTrigger
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import java.util.*


class GenericScriptorTrigger(var location: ResourceLocation) :
    SimpleCriterionTrigger<GenericScriptorTrigger.Instance>() {
    fun trigger(player: ServerPlayer?) {
        try {
            this.trigger(player!!) { instance: Instance? -> true }
        } catch (e: NullPointerException) {
            LOGGER.error("An error occurred while trying to award an advancement:")
            LOGGER.error(e)
        }
    }

    override fun codec(): Codec<Instance> {
        return Instance.CODEC
    }

    @Suppress("unused")
    class Instance : SimpleInstance {
        var player: ContextAwarePredicate?

        constructor(predicate: ContextAwarePredicate?) {
            this.player = predicate
        }

        constructor(predicate: Optional<ContextAwarePredicate?>) {
            this.player = predicate.orElse(null)
        }

        constructor() {
            this.player = null
        }

        override fun player(): Optional<ContextAwarePredicate> =  Optional.ofNullable(player)

        companion object {
            var CODEC: Codec<Instance> = RecordCodecBuilder.create { instance: RecordCodecBuilder.Instance<Instance> ->
                instance
                    .group(ContextAwarePredicate.CODEC.fieldOf("player").forGetter { i: Instance -> i.player })
                    .apply(instance) { predicate: ContextAwarePredicate? -> Instance(predicate) }
            }
        }
    }
}