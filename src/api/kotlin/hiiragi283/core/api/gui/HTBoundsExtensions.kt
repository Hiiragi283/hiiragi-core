package hiiragi283.core.api.gui

import net.minecraft.client.renderer.Rect2i
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
fun HTBounds.toRec2i(): Rect2i = Rect2i(this.x, this.y, this.width, this.height)
