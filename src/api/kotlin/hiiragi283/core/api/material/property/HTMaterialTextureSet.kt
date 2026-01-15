package hiiragi283.core.api.material.property

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.prefix.HTPrefixLike
import net.minecraft.resources.ResourceLocation

class HTMaterialTextureSet(val name: String, val parent: HTMaterialTextureSet?) {
    companion object {
        @JvmField
        val DEFAULT = HTMaterialTextureSet("default", null)

        @JvmField
        val DULL = HTMaterialTextureSet("dull", DEFAULT)

        @JvmField
        val SHINE = HTMaterialTextureSet("shine", DEFAULT)
    }

    operator fun get(prefix: HTPrefixLike): ResourceLocation = parent?.get(prefix) ?: HiiragiCoreAPI.id(name, prefix.asPrefixName())
}
