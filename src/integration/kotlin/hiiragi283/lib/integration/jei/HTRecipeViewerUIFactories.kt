@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.integration.jei

import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.ScrollDataSource
import com.lowdragmc.lowdraglib2.gui.texture.VanillaSpriteTexture
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.asXeiRecipeIngredient
import com.lowdragmc.lowdraglib2.gui.ui.elements.asXeiRecipeSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.fluidSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.itemSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.withFluid
import com.lowdragmc.lowdraglib2.gui.ui.elements.withItem
import com.lowdragmc.lowdraglib2.gui.ui.layout.pct
import com.lowdragmc.lowdraglib2.gui.ui.layoutDsl
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import dev.vfyjxf.taffy.style.FlexDirection
import dev.vfyjxf.taffy.style.FlexWrap
import hiiragi283.lib.recipe.viewer.display.HTRecipeContents
import hiiragi283.lib.recipe.viewer.display.HTRecipeDisplay
import hiiragi283.lib.resource.vanillaId
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType

/**
 * Hiiragi Seriesで使用されるをまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
data object HTRecipeViewerUIFactories {
    @JvmStatic
    fun itemToItem(display: HTRecipeDisplay.Simple): ModularUI = display(display) { contents: HTRecipeContents ->
        element({
            layout = {
                width(100f.pct)
                height(100f.pct)
                flexDirection(FlexDirection.ROW)
                wrap(FlexWrap.WRAP)
            }
        }, {}).addChildren(
            inputSlot(contents.inputItem(0)),
            progress(),
            outputSlot(contents.outputItem(0)),
        )
    }

    @JvmStatic
    fun itemOrFluid(display: HTRecipeDisplay.Simple): ModularUI = display(display) { contents: HTRecipeContents ->
        element({
            layout = {
                width(100f.pct)
                height(100f.pct)
                flexDirection(FlexDirection.ROW)
                wrap(FlexWrap.WRAP)
            }
        }, {}).addChildren(
            UIElement().addChildren(
                inputSlot(contents.inputItem(0)),
                inputSlot(contents.inputFluid(0)),
            ),
            progress().layoutDsl { margin { top(8) } },
            UIElement().addChildren(
                outputSlot(contents.outputItem(0)),
                outputSlot(contents.outputFluid(0)),
            ),
        )
    }

    @JvmStatic
    fun itemToChancedItems(display: HTRecipeDisplay.Simple): ModularUI = display(display) { contents: HTRecipeContents ->
        element({
            layout = {
                width(100f.pct)
                height(100f.pct)
                flexDirection(FlexDirection.ROW)
                wrap(FlexWrap.WRAP)
            }
        }, {}).addChildren(
            inputSlot(contents.inputItem(0)).layoutDsl { margin { top(8) } },
            progress().layoutDsl { margin { top(8) } },
            UIElement().addChildren(
                outputSlot(contents.outputItem(0)),
                outputSlot(contents.outputItem(2)),
            ),
            UIElement().addChildren(
                outputSlot(contents.outputItem(1)),
                outputSlot(contents.outputItem(3)),
            ),
        )
    }

    @JvmStatic
    inline fun display(display: HTRecipeDisplay.Simple, builderAction: (contents: HTRecipeContents) -> UIElement): ModularUI {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        return ModularUI.of(UI.of(builderAction(display.contents), StylesheetManager.MC))
    }

    //    Elements    //

    @JvmName("itemInputSlot")
    @JvmStatic
    fun inputSlot(items: List<ItemStack>): ItemSlot = itemSlot {
        api {
            xeiRecipeIngredient(IngredientIO.INPUT, items::stream)
            xeiRecipeSlot(IngredientIO.INPUT, 1f, 1, items::stream)
        }
        dataSource(ScrollDataSource.of(items))
    }

    @JvmName("fluidInputSlot")
    @JvmStatic
    fun inputSlot(fluids: HTRecipeContents.FluidInput): FluidSlot = inputSlot(fluids.stacks)

    @JvmName("fluidInputSlot")
    @JvmStatic
    fun inputSlot(fluids: List<FluidStack>): FluidSlot = fluidSlot {
        api {
            xeiRecipeIngredient(IngredientIO.INPUT, fluids::stream)
            xeiRecipeSlot(IngredientIO.INPUT, 1f, FluidType.BUCKET_VOLUME, fluids::stream)
        }
        dataSource(ScrollDataSource.of(fluids))
    }

    @JvmName("itemOutputSlot")
    @JvmStatic
    fun outputSlot(item: ItemStack): ItemSlot = itemSlot {
        withItem(item)
        asXeiRecipeIngredient(IngredientIO.OUTPUT)
        asXeiRecipeSlot(IngredientIO.OUTPUT)
    }

    @JvmName("itemOutputSlot")
    @JvmStatic
    fun outputSlot(item: HTRecipeContents.ChancedItemStack?): ItemSlot = itemSlot {
        item?.stack?.let(::withItem)
        asXeiRecipeIngredient(IngredientIO.OUTPUT)
        asXeiRecipeSlot(IngredientIO.OUTPUT, item?.chance ?: 0f)
    }

    @JvmName("fluidOutputSlot")
    @JvmStatic
    fun outputSlot(fluid: FluidStack): FluidSlot = fluidSlot {
        withFluid(fluid)
        asXeiRecipeIngredient(IngredientIO.OUTPUT)
        asXeiRecipeSlot(IngredientIO.OUTPUT)
    }

    @JvmStatic
    fun progress(): UIElement = element({
        layout = {
            width(24)
            height(16)
            margin { horizontal(4f) }
        }
        style = { backgroundTexture(VanillaSpriteTexture.of(vanillaId("container", "furnace", "burn_progress"))) }
    }, {})
}
