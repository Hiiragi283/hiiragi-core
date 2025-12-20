package hiiragi283.core.api.data.texture

import com.mojang.blaze3d.platform.NativeImage
import net.minecraft.resources.ResourceLocation

fun interface HTTextureOutput {
    fun accept(id: ResourceLocation, image: NativeImage)
}
