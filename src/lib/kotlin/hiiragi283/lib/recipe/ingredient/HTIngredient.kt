package hiiragi283.lib.recipe.ingredient

import java.util.function.Predicate
import net.minecraft.core.TypedInstance
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.util.context.ContextMap

interface HTIngredient<TYPE : Any, STACK> : Predicate<TypedInstance<TYPE>> where STACK : TypedInstance<TYPE>, STACK : DataComponentGetter {
    /**
     * 指定した[instance]が条件を満たしているか判定します。
     */
    override fun test(instance: TypedInstance<TYPE>): Boolean

    /**
     * 指定した[instance]が数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(instance: TypedInstance<TYPE>): Boolean

    fun getRequiredAmount(instance: TypedInstance<TYPE>): Int

    fun getPreviewStacks(contextMap: ContextMap): List<STACK>
}
