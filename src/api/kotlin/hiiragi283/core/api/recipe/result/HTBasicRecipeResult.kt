package hiiragi283.core.api.recipe.result

import hiiragi283.core.api.function.generateHash
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.text.HTCommonTranslation
import hiiragi283.core.api.text.HTTextResult
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch

abstract class HTBasicRecipeResult<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>>(
    val entry: HTHolderOrTagKey<TYPE>,
    val amount: Int,
    val components: DataComponentPatch,
) : HTRecipeResult<STACK>,
    HTIdLike by entry {
    final override fun getStackResult(provider: HolderLookup.Provider?): HTTextResult<STACK> = entry
        .getHolder(provider)
        .flatMap { holder: Holder<TYPE> ->
            when (val stack: STACK? = createStack(holder, amount, components)) {
                null -> HTTextResult.failure(HTCommonTranslation.EMPTY)
                else -> HTTextResult.success(stack)
            }
        }

    protected abstract fun createStack(holder: Holder<TYPE>, amount: Int, components: DataComponentPatch): STACK?

    override fun equals(other: Any?): Boolean {
        val result1: HTBasicRecipeResult<TYPE, STACK> = (other as? HTBasicRecipeResult<TYPE, STACK>) ?: return false
        val bool1: Boolean = this.entry == result1.entry
        val bool2: Boolean = this.amount == result1.amount
        val bool3: Boolean = this.components == result1.components
        return bool1 && bool2 && bool3
    }

    override fun hashCode(): Int = generateHash(entry, amount, components)
}
