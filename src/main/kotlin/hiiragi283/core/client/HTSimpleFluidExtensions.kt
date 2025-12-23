package hiiragi283.core.client

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.resource.vanillaId
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import java.awt.Color

@OnlyIn(Dist.CLIENT)
class HTSimpleFluidExtensions(val stillTex: ResourceLocation, val color: Color? = null, val floatingTex: ResourceLocation = stillTex) :
    IClientFluidTypeExtensions {
    companion object {
        @JvmStatic
        fun clear(color: Color): HTSimpleFluidExtensions = HTSimpleFluidExtensions(
            vanillaId("block", "water_still"),
            color,
            vanillaId("block", "water_flow"),
        )

        @JvmStatic
        fun dull(color: Color): HTSimpleFluidExtensions = HTSimpleFluidExtensions(
            HTConst.NEOFORGE.toId("block", "milk_still"),
            color,
            HTConst.NEOFORGE.toId("block", "milk_flowing"),
        )

        @JvmStatic
        fun molten(color: Color): HTSimpleFluidExtensions = HTSimpleFluidExtensions(
            HiiragiCoreAPI.id("block", "molten_still"),
            color,
            HiiragiCoreAPI.id("block", "molten_flow"),
        )
    }

    override fun getStillTexture(): ResourceLocation = stillTex

    override fun getFlowingTexture(): ResourceLocation = floatingTex

    override fun getTintColor(): Int = color?.rgb ?: super.getTintColor()
}
