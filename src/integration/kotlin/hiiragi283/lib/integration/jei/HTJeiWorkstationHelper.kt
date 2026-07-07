package hiiragi283.lib.integration.jei

import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import net.minecraft.world.item.ItemStack

/**
 * [IRecipeCatalystRegistration]へのレシピ登録を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@Suppress("NOTHING_TO_INLINE")
@JvmInline
value class HTJeiWorkstationHelper(@PublishedApi internal val registration: IRecipeCatalystRegistration) {
    /**
     * 指定した[recipeType]に[workstations]を登録します。
     */
    inline fun add(recipeType: IRecipeType<*>, workstations: List<ItemStack>) {
        registration.addCraftingStations(recipeType, VanillaTypes.ITEM_STACK, workstations)
    }

    /**
     * 指定した[viewerType]に[workstations]を登録します。
     */
    inline fun add(viewerType: HTRecipeViewerType<*>, workstations: List<ItemStack>) {
        this.add(HTJeiPlugin.getRecipeType(viewerType), workstations)
    }

    /**
     * [HTRecipeViewerType.workStations]に基づいて登録します。
     */
    inline fun addFromViewerType(vararg viewerTypes: HTRecipeViewerType<*>) {
        for (viewerType: HTRecipeViewerType<*> in viewerTypes) {
            this.add(viewerType, viewerType.workStations)
        }
    }
}
