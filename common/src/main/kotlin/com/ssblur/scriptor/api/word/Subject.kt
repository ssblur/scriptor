package com.ssblur.scriptor.api.word

import com.ssblur.scriptor.helpers.targetable.Targetable
import com.ssblur.scriptor.word.Spell
import java.util.concurrent.CompletableFuture

abstract class Subject : Word() {
    /**
     * Called to get targets for this Subject.
     * Actions will not be applied until resolved.
     * @param caster The Entity which cast this spell.
     * @param spell The Spell which is being cast.
     */
    abstract fun getTargets(caster: Targetable, spell: Spell): CompletableFuture<List<Targetable>>
}
