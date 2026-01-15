package hiiragi283.core.api.gui

import com.lowdragmc.lowdraglib2.LDLib2
import com.lowdragmc.lowdraglib2.gui.texture.SpriteTexture
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.style.StylesheetManager
import hiiragi283.core.api.gui.element.addCenterLabel
import hiiragi283.core.api.gui.element.addInventory
import hiiragi283.core.api.gui.element.alineCenter
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player

/**
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
object HTModularUIHelper {
    //    Element    //

    /**
     * @see com.lowdragmc.lowdraglib2.gui.texture.Icons
     */
    @JvmStatic
    fun createIcon(
        x: Int,
        y: Int,
        width: Int,
        height: Int,
    ): UIElement = UIElement()
        .layout { it.height(18f).aspectRatio(1f) }
        .style { it.backgroundTexture(SpriteTexture.of(LDLib2.id("textures/gui/icon/gdp_icons.png")).setSprite(x, y, width, height)) }

    @JvmStatic
    fun rightArrowIcon(): UIElement = createIcon(12 * 4, 12 * 5, 12, 12)

    @JvmStatic
    fun plusIcon(): UIElement = createIcon(12 * 12 + 10 * 4, 0, 10, 10)

    @JvmStatic
    inline fun createRootWithInv(title: Component, action: UIElement.() -> Unit): UIElement {
        val root: UIElement = UIElement().layout { it.paddingAll(4f) }.alineCenter().addClass("panel_bg")
        root.addCenterLabel(title)
        root.action()
        root.addInventory()
        return root
    }

    //    UI    //

    @JvmStatic
    fun createEmptyUI(player: Player? = null): ModularUI = ModularUI(UI.of(), player)

    @JvmStatic
    fun createVanillaUI(root: UIElement, player: Player? = null): ModularUI = ModularUI(
        UI.of(
            root,
            StylesheetManager.INSTANCE.getStylesheetSafe(StylesheetManager.MC),
        ),
        player,
    )

    @JvmStatic
    inline fun createVanillaUI(player: Player, title: Component, action: UIElement.() -> Unit): ModularUI =
        createVanillaUI(createRootWithInv(title, action), player)
}
