package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.tag.HTTagPrefix
import net.minecraft.resources.ResourceLocation

/**
 * テクスチャを生成する際のプリセットを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
class HTMaterialTextureSet(val name: String, val parent: HTMaterialTextureSet?) {
    companion object {
        @JvmField
        val DEFAULT = HTMaterialTextureSet("default", null)

        @JvmField
        val DULL = HTMaterialTextureSet("dull", DEFAULT)

        @JvmField
        val SHINE = HTMaterialTextureSet("shine", DEFAULT)

        /**
         * @since 0.10.0
         */
        @JvmField
        val MYSTICAL = HTMaterialTextureSet("mystical", SHINE)
    }

    operator fun get(prefix: HTTagPrefix): ResourceLocation = parent?.get(prefix) ?: HiiragiCoreAPI.id(name, prefix.name)
}
