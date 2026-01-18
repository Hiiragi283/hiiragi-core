package hiiragi283.core.common.material

import hiiragi283.core.api.collection.ImmutableTable
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.setup.HCMiscRegister

class HTMaterialContentsAccessImpl : HTMaterialContentsAccess {
    override fun getVanillaTable(): ImmutableTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>> = VanillaMaterialKeys.INGREDIENTS

    override fun getBlockTable(): ImmutableTable<HTTagPrefix, HTMaterialKey, out HTBlockHolderLike<*, *>> = HCMiscRegister.materialBlocks

    override fun getItemTable(): ImmutableTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>> = HCMiscRegister.materialItems
}
