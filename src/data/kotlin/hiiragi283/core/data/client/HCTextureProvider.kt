package hiiragi283.core.data.client

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.texture.HTTextureProvider
import net.minecraft.resources.ResourceLocation
import java.util.function.BiConsumer

class HCTextureProvider(context: HTDataGenContext) : HTTextureProvider(HiiragiCoreAPI.MOD_ID, context) {
    override fun gather(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output)
    }
}
