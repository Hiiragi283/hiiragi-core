package hiiragi283.core.common.data.recipe.builder

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.wrapOptional
import net.minecraft.resources.ResourceLocation
import java.util.Optional

abstract class HTChancedRecipeBuilder(prefix: String) : HTProcessingRecipeBuilder(prefix) {
    lateinit var result: HTItemResult
    val extraResult: ExtraResultHolder = ExtraResultHolder()

    inner class ExtraResultHolder {
        var result: HTItemResult? = null
            private set

        @JvmName("setResult")
        operator fun plusAssign(result: HTItemResult) {
            check(this.result == null) { "Extra Result has already beed initialized" }
            this.result = result
        }

        fun toOptional(): Optional<HTItemResult> = result.wrapOptional()
    }

    final override fun getPrimalId(): ResourceLocation = result.getId()
}
