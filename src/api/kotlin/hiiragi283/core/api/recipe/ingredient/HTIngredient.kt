package hiiragi283.core.api.recipe.ingredient

import com.mojang.datafixers.util.Either
import hiiragi283.core.api.stack.ImmutableStack
import hiiragi283.core.api.storage.resource.HTResourceType
import hiiragi283.core.api.tag.getName
import hiiragi283.core.api.text.HTHasText
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
import net.minecraft.tags.TagKey
import java.util.function.BiPredicate

/**
 * レシピの材料を表すインターフェースです。
 * @param TYPE [RESOURCE]の種類のクラス
 * @param RESOURCE 判定の対象となるクラス
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see HTItemIngredient
 * @see HTFluidIngredient
 * @see mekanism.api.recipes.ingredients.InputIngredient
 */
interface HTIngredient<TYPE : Any, RESOURCE : HTResourceType<TYPE>> :
    BiPredicate<RESOURCE, Int>,
    HTHasText {
    /**
     * 指定した[resource]と[amount]が条件を満たしているか判定します。
     * @return [testOnlyType]が`true`，かつ[ImmutableStack.amount]が[getRequiredAmount]以上の場合は`true`
     */
    override fun test(resource: RESOURCE, amount: Int): Boolean = testOnlyType(resource) && amount >= getRequiredAmount()

    /**
     * 指定した[resource]が条件を満たしているか判定します。
     */
    fun testOnlyType(resource: RESOURCE): Boolean

    /**
     * この材料が要求する量を返します。
     */
    fun getRequiredAmount(): Int

    /**
     * この材料に一致するすべての種類を返します。
     */
    fun unwrap(): Either<TagKey<TYPE>, List<RESOURCE>>

    override fun getText(): Component = unwrap().map(TagKey<TYPE>::getName) { resources: List<RESOURCE> ->
        ComponentUtils.formatList(resources, HTHasText::getText)
    }
}
