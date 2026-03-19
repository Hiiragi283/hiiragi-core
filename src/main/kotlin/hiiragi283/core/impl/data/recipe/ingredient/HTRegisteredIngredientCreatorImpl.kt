package hiiragi283.core.impl.data.recipe.ingredient

import hiiragi283.core.api.data.recipe.ingredient.HTIngredientCreator
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.tags.TagKey

abstract class HTRegisteredIngredientCreatorImpl<TYPE : Any, INGREDIENT : Any>(protected val getter: HolderGetter<TYPE>) :
    HTIngredientCreator.Registered<TYPE, INGREDIENT> {
    protected abstract fun getHolder(type: TYPE): Holder<TYPE>

    final override fun from(type: TYPE): INGREDIENT = fromHolder(getHolder(type))

    final override fun from(types: Collection<TYPE>): INGREDIENT = fromHolders(types.map(::getHolder))

    final override fun fromTagKey(tagKey: TagKey<TYPE>): INGREDIENT = fromHolderSet(getter.getOrThrow(tagKey))

    final override fun fromTagKeys(tagKeys: Collection<TagKey<TYPE>>): INGREDIENT = when (tagKeys.size) {
        1 -> return fromTagKey(tagKeys.first())
        else -> fromHolderSets(tagKeys.map(getter::getOrThrow))
    }
}
