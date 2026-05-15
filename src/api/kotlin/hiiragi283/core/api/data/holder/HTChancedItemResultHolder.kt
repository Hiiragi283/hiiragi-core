package hiiragi283.core.api.data.holder

import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

class HTChancedItemResultHolder : HTIdLike {
    val results: List<HTChancedItemResult> get() = _results
    private val _results: MutableList<HTChancedItemResult> = mutableListOf()

    fun add(result: HTItemResult) {
        this.add(result.withChance())
    }

    fun add(result: HTChancedItemResult) {
        _results += result
    }

    operator fun plusAssign(result: HTItemResult) {
        this.add(result)
    }

    operator fun plusAssign(result: HTChancedItemResult) {
        this.add(result)
    }

    override fun getId(): ResourceLocation = results.first().getId()
}
