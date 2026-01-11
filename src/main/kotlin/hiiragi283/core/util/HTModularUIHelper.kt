package hiiragi283.core.util

import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager
import hiiragi283.core.api.HTDefaultColor
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import org.appliedenergistics.yoga.YogaEdge
import org.appliedenergistics.yoga.YogaFlexDirection
import org.appliedenergistics.yoga.YogaJustify

/**
 * @see com.lowdragmc.lowdraglib2.test.TestBlockEntity.createUI
 */
object HTModularUIHelper {
    @JvmStatic
    fun createRow(): UIElement = UIElement().layout { style: LayoutStyle -> style.setFlexDirection(YogaFlexDirection.ROW) }

    //    UI    //

    @JvmStatic
    fun createEmptyUI(player: Player? = null): ModularUI = ModularUI(UI.of(), player)

    @JvmStatic
    inline fun createUIWithInv(player: Player, title: Component, action: UIElement.() -> Unit): ModularUI {
        val root: UIElement = UIElement()
            .layout { style: LayoutStyle ->
                style
                    .setPadding(YogaEdge.ALL, 4f)
                    .setJustifyContent(YogaJustify.CENTER)
            }.addClass("panel_bg")
        root.addChild(
            Label()
                .setText(title)
                .textStyle { style: TextElement.TextStyle -> style.textColor(HTDefaultColor.GRAY.color) }
        )
        root.action()

        val inventory = InventorySlots()
        inventory.layout.setMargin(YogaEdge.TOP, 5f)
        root.addChild(inventory)

        return ModularUI(
            UI.of(
                root,
                StylesheetManager.INSTANCE.getStylesheetSafe(StylesheetManager.MC),
            ),
            player,
        )
    }
}
