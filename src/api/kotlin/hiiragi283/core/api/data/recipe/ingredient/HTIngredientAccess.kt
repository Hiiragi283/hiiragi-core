package hiiragi283.core.api.data.recipe.ingredient

import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.core.HolderGetter
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

interface HTIngredientAccess {
    companion object {
        @JvmField
        val INSTANCE: HTIngredientAccess = HiiragiCoreAPI.getService()
    }

    fun itemCreator(getter: HolderGetter<Item>): HTItemIngredientCreator

    fun fluidCreator(getter: HolderGetter<Fluid>): HTFluidIngredientCreator
}
