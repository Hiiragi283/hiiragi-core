package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.registry.toHolderSet
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.TypedInstance
import net.minecraft.tags.TagKey

/**
 * 材料を作成するヘルパーのインターフェース
 * @param TYPE タイプのクラス
 * @param INGREDIENT 材料のクラス
 * @see mekanism.api.recipes.ingredients.creator.IIngredientCreator
 */
interface HTIngredientCreator<TYPE : Any, INGREDIENT : Any> {
    // Type
    fun from(type: TYPE): INGREDIENT

    fun from(types: Collection<TYPE>): INGREDIENT

    //    Registered    //

    interface Registered<TYPE : Any, INGREDIENT : Any> : HTIngredientCreator<TYPE, INGREDIENT> {
        // TagKey
        fun fromTagKey(tagKey: TagKey<TYPE>): INGREDIENT

        fun fromTagKeys(tagKeys: Collection<TagKey<TYPE>>): INGREDIENT

        // Holder
        fun fromHolder(holder: Holder<TYPE>): INGREDIENT = fromHolders(listOf(holder))

        fun fromHolder(instance: TypedInstance<TYPE>): INGREDIENT = fromHolder(instance.typeHolder())

        fun fromHolders(holders: Collection<Holder<TYPE>>): INGREDIENT = fromHolderSet(holders.toHolderSet())

        // HolderSet
        fun fromHolderSet(holderSet: HolderSet<TYPE>): INGREDIENT

        fun fromHolderSets(holderSets: Collection<HolderSet<TYPE>>): INGREDIENT
    }
}
