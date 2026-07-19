package hiiragi283.core.api.integration.jei

import hiiragi283.core.api.recipe.viewer.HTRecipeViewerType
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeCatalystRegistration
import net.minecraft.world.item.ItemStack

/**
 * [IRecipeCatalystRegistration]へのレシピ登録を補助するクラスです。
 * @author Hiiragi Tsubasa
 * @since 21.1.0
 */
@Suppress("NOTHING_TO_INLINE")
@JvmInline
value class HTJeiWorkstationHelper(@PublishedApi internal val registration: IRecipeCatalystRegistration) {
    /**
     * 指定した[recipeType]に[workstation]を登録します。
     */
    inline fun add(recipeType: JeiRecipeType<*>, workstation: ItemStack) {
        this.addAll(recipeType, listOf(workstation))
    }

    /**
     * 指定した[viewerType]に[workstation]を登録します。
     */
    inline fun add(viewerType: HTRecipeViewerType<*>, workstation: ItemStack) {
        this.addAll(viewerType, listOf(workstation))
    }

    /**
     * 指定した[recipeType]に[workstations]を登録します。
     */
    inline fun addAll(recipeType: JeiRecipeType<*>, workstations: List<ItemStack>) {
        if (workstations.isEmpty()) return
        registration.addRecipeCatalysts(recipeType, VanillaTypes.ITEM_STACK, workstations)
    }

    /**
     * 指定した[viewerType]に[workstations]を登録します。
     */
    inline fun addAll(viewerType: HTRecipeViewerType<*>, workstations: List<ItemStack>) {
        this.addAll(HTJeiPlugin.getRecipeType(viewerType), workstations)
    }

    /**
     * [HTRecipeViewerType.workStations]に基づいて登録します。
     */
    inline fun addFromViewerType(vararg viewerTypes: HTRecipeViewerType<*>) {
        for (viewerType: HTRecipeViewerType<*> in viewerTypes) {
            this.addAll(viewerType, viewerType.workStations)
        }
    }
}
