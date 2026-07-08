package hiiragi283.core.setup

import hiiragi283.core.common.recipe.HTVanillaRecipeTypes
import hiiragi283.lib.math.HTBounds
import hiiragi283.lib.recipe.viewer.HTRecipeViewerType
import hiiragi283.lib.recipe.viewer.HTSimpleRecipeViewerType
import hiiragi283.lib.recipe.viewer.display.HTProgressRecipeDisplay
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.util.Either
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

data object HCRecipeViewerTypes {
    //    Basic    //

    @JvmField
    val BREWING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(HTVanillaRecipeTypes.BREWING, Items.BREWING_STAND, height = 18 * 2)

    @JvmField
    val CHARGING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(HCRecipeTypes.CHARGING, Items.LIGHTNING_ROD)

    @JvmField
    val CHOPPING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(HCRecipeTypes.CHOPPING, HCBlocks.CHOPPING_BOARD, 18 * 5, 18 * 2)

    @JvmField
    val CRUSHING: HTRecipeViewerType<HTProgressRecipeDisplay> = create(HCRecipeTypes.CRUSHING, HCBlocks.FORGING_ANVIL, 18 * 5, 18 * 2)

    @JvmField
    val EXPLODING: HTRecipeViewerType<HTRecipeDisplay.Simple> = create(HCRecipeTypes.EXPLODING, Items.TNT)

    //    Tank Interaction    //

    @JvmField
    val EMPTYING: HTRecipeViewerType<HTRecipeDisplay.Simple> = create(HCRecipeTypes.EMPTYING, Items.BUCKET, height = 18 * 2)

    @JvmField
    val FILLING: HTRecipeViewerType<HTRecipeDisplay.Simple> = create(HCRecipeTypes.FILLING, Items.WATER_BUCKET, height = 18 * 2)

    @JvmStatic
    private inline fun <reified T : Any> create(
        recipeType: HTIdLike.Translatable,
        iconItem: ItemLike,
        width: Int = 18 * 4,
        height: Int = 18 * 1,
        builderAction: HTSimpleRecipeViewerType.Builder.() -> Unit = {},
    ): HTRecipeViewerType<T> = HTSimpleRecipeViewerType.create<T> {
        id = recipeType
        title = recipeType
        val iconStack = ItemStack(iconItem)
        icon = Either.Right(iconStack)
        bounds = HTBounds(0, 0, width, height)
        workStations += iconStack
        builderAction()
    }
}
