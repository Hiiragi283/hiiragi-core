package hiiragi283.core.client.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.gui.HTBounds
import hiiragi283.core.api.gui.widget.HTAbstractWidgetRenderer
import hiiragi283.core.api.gui.widget.HTWidget
import hiiragi283.core.api.times
import hiiragi283.core.util.HTSpriteRenderHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.TooltipFlag
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.ClientTooltipFlag
import org.apache.commons.lang3.math.Fraction
import java.util.function.Consumer

@OnlyIn(Dist.CLIENT)
abstract class HTSpriteWidgetRenderer<WIDGET : HTWidget>(widget: WIDGET) : HTAbstractWidgetRenderer<WIDGET>(widget) {
    protected val font: Font = Minecraft.getInstance().font

    override fun render(
        bounds: HTBounds,
        guiGraphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        partialTick: Float,
    ) {
        // Render background
        renderBackground(bounds, guiGraphics)
        // Render sprite
        renderSprite(bounds, guiGraphics)
        // Render tooltip
        if (bounds.contains(mouseX, mouseY)) {
            guiGraphics.renderComponentTooltip(
                font,
                buildList { collectTooltips(this::add, getTooltipFlag()) },
                mouseX,
                mouseY,
            )
        }
    }

    private fun renderSprite(bounds: HTBounds, guiGraphics: GuiGraphics) {
        if (!shouldRender()) return
        val sprite: TextureAtlasSprite = getSprite() ?: return
        val color: Int = getColor()

        val minU: Float = sprite.u0
        val maxU: Float = sprite.u1
        val minV: Float = sprite.v0
        val maxV: Float = sprite.v1
        val delta: Float = maxV - minV
        val fillLevel: Float = getScaledLevel().toFloat()

        RenderSystem.setShaderTexture(0, sprite.atlasLocation())
        RenderSystem.defaultBlendFunc()
        HTSpriteRenderHelper.setShaderColor(guiGraphics, color) {
            RenderSystem.enableBlend()
            val (x: Int, y: Int, width: Int, height: Int) = bounds
            for (i: Int in (0..(Mth.ceil(fillLevel) / width))) {
                val subHeight: Float = minOf(width.toFloat(), fillLevel - (width * i))
                val offsetY: Float = height - width * i - subHeight
                HTSpriteRenderHelper.drawQuad(
                    guiGraphics,
                    x.toFloat(),
                    y + offsetY,
                    width.toFloat(),
                    subHeight,
                    minU,
                    maxV - delta * (subHeight / width),
                    maxU,
                    maxV,
                )
            }
            RenderSystem.disableBlend()
        }
    }

    protected fun getTooltipFlag(): TooltipFlag = ClientTooltipFlag.of(
        when (Minecraft.getInstance().options.advancedItemTooltips) {
            true -> TooltipFlag.ADVANCED
            false -> TooltipFlag.NORMAL
        },
    )

    protected fun getSprite(id: ResourceLocation?, atlas: ResourceLocation = HTConst.BLOCK_ATLAS): TextureAtlasSprite? = when (id) {
        null -> null
        else -> Minecraft.getInstance().getTextureAtlas(atlas).apply(id)
    }

    protected abstract fun renderBackground(bounds: HTBounds, guiGraphics: GuiGraphics)

    protected abstract fun shouldRender(): Boolean

    protected abstract fun getSprite(): TextureAtlasSprite?

    protected abstract fun getColor(): Int

    protected open fun getScaledLevel(): Fraction = getLevel() * widget.bounds.height

    protected abstract fun getLevel(): Fraction

    protected abstract fun collectTooltips(consumer: Consumer<Component>, flag: TooltipFlag)
}
