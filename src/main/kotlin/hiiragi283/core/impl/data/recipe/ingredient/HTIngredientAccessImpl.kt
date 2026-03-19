package hiiragi283.core.impl.data.recipe.ingredient

import hiiragi283.core.api.data.recipe.ingredient.HTFluidIngredientCreator
import hiiragi283.core.api.data.recipe.ingredient.HTIngredientAccess
import hiiragi283.core.api.data.recipe.ingredient.HTItemIngredientCreator
import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

class HTIngredientAccessImpl : HTIngredientAccess {
    override fun itemCreator(getter: HolderGetter<Item>): HTItemIngredientCreator = HTItemIngredientCreatorImpl(getter)

    override fun fluidCreator(getter: HolderGetter<Fluid>): HTFluidIngredientCreator = HTFluidIngredientCreatorImpl(getter)
}
