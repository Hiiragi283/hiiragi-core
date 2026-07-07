package hiiragi283.core.client.integration.jei

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.element
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.layout.pct
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager
import dev.vfyjxf.taffy.style.FlexDirection
import dev.vfyjxf.taffy.style.FlexWrap
import hiiragi283.core.common.recipe.HCBrewingRecipe
import hiiragi283.core.util.HCPotionFluidHelper
import hiiragi283.lib.integration.jei.HTJeiRecipeHelper
import hiiragi283.lib.integration.jei.HTRecipeViewerUIFactories
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient

/**
 * Hiiragi Coreで使用されるをまとめたクラスです。
 *
 * 参照 : [LDLib2 - TestRecipe](https://github.com/Low-Drag-MC/LDLib2/blob/26.1/src/main/java/com/lowdragmc/lowdraglib2/test/xei/TestRecipe.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.3
 */
data object HCRecipeViewerUIFactories {
    @JvmStatic
    fun brewing(recipe: HCBrewingRecipe): ModularUI = ModularUI.of(
        UI.of(
            element({
                layout = {
                    width(100f.pct)
                    height(100f.pct)
                    flexDirection(FlexDirection.ROW)
                    wrap(FlexWrap.WRAP)
                }
            }, {}).addChildren(
                UIElement().addChildren(
                    HTRecipeViewerUIFactories.inputSlot(HTJeiRecipeHelper.resolveItems(recipe.ingredient)),
                    HTPotionFluidIngredient(recipe.potionFrom).let(HTJeiRecipeHelper::resolveFluids).let(HTRecipeViewerUIFactories::inputSlot),
                ),
                HTRecipeViewerUIFactories.progress(),
                UIElement().addChildren(
                    ItemSlot(),
                    recipe.potionTo.let(HCPotionFluidHelper::createFluid).let(HTRecipeViewerUIFactories::outputSlot),
                ),
            ),
            StylesheetManager.MC,
        ),
    )
}
