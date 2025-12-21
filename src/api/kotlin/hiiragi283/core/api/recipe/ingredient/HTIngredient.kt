package hiiragi283.core.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.tag.getName
import hiiragi283.core.api.text.HTHasText
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.tags.TagKey
import java.util.function.Predicate

/**
 * レシピの材料を表すインターフェースです。
 * @param TYPE [STACK]の種類のクラス
 * @param STACK 判定の対象となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see HTItemIngredient
 * @see HTFluidIngredient
 * @see mekanism.api.recipes.ingredients.InputIngredient
 */
interface HTIngredient<TYPE : Any, STACK : ImmutableStack<TYPE, STACK>> :
    Predicate<STACK>,
    HTHasText {
    /**
     * 指定した[stack]が条件を満たしているか判定します。
     * @return [testOnlyType]が`true`，かつ[ImmutableStack.amount]が[getRequiredAmount]以上の場合は`true`
     */
    override fun test(stack: STACK): Boolean = testOnlyType(stack) && stack.amount() >= getRequiredAmount()

    /**
     * 指定した[stack]が数量を除いて条件を満たしているか判定します。
     */
    fun testOnlyType(stack: STACK): Boolean

    /**
     * この材料が要求する量を返します。
     */
    fun getRequiredAmount(): Int

    /**
     * この材料に一致するすべての種類を返します。
     */
    fun unwrap(): Either<Pair<TagKey<TYPE>, Int>, List<STACK>>

    override fun getText(): Component = unwrap().map(
        { (tagKey: TagKey<TYPE>, _) -> tagKey.getName() },
        { stacks: List<STACK> -> ComponentUtils.formatList(stacks, HTHasText::getText) },
    )
}
