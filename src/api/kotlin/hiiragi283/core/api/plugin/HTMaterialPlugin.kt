package hiiragi283.core.api.plugin

import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.property.HTPropertyMap
import hiiragi283.core.api.resource.HTIdLike

interface HTMaterialPlugin : HTIdLike {
    val priority: Int

    fun onModifyMaterial(builder: MaterialBuilder) {}

    fun interface MaterialBuilder {
        fun getBuilder(key: HTMaterialKey): HTPropertyMap.Mutable
    }

    fun onRecipeLoad() {}
}
