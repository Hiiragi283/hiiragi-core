package hiiragi283.core.common.integration

import com.buuz135.replication.ReplicationRegistry
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.registry.toBlockLike
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import net.minecraft.resources.ResourceLocation

@HTPlugin
data object HTReplicationMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = 0

    override fun getId(): ResourceLocation = HCIConstants.REPLICATION.toId("material_plugin", HiiragiCoreAPI.MOD_ID)

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.REPLICA, ReplicationRegistry.Blocks.REPLICA_BLOCK.toBlockLike())
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        consumer.accept(CommonParts.INGOT, HCIntegrationMaterialKeys.REPLICA, ReplicationRegistry.Items.REPLICA_INGOT.toLike())
        consumer.accept(CommonParts.RAW, HCIntegrationMaterialKeys.REPLICA, ReplicationRegistry.Items.RAW_REPLICA.toLike())
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        provider.getBuilder(HCIntegrationMaterialKeys.REPLICA).apply {
            setDefaultPart(HTDefaultPart.Prefixed.INGOT)
            addItemPrefixes(CommonParts.DUST, CommonParts.CRUSHED_ORE, CommonParts.NUGGET)
            put(HTMaterialPropertyKeys.ORIGIN_MOD_ID, HCIConstants.REPLICATION)

            setName("Replica", "レプリカ")
        }
    }
}
