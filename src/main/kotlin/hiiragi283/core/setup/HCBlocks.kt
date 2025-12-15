package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.prefix.HTMaterialTable
import hiiragi283.core.common.material.CommonMaterialPrefixes
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.registry.HTSimpleDeferredBlock
import hiiragi283.core.common.registry.register.HTDeferredBlockRegister
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus

object HCBlocks {
    @JvmField
    val REGISTER = HTDeferredBlockRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmStatic
    val MATERIAL: HTMaterialTable<HTSimpleDeferredBlock> = buildTable {
        // Storage Blocks
        for (material: HCMaterial in HCMaterial.entries) {
            val properties: BlockBehaviour.Properties = material.getStorageBlockProp() ?: continue
            this.put(
                CommonMaterialPrefixes.STORAGE_BLOCK.asMaterialPrefix(),
                material.asMaterialKey(),
                REGISTER.registerSimple("${material.asMaterialName()}_block", properties),
            )
        }
    }.let(::HTMaterialTable)
}
