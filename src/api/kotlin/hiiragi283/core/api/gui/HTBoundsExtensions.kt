@file:OnlyIn(Dist.CLIENT)

package hiiragi283.core.api.gui

import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.navigation.ScreenRectangle
import net.minecraft.client.renderer.Rect2i
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

//    Rect2i    //

fun HTBounds.toRec2i(): Rect2i = Rect2i(this.x, this.y, this.width, this.height)

fun Rect2i.toBounds(): HTBounds = HTBounds(this.x, this.y, this.width, this.height)

//    ScreenRectangle    //

fun HTBounds.toRectangle(): ScreenRectangle = ScreenRectangle(this.x, this.y, this.width, this.height)

fun ScreenRectangle.toBounds(): HTBounds = HTBounds(this.left(), this.top(), this.width, this.height)

val LayoutElement.bounds: HTBounds get() = this.rectangle.toBounds()
