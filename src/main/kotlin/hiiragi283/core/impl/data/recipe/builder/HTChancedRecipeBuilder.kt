package hiiragi283.core.impl.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStackTemplate
import java.util.Optional

abstract class HTChancedRecipeBuilder(prefix: String) : HTProcessingRecipeBuilder(prefix) {
    lateinit var result: ItemStackTemplate
    val extraResult: ExtraResultHolder = ExtraResultHolder()

    inner class ExtraResultHolder {
        var result: HTItemResult? = null
            private set

        @JvmName("setResult")
        operator fun plusAssign(template: ItemStackTemplate) {
            this.plusAssign(HTItemResult(template))
        }

        @JvmName("setResult")
        operator fun plusAssign(pair: Pair<ItemStackTemplate, Float>) {
            val (template: ItemStackTemplate, chance: Float) = pair
            this.plusAssign(HTItemResult(template, chance))
        }

        @JvmName("setResultWithChance")
        operator fun plusAssign(result: HTItemResult) {
            check(this.result == null) { "Extra Result has already beed initialized" }
            this.result = result
        }

        fun toOptional(): Optional<HTItemResult> = result.wrapOptional()
    }

    final override fun getPrimalId(): Identifier = result.typeHolder().toLike().getId()
}
