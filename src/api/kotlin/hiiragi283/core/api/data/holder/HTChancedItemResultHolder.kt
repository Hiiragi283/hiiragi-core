package hiiragi283.core.api.data.holder

import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.resource.HTIdLike
import net.minecraft.resources.ResourceLocation

/**
 * [HTChancedItemResult]の[List]を作成するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.17.0
 */
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
