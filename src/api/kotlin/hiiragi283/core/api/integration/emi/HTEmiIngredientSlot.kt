package hiiragi283.core.api.integration.emi

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture
import com.lowdragmc.lowdraglib2.gui.ui.Style
import com.lowdragmc.lowdraglib2.gui.ui.elements.BindableUIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.FluidSlot
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.event.HoverTooltips
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext
import com.lowdragmc.lowdraglib2.gui.ui.style.Property
import com.lowdragmc.lowdraglib2.gui.ui.style.PropertyRegistry
import com.lowdragmc.lowdraglib2.integration.xei.IngredientIO
import com.lowdragmc.lowdraglib2.integration.xei.emi.LDLibEMIPlugin
import com.lowdragmc.lowdraglib2.registry.annotation.LDLRegister
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.stack.EmiStackInteraction
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

/**
 * @see FluidSlot
 * @see ItemSlot
 */
@LDLRegister(name = "emi-ingredient-slot", group = "misc", registry = "ldlib2:ui_element")
class HTEmiIngredientSlot : BindableUIElement<EmiIngredient>() {
    companion object {
        @JvmStatic
        private val PROPERTIES: Array<Property<*>> = arrayOf(
            PropertyRegistry.HOVER_OVERLAY,
        )

        @JvmStatic
        fun input(): HTEmiIngredientSlot = HTEmiIngredientSlot()
            .setIngredient(IngredientIO.INPUT)
            .setRecipeSlot()

        @JvmStatic
        fun catalyst(): HTEmiIngredientSlot = HTEmiIngredientSlot()
            .setIngredient(IngredientIO.CATALYST)
            .setRecipeSlot()

        @JvmStatic
        fun output(): HTEmiIngredientSlot = HTEmiIngredientSlot()
            .setIngredient(IngredientIO.OUTPUT)
            .setRecipeSlot()
    }

    inner class SlotStyle : Style(this) {
        init {
            setDefault(PropertyRegistry.HOVER_OVERLAY, ItemSlot.DRAGGING_BG)
        }

        override fun getProperties(): Array<out Property<*>> = PROPERTIES

        fun hoverOverlay(): IGuiTexture = getValueSave(PropertyRegistry.HOVER_OVERLAY)

        fun hoverOverlay(texture: IGuiTexture): SlotStyle = apply {
            set(PropertyRegistry.HOVER_OVERLAY, texture)
        }
    }

    val slotStyle = SlotStyle()

    // Runtime
    var ingredient: EmiIngredient = EmiStack.EMPTY
        private set

    init {
        layout.width(18f)
        layout.height(18f)
        layout.paddingAll(1f)
        style.backgroundTexture(ItemSlot.ITEM_SLOT_TEXTURE)
        addEventListener(UIEvents.HOVER_TOOLTIPS, ::onHoverTooltip)
        LDLibEMIPlugin.stackProvider(this) {
            val ingredient: EmiIngredient = this.value
            if (ingredient.isEmpty) return@stackProvider null
            EmiStackInteraction(ingredient, null, false)
        }
        internalSetup()
    }

    inline fun slotStyle(action: SlotStyle.() -> Unit): HTEmiIngredientSlot = apply {
        slotStyle.action()
    }

    fun setPhantom(): HTEmiIngredientSlot = apply {
        LDLibEMIPlugin.renderDragHandler(this) { true }
        LDLibEMIPlugin.dropStackHandler(this, { true }, ::setValue)
    }

    fun setIngredient(io: IngredientIO): HTEmiIngredientSlot = apply {
        LDLibEMIPlugin.recipeIngredient(this, io) { listOf(this.value) }
    }

    fun setRecipeSlot(): HTEmiIngredientSlot = apply {
        LDLibEMIPlugin.recipeSlot(this, this::getValue)
    }

    fun setIngredient(ingredient: EmiIngredient, notify: Boolean = true): HTEmiIngredientSlot = setValue(ingredient, notify)

    fun getFullTooltipTexts(): List<Component> = buildList {
        addAll(style.tooltips().asList())
    }

    private fun onHoverTooltip(event: UIEvent) {
        val ingredient: EmiIngredient = value
        if (ingredient.isEmpty) return
        event.hoverTooltips = HoverTooltips(getFullTooltipTexts(), null, null, null)
    }

    override fun getValue(): EmiIngredient = ingredient

    override fun setValue(value: EmiIngredient?, notify: Boolean): HTEmiIngredientSlot {
        val value1: EmiIngredient = when {
            value == null -> EmiStack.EMPTY
            else -> value
        }
        if (value1 == this.value) return this
        this.ingredient = value1
        if (notify) notifyListeners()
        return this
    }

    override fun drawBackgroundAdditional(guiContext: GUIContext) {
        val ingredient: EmiIngredient = value
        val hovered: Boolean = isHover || isSelfOrChildHover
        if (ingredient.isEmpty && !hovered) return

        val contentX: Float = getContentX()
        val contentY: Float = getContentY()
        val contentWidth: Float = getContentWidth()
        val contentHeight: Float = getContentHeight()

        if (!ingredient.isEmpty) {
            ingredient.render(
                guiContext.graphics,
                contentX.toInt(),
                contentY.toInt(),
                Minecraft.getInstance().timer.getGameTimeDeltaPartialTick(false),
            )
        }
        if (hovered) {
            guiContext.drawTexture(slotStyle.hoverOverlay(), contentX, contentY, contentWidth, contentHeight)
        }
    }
}
