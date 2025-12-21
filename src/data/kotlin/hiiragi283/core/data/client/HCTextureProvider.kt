package hiiragi283.core.data.client

import com.mojang.blaze3d.platform.NativeImage
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.HTMultiMap
import hiiragi283.core.api.collection.buildMultiMap
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.texture.HTTextureProvider
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.common.data.texture.HCMaterialPalette
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.resources.ResourceLocation
import java.awt.Color
import java.util.function.BiConsumer

class HCTextureProvider(context: HTDataGenContext) : HTTextureProvider(context) {
    override fun gather(output: BiConsumer<ResourceLocation, NativeImage>) {
        material(output, "block") { HCBlocks.MATERIALS.column(it).keys }
        material(output, "item") { HCItems.MATERIALS.column(it).keys }
    }

    private fun material(
        output: BiConsumer<ResourceLocation, NativeImage>,
        pathPrefix: String,
        transform: (HCMaterial) -> Set<HTMaterialPrefix>,
    ) {
        for (material: HCMaterial in HCMaterial.entries) {
            for (prefix: HTMaterialPrefix in transform(material)) {
                val templateImage: NativeImage = material.getTemplateId(prefix)?.let(::getTexture) ?: continue
                val image: NativeImage = copyFrom(templateImage)

                for ((index: Int, pixels: Collection<Pair<Int, Int>>) in createTemplate(templateImage).map) {
                    for ((x: Int, y: Int) in pixels) {
                        image.setPixelRGBA(x, y, argbToFromABGR(material.colorPalette[index].rgb))
                    }
                }
                output.accept(HiiragiCoreAPI.id(pathPrefix, prefix.asPrefixName(), material.asMaterialName()), image)
            }
        }
    }

    private fun createTemplate(image: NativeImage): HTMultiMap<Int, Pair<Int, Int>> = buildMultiMap {
        for (x: Int in (0..<image.width)) {
            for (y: Int in (0..<image.height)) {
                val color = Color(argbToFromABGR(image.getPixelRGBA(x, y)))
                val index: Int = HCMaterialPalette.TEMPLATE[color] ?: continue
                put(index, x to y)
            }
        }
    }

    /**
     * @see mekanism.common.lib.Color.argbToFromABGR
     */
    fun argbToFromABGR(argb: Int): Int {
        val red = argb shr 16 and 0xFF
        val blue = argb and 0xFF
        return argb and -0xff0100 or (blue shl 16) or red
    }
}
