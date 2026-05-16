package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.ingredient.HTIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey

interface HTSizedIngredientCreator<TYPE : Any, INGREDIENT : HTIngredient<TYPE, *>> {
    fun getDefaultSize(): Int

    // from Type
    fun create(type: TYPE, size: Int = getDefaultSize()): INGREDIENT

    fun create(types: Collection<TYPE>, size: Int = getDefaultSize()): INGREDIENT

    // from TagKey
    fun tag(tagKey: TagKey<TYPE>, size: Int = getDefaultSize()): INGREDIENT

    fun tags(tagKeys: Collection<TagKey<TYPE>>, size: Int = getDefaultSize()): INGREDIENT

    // from Holder
    fun holder(holder: Holder<TYPE>, size: Int = getDefaultSize()): INGREDIENT = holderSet(HolderSet.direct(holder.delegate), size)

    fun holders(holders: Collection<Holder<TYPE>>, size: Int = getDefaultSize()): INGREDIENT = holderSet(HolderSet.direct(holders.map(Holder<TYPE>::getDelegate)), size)

    fun holderSet(holderSet: HolderSet<TYPE>, size: Int = getDefaultSize()): INGREDIENT

    fun holderSets(holderSets: Collection<HolderSet<TYPE>>, size: Int = getDefaultSize()): INGREDIENT
}
