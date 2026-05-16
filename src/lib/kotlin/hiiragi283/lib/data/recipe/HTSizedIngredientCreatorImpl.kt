package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.ingredient.HTIngredient
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.tags.TagKey

abstract class HTSizedIngredientCreatorImpl<TYPE : Any, INGREDIENT : HTIngredient<TYPE, *>>(protected val getter: HolderGetter<TYPE>) : HTSizedIngredientCreator<TYPE, INGREDIENT> {
    final override fun create(type: TYPE, size: Int): INGREDIENT = holder(getHolder(type), size)

    final override fun create(types: Collection<TYPE>, size: Int): INGREDIENT = holders(types.map(::getHolder), size)

    protected abstract fun getHolder(type: TYPE): Holder<TYPE>

    final override fun tag(tagKey: TagKey<TYPE>, size: Int): INGREDIENT = holderSet(getter.getOrThrow(tagKey), size)

    final override fun tags(tagKeys: Collection<TagKey<TYPE>>, size: Int): INGREDIENT = when (tagKeys.size) {
        1 -> tag(tagKeys.first(), size)
        else -> holderSets(tagKeys.map(getter::getOrThrow), size)
    }
}
