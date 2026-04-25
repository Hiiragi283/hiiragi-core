package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeCatalystRegistration
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike

data object HTJeiWorkstationHelper {
    @JvmStatic
    fun add(registration: IRecipeCatalystRegistration, recipeType: JeiRecipeType<*>, workstations: List<ItemStack>) {
        registration.addRecipeCatalysts(recipeType, VanillaTypes.ITEM_STACK, workstations)
    }

    @JvmStatic
    fun add(registration: IRecipeCatalystRegistration, viewerType: HTRecipeViewerType<*>, workstations: List<ItemStack>) {
        this.add(registration, HTJeiPlugin.getRecipeType(viewerType), workstations)
    }

    @JvmName("addFromStacks")
    @JvmStatic
    fun add(registration: IRecipeCatalystRegistration, viewerType: HTRecipeViewerType<*>, vararg workstations: ItemStack) {
        this.add(registration, viewerType, workstations.toList())
    }

    @JvmName("addFromItems")
    @JvmStatic
    fun add(registration: IRecipeCatalystRegistration, viewerType: HTRecipeViewerType<*>, vararg workstations: ItemLike) {
        this.add(registration, viewerType, workstations.map(::ItemStack))
    }

    @JvmStatic
    fun addFromViewerType(registration: IRecipeCatalystRegistration, vararg viewerTypes: HTRecipeViewerType<*>) {
        for (viewerType: HTRecipeViewerType<*> in viewerTypes) {
            this.add(registration, viewerType, viewerType.workStations)
        }
    }
}
