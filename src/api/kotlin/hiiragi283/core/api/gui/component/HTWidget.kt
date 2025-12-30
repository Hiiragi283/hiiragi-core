package hiiragi283.core.api.gui.component

import hiiragi283.core.api.math.HTBoundsProvider
import net.minecraft.client.gui.components.Renderable
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@OnlyIn(Dist.CLIENT)
interface HTWidget :
    HTBoundsProvider,
    Renderable
