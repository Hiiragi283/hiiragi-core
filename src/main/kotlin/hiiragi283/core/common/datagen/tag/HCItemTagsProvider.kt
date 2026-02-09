package hiiragi283.core.common.datagen.tag

import hiiragi283.core.api.collection.forEach
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.item.tool.HTToolType
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.Tags

object HCItemTagsProvider : HTTagsProvider<Item>(Registries.ITEM) {
    fun HTTagBuilder<Item>.addItem(item: ItemLike, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> =
        this.add(HTItemHolderLike.of(item), type)

    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        material(factory)
        tool(factory)
        bucket(factory)
        misc(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        contents.getBlockTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, block: HTIdLike) ->
            addMaterial(factory, prefix, key).add(block)
        }
        contents.getItemTable().forEach { (prefix: HTTagPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
        }

        addMaterial(factory, CommonTagPrefixes.FUEL, VanillaMaterialKeys.CHARCOAL).addItem(Items.CHARCOAL)
        addMaterial(factory, CommonTagPrefixes.FUEL, VanillaMaterialKeys.COAL).addItem(Items.COAL)
        addMaterial(factory, CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO).addItem(Items.ECHO_SHARD)
        addMaterial(factory, CommonTagPrefixes.PEARL, VanillaMaterialKeys.ENDER).addItem(Items.ENDER_PEARL)
        addMaterial(factory, CommonTagPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE).addItem(Items.NETHERITE_SCRAP)

        for ((key: HTMaterialKey, block: ItemLike) in HCBlockTagsProvider.VANILLA_STORAGE_BLOCKS) {
            addMaterial(factory, CommonTagPrefixes.BLOCK, key).addItem(block)
        }

        factory.apply(ItemTags.COALS).add(HCItems.BAMBOO_CHARCOAL)
        factory.apply(ItemTags.PLANKS).add(HCItems.PARTICLE_BOARD)
    }

    //    Tool    //

    private fun tool(factory: BuilderFactory<Item>) {
        contents.getToolTable().forEach { (toolType: HTToolType, _, tool: HTIdLike) ->
            toolType.toolTags.map(factory::apply).forEach { it.add(tool) }
        }

        listOf(
            ItemTags.AXES,
            ItemTags.HOES,
            ItemTags.PICKAXES,
            ItemTags.SHOVELS,
            Tags.Items.MINING_TOOL_TOOLS,
        ).map(factory::apply).forEach { it.add(HCItems.ALMIGHTY_PICKAXE) }
    }

    //    Bucket    //

    private fun bucket(factory: BuilderFactory<Item>) {
        for (content: HTFluidContent in HCFluids.REGISTER.entries) {
            addTags(factory, Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
    }

    //    Misc    //

    private fun misc(factory: BuilderFactory<Item>) {
        // Foods
        factory.apply(Tags.Items.FOODS_GOLDEN).add(HCItems.AMBROSIA)
        // Others
        factory.apply(Tags.Items.LEATHERS).add(HCItems.SYNTHETIC_LEATHER)
        factory.apply(Tags.Items.SLIME_BALLS).add(HCItems.RAW_RUBBER)

        factory
            .apply(HiiragiCoreTags.Items.ELDRITCH_PEARL_BINDER)
            .addItem(Items.GHAST_TEAR)
            .addItem(Items.PHANTOM_MEMBRANE)
            .addItem(Items.WIND_CHARGE)
    }
}
