package hiiragi283.core.api.gui.element

import com.lowdragmc.lowdraglib2.gui.ColorPattern
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.data.Horizontal
import com.lowdragmc.lowdraglib2.gui.ui.data.Vertical
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label
import com.lowdragmc.lowdraglib2.gui.ui.elements.TextElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.inventory.InventorySlots
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle
import net.minecraft.network.chat.Component
import org.appliedenergistics.yoga.YogaFlexDirection
import org.appliedenergistics.yoga.YogaJustify

fun UIElement.alineCenter(): UIElement = this.layout { style: LayoutStyle -> style.justifyItems(YogaJustify.CENTER) }

fun UIElement.addChildren(children: Iterable<UIElement?>): UIElement = apply {
    for (element: UIElement? in children) {
        addChild(element)
    }
}

fun UIElement.addChildren(children: Sequence<UIElement?>): UIElement = apply {
    for (element: UIElement? in children) {
        addChild(element)
    }
}

inline fun UIElement.addCenterLabel(text: Component, builderAction: Label.() -> Unit = {}): UIElement = apply {
    val label = Label()
    label.setText(text)
    label.textStyle { style: TextElement.TextStyle ->
        style
            .textAlignHorizontal(Horizontal.CENTER)
            .textAlignVertical(Vertical.CENTER)
            .textColor(ColorPattern.GRAY.color)
            .textShadow(false)
    }
    addChild(label.alineCenter())
    label.builderAction()
}

inline fun UIElement.addInventory(builderAction: InventorySlots.() -> Unit = {}): UIElement = apply {
    val inventory = InventorySlots()
    addChild(inventory.layout { style: LayoutStyle -> style.marginTop(5f) })
    inventory.builderAction()
}

inline fun UIElement.addRowChild(builderAction: UIElement.() -> Unit = {}): UIElement = apply {
    val row = UIElement()
    addChild(row.layout { style: LayoutStyle -> style.setFlexDirection(YogaFlexDirection.ROW) })
    row.builderAction()
}
