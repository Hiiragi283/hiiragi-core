package hiiragi283.lib.registry

import hiiragi283.lib.HTRegistries
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialContentsLike
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTMaterialRawEntry
import hiiragi283.lib.resource.SupplierWithId
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey

@JvmRecord
data class HTDeferredMaterialContents(val key: ResourceKey<HTMaterialContents>) :
    SupplierWithId<HTMaterialContents>,
    HTMaterialContentsLike {
    constructor(id: Identifier) : this(HTRegistries.Keys.MATERIAL_CONTENTS.createKey(id))

    override fun get(): HTMaterialContents = HTRegistries.MATERIAL_CONTENTS.getOrThrow(key).value()

    override fun getId(): Identifier = key.identifier()

    override fun getRawEntry(key: HTMaterialPartKey): HTMaterialRawEntry? = get().getRawEntry(key)
}
