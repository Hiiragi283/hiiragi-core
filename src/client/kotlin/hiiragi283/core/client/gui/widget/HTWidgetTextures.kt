package hiiragi283.core.client.gui.widget

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
object HTWidgetTextures {
    @JvmField
    val SLOT: ResourceLocation = create("slot")

    @JvmStatic
    fun create(path: String): ResourceLocation = HiiragiCoreAPI.id(HTConst.TEXTURES, HTConst.GUI, "$path.png")
}
