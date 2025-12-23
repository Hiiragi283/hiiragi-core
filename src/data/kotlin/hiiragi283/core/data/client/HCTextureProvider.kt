package hiiragi283.core.data.client

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.texture.HTTextureProvider
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer

class HCTextureProvider(context: HTDataGenContext) : HTTextureProvider(context) {
    override fun gather(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output, HiiragiCoreAPI.MOD_ID, HTConst.BLOCK, HCMaterial.entries) { HCBlocks.MATERIALS.column(it).keys }
        material(output, HiiragiCoreAPI.MOD_ID, HTConst.ITEM, HCMaterial.entries) { HCItems.MATERIALS.column(it).keys }
    }
}
