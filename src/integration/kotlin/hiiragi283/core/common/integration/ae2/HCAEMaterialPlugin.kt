package hiiragi283.core.common.integration.ae2

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import appeng.core.definitions.BlockDefinition
import appeng.core.definitions.ItemDefinition
import com.mojang.datafixers.util.Either
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.property.HTDefaultPart
import hiiragi283.core.api.material.property.HTMaterialPropertyKeys
import hiiragi283.core.api.material.property.HTStorageBlockProperty
import hiiragi283.core.api.material.property.addItemPrefixes
import hiiragi283.core.api.material.property.setDefaultPart
import hiiragi283.core.api.material.property.setName
import hiiragi283.core.api.material.property.setTextureSet
import hiiragi283.core.api.plugin.HTMaterialPlugin
import hiiragi283.core.api.plugin.HTPlugin
import hiiragi283.core.api.registry.HTBlockHolderLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.createKey
import hiiragi283.core.common.integration.HCIConstants
import hiiragi283.core.common.material.HCIntegrationMaterialKeys
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

@HTPlugin
data object HCAEMaterialPlugin : HTMaterialPlugin {
    override val priority: Int = 0

    override fun getId(): ResourceLocation = HiiragiCoreAPI.id("material_plugin", HCIConstants.AE2)

    override fun registerExistingBlock(consumer: HTMaterialPlugin.BlockConsumer) {
        // Gem
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.CERTUS_QUARTZ, AEBlocks.QUARTZ_BLOCK.toLike())
        consumer.accept(CommonParts.BLOCK, HCIntegrationMaterialKeys.FLUIX, AEBlocks.FLUIX_BLOCK.toLike())
    }

    override fun registerExistingItem(consumer: HTMaterialPlugin.ItemConsumer) {
        // Gem
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.CERTUS_QUARTZ, AEItems.CERTUS_QUARTZ_DUST.toLike())
        consumer.accept(CommonParts.GEM, HCIntegrationMaterialKeys.CERTUS_QUARTZ, AEItems.CERTUS_QUARTZ_CRYSTAL.toLike())

        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.FLUIX, AEItems.FLUIX_DUST.toLike())
        consumer.accept(CommonParts.GEM, HCIntegrationMaterialKeys.FLUIX, AEItems.FLUIX_CRYSTAL.toLike())
        // Other
        consumer.accept(CommonParts.DUST, HCIntegrationMaterialKeys.SKY_STONE, AEItems.SKY_DUST.toLike())
    }

    override fun modifyMaterial(provider: HTMaterialPlugin.MaterialProvider) {
        // Gem
        provider.getBuilder(HCIntegrationMaterialKeys.CERTUS_QUARTZ).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            addItemPrefixes(CommonParts.RAW, CommonParts.CRUSHED_ORE)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Certus Quartz", "ケルタスクォーツ")
            setTextureSet("amethyst")
        }
        provider.getBuilder(HCIntegrationMaterialKeys.FLUIX).apply {
            setDefaultPart(HTDefaultPart.Prefixed.GEM)
            put(HTMaterialPropertyKeys.STORAGE_BLOCK, HTStorageBlockProperty.TWO_BY_TWO)

            setName("Fluix Crystal", "フルーシュ")
            setTextureSet("amethyst")
        }
        // Other
        provider.getBuilder(HCIntegrationMaterialKeys.SKY_STONE).apply {
            setName("Sky Stone", "スカイストーン")
        }
    }

    //    Extensions    //

    fun <BLOCK : Block> BlockDefinition<BLOCK>.toLike(): HTBlockHolderLike<BLOCK> = object : HTBlockHolderLike<BLOCK> {
        override fun unwrap(): Either<ResourceKey<Block>, Holder<Block>> = Either.left(Registries.BLOCK.createKey(this@toLike.id()))

        override fun get(): BLOCK = this@toLike.block()
    }

    fun <ITEM : Item> ItemDefinition<ITEM>.toLike(): HTItemHolderLike<ITEM> = object : HTItemHolderLike.Simple<ITEM> {
        override fun unwrap(): Either<ResourceKey<Item>, Holder<Item>> = Either.left(Registries.ITEM.createKey(this@toLike.id()))

        override fun get(): ITEM = this@toLike.asItem()
    }
}
