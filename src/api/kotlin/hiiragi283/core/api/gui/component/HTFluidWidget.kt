package hiiragi283.core.api.gui.component

import hiiragi283.core.api.storage.fluid.HTFluidView
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

/**
 * 液体向けの[HTWidget]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 */
@OnlyIn(Dist.CLIENT)
interface HTFluidWidget :
    HTFluidView,
    HTWidget
