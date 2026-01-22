package hiiragi283.core.common.material

import hiiragi283.core.api.collection.HTTable
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialContentsAccess
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.setup.HCMiscRegister

class HTMaterialContentsAccessImpl : HTMaterialContentsAccess {
    override fun getVanillaTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>> = VanillaMaterialKeys.INGREDIENTS

    override fun getBlockTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTBlockHolderLike<*, *>> = HCMiscRegister.materialBlocks

    override fun getItemTable(): HTTable<HTTagPrefix, HTMaterialKey, out HTItemHolderLike<*>> = HCMiscRegister.materialItems

    override fun getToolTable(): HTTable<HTToolType, HTMaterialKey, out HTItemHolderLike<*>> = HCMiscRegister.toolItems
}
