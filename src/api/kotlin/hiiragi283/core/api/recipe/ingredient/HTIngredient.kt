package hiiragi283.core.api.recipe.ingredient

import hiiragi283.core.api.monad.Either
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
 * @since 0.10.0
 * @see HTItemIngredient
 * @see HTFluidIngredient
 */
interface HTIngredient<TYPE : Any, RESOURCE : HTResourceType<TYPE>> :
    BiPredicate<RESOURCE, Int>,
    HTHasText {
    /**
     * 指定した[resource]と[amount]が条件を満たしているか判定します。
     * @return [testOnlyType]が`true`，かつ[amount]が[HTIngredient.amount]以上の場合は`true`
     */
    override fun test(resource: RESOURCE, amount: Int): Boolean {
        val bool1: Boolean = testOnlyType(resource)
        return when {
            isCatalyst -> bool1
            else -> bool1 && amount >= this.amount
        }
    }

    /**
     * 指定した[resource]が条件を満たしているか判定します。
     */
    fun testOnlyType(resource: RESOURCE): Boolean

    /**
     * この材料が要求する量を取得します。
     */
    val amount: Int

    /**
     * この材料が触媒であるか判定します。
     */
    val isCatalyst: Boolean get() = amount <= 0

    /**
     * この材料に一致するすべての種類を返します。
     */
    fun unwrap(): Either<TagKey<TYPE>, List<RESOURCE>>

    override fun getText(): Component = unwrap().map(TagKey<TYPE>::getName) { resources: List<RESOURCE> ->
        ComponentUtils.formatList(resources, HTHasText::getText)
    }
}
