package hiiragi283.core.setup

import hiiragi283.core.api.HCConstants
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.text.HCTranslation
import hiiragi283.core.common.item.consume.HTClearRandomEffectConsumeEffect
import hiiragi283.core.common.recipe.ingredient.HTDamageableIngredient
import hiiragi283.lib.HTConstants
import hiiragi283.lib.HTRegistries
import hiiragi283.lib.item.HTCreativeModeTabHelper
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialContents
import hiiragi283.lib.material.HTMaterialItemEntry
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.HTPartTagManager
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.recipe.ingredient.HTMaterialPartIngredient
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTTagPrefix
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.registries.NeoForgeRegistries
import net.neoforged.neoforge.registries.RegisterEvent

internal data object HCMiscRegister {
    @JvmStatic
    fun register(event: RegisterEvent) {
        // Consume Effect Type
        event.register(Registries.CONSUME_EFFECT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("clear_random_effect"), HTClearRandomEffectConsumeEffect.TYPE)
        }
        // Creative Mode Tab
        event.register(Registries.CREATIVE_MODE_TAB) { helper ->
            helper.register(
                HCCreativeTabs.COMMON,
                HTCreativeModeTabHelper.createSimpleTab(HCTranslation.HIIRAGI_CORE, HCItems.IRIDESCENT_POWDER) { parameters: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                    // Items
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = HCItems.REGISTER.asSequence())
                    // Blocks
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = HCBlocks.REGISTER.asItemSequence())
                    // Fluids
                    HTCreativeModeTabHelper.addToDisplay(parameters, output, items = HCFluids.REGISTER.asItemSequence())
                },
            )
        }
        // Data Component Type
        event.register(Registries.DATA_COMPONENT_TYPE) { helper ->
            helper.register(HiiragiCoreAPI.id("bottle_type"), HCDataComponents.BOTTLE_TYPE)
            helper.register(HiiragiCoreAPI.id(HTConstants.FLUID), HCDataComponents.FLUID)
        }
        // Recipe Serializer
        event.register(Registries.RECIPE_SERIALIZER) { helper ->
            helper.register(HiiragiCoreAPI.id("eternal_upgrade"), HCRecipeSerializers.ETERNAL_UPGRADE)

            helper.register(HiiragiCoreAPI.id(HCConstants.CHARGING), HCRecipeSerializers.CHARGING)
            helper.register(HiiragiCoreAPI.id(HCConstants.CHOPPING), HCRecipeSerializers.CHOPPING)
            helper.register(HiiragiCoreAPI.id(HCConstants.CRUSHING), HCRecipeSerializers.CRUSHING)
            helper.register(HiiragiCoreAPI.id(HCConstants.EXPLODING), HCRecipeSerializers.EXPLODING)

            helper.register(HiiragiCoreAPI.id(HCConstants.EMPTYING), HCRecipeSerializers.EMPTYING)
            helper.register(HiiragiCoreAPI.id(HCConstants.FILLING), HCRecipeSerializers.FILLING)
        }

        // Attachment Type
        event.register(NeoForgeRegistries.Keys.ATTACHMENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("in_world_recipe_caches"), HCAttachmentTypes.IN_WORLD_RECIPE_CACHES)
        }
        // Ingredient Type
        event.register(NeoForgeRegistries.Keys.INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("material_part"), HTMaterialPartIngredient.TYPE)
            helper.register(HiiragiCoreAPI.id("damageable"), HTDamageableIngredient.TYPE)
        }
        // Fluid Ingredient Type
        event.register(NeoForgeRegistries.Keys.FLUID_INGREDIENT_TYPES) { helper ->
            helper.register(HiiragiCoreAPI.id("potion"), HTPotionFluidIngredient.TYPE)
        }

        // Item Result Serializer
        event.register(HTRegistries.Keys.ITEM_RESULT_SERIALIZER) { helper ->
            helper.register(HiiragiCoreAPI.id("tag"), HTItemResult.Tagged.SERIALIZER)
            helper.register(HiiragiCoreAPI.id("part"), HTItemResult.MaterialPart.SERIALIZER)
        }
        // Material Contents
        event.register(HTRegistries.Keys.MATERIAL_CONTENTS, ::registerMaterial)
    }

    @JvmStatic
    private fun registerMaterial(helper: RegisterEvent.RegisterHelper<HTMaterialContents>) {
        fun register(material: HTMaterialKey, primalKey: HTMaterialPartKey, builderAction: HTMaterialContents.Builder.() -> Unit) {
            helper.register(material, HTMaterialContents.create(material, primalKey, builderAction))
        }

        //    Vanilla    //

        // Fuel
        register(VanillaMaterialKeys.COAL, CommonPartKeys.FUEL) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.COAL_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)
            add(CommonPartKeys.FUEL, HTMaterialItemEntry.ItemEntry(Items.COAL))
        }
        register(VanillaMaterialKeys.CHARCOAL, CommonPartKeys.FUEL) {
            addFromTable()
            add(CommonPartKeys.FUEL, HTMaterialItemEntry.ItemEntry(Items.CHARCOAL))
        }
        // Mineral
        register(VanillaMaterialKeys.REDSTONE, CommonPartKeys.DUST) {
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.REDSTONE_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.DUST, HTMaterialItemEntry.ItemEntry(Items.REDSTONE), CommonTagPrefixes.DUST)
        }
        register(VanillaMaterialKeys.GLOWSTONE, CommonPartKeys.DUST) {
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.GLOWSTONE), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.DUST, HTMaterialItemEntry.ItemEntry(Items.GLOWSTONE_DUST), CommonTagPrefixes.DUST)
        }
        // Gem
        register(VanillaMaterialKeys.LAPIS, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.LAPIS_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.LAPIS_LAZULI), CommonTagPrefixes.GEM)
        }
        register(VanillaMaterialKeys.QUARTZ, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.QUARTZ_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.QUARTZ), CommonTagPrefixes.GEM)
        }
        register(VanillaMaterialKeys.AMETHYST, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.AMETHYST_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.AMETHYST_SHARD), CommonTagPrefixes.GEM)
        }
        register(VanillaMaterialKeys.DIAMOND, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.DIAMOND_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.DIAMOND), CommonTagPrefixes.GEM)
        }
        register(VanillaMaterialKeys.EMERALD, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.EMERALD_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.EMERALD), CommonTagPrefixes.GEM)
        }
        register(VanillaMaterialKeys.ECHO, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.ECHO_SHARD), CommonTagPrefixes.GEM)
        }
        register(VanillaMaterialKeys.PRISMARINE, CommonPartKeys.GEM) {
            addFromTable()
            add(CommonPartKeys.GEM, HTMaterialItemEntry.ItemEntry(Items.PRISMARINE_CRYSTALS), CommonTagPrefixes.GEM)
        }
        // Metal
        register(VanillaMaterialKeys.COPPER, CommonPartKeys.INGOT) {
            addFromTable()
            add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.RAW_COPPER_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.COPPER_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.INGOT, HTMaterialItemEntry.ItemEntry(Items.COPPER_INGOT), CommonTagPrefixes.INGOT)
            add(CommonPartKeys.NUGGET, HTMaterialItemEntry.ItemEntry(Items.COPPER_NUGGET), CommonTagPrefixes.NUGGET)
            add(CommonPartKeys.RAW, HTMaterialItemEntry.ItemEntry(Items.RAW_COPPER), CommonTagPrefixes.RAW_MATERIALS)
        }
        register(VanillaMaterialKeys.IRON, CommonPartKeys.INGOT) {
            addFromTable()
            add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.RAW_IRON_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.IRON_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.INGOT, HTMaterialItemEntry.ItemEntry(Items.IRON_INGOT), CommonTagPrefixes.INGOT)
            add(CommonPartKeys.NUGGET, HTMaterialItemEntry.ItemEntry(Items.IRON_NUGGET), CommonTagPrefixes.NUGGET)
            add(CommonPartKeys.RAW, HTMaterialItemEntry.ItemEntry(Items.RAW_IRON), CommonTagPrefixes.RAW_MATERIALS)
        }
        register(VanillaMaterialKeys.GOLD, CommonPartKeys.INGOT) {
            addFromTable()
            add(CommonPartKeys.RAW_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.RAW_GOLD_BLOCK), CommonTagPrefixes.RAW_STORAGE_BLOCK)
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.GOLD_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.INGOT, HTMaterialItemEntry.ItemEntry(Items.GOLD_INGOT), CommonTagPrefixes.INGOT)
            add(CommonPartKeys.NUGGET, HTMaterialItemEntry.ItemEntry(Items.GOLD_NUGGET), CommonTagPrefixes.NUGGET)
            add(CommonPartKeys.RAW, HTMaterialItemEntry.ItemEntry(Items.RAW_GOLD), CommonTagPrefixes.RAW_MATERIALS)
        }
        // Alloy
        register(VanillaMaterialKeys.NETHERITE, CommonPartKeys.INGOT) {
            addFromTable()
            add(CommonPartKeys.STORAGE_BLOCK, HTMaterialItemEntry.BlockEntry(Blocks.NETHERITE_BLOCK), CommonTagPrefixes.STORAGE_BLOCK)

            add(CommonPartKeys.INGOT, HTMaterialItemEntry.ItemEntry(Items.NETHERITE_INGOT), CommonTagPrefixes.INGOT)
        }
        // Other
        register(VanillaMaterialKeys.WOOD, CommonPartKeys.MISC) {
            addFromTable()
            add(CommonPartKeys.MISC, HTMaterialItemEntry.ItemEntry(Items.OAK_PLANKS), ItemTags.PLANKS)
        }
        register(VanillaMaterialKeys.GLASS, CommonPartKeys.MISC) {
            addFromTable()
            add(CommonPartKeys.MISC, HTMaterialItemEntry.BlockEntry(Blocks.GLASS), Tags.Items.GLASS_BLOCKS_COLORLESS)
        }
        register(VanillaMaterialKeys.STONE, CommonPartKeys.MISC) {
            add(CommonPartKeys.MISC, HTMaterialItemEntry.BlockEntry(Blocks.STONE), Tags.Items.STONES)
        }
        register(VanillaMaterialKeys.OBSIDIAN, CommonPartKeys.MISC) {
            addFromTable()
            add(CommonPartKeys.MISC, HTMaterialItemEntry.BlockEntry(Blocks.OBSIDIAN), Tags.Items.OBSIDIANS_NORMAL)
        }

        register(VanillaMaterialKeys.ENDER_PEARL, CommonPartKeys.MISC) {
            addFromTable()
            add(CommonPartKeys.MISC, HTMaterialItemEntry.ItemEntry(Items.ENDER_PEARL), Tags.Items.ENDER_PEARLS)
        }
        register(VanillaMaterialKeys.BLAZE, CommonPartKeys.ROD) {
            add(CommonPartKeys.DUST, HTMaterialItemEntry.ItemEntry(Items.BLAZE_POWDER))
            add(CommonPartKeys.ROD, HTMaterialItemEntry.ItemEntry(Items.BLAZE_ROD), Tags.Items.RODS_BLAZE)
        }
        register(VanillaMaterialKeys.BREEZE, CommonPartKeys.ROD) {
            add(CommonPartKeys.DUST, HTMaterialItemEntry.ItemEntry(Items.WIND_CHARGE))
            add(CommonPartKeys.ROD, HTMaterialItemEntry.ItemEntry(Items.BREEZE_ROD), Tags.Items.RODS_BREEZE)
        }

        register(VanillaMaterialKeys.BRICK, CommonPartKeys.MISC) {
            addFromTable()
            add(CommonPartKeys.MISC, HTMaterialItemEntry.ItemEntry(Items.BRICK), Tags.Items.BRICKS_NORMAL)
        }
        register(VanillaMaterialKeys.NETHER_BRICK, CommonPartKeys.MISC) {
            addFromTable()
            add(CommonPartKeys.MISC, HTMaterialItemEntry.ItemEntry(Items.NETHER_BRICK), Tags.Items.BRICKS_NETHER)
        }
        //    Common    //

        // Fuel
        // Mineral
        // Gem
        // Metal
        register(CommonMaterialKeys.TIN, CommonPartKeys.INGOT) { addFromTable() }

        register(CommonMaterialKeys.IRIDIUM, CommonPartKeys.INGOT) { addFromTable() }
        register(CommonMaterialKeys.PLATINUM, CommonPartKeys.INGOT) { addFromTable() }
        register(CommonMaterialKeys.LEAD, CommonPartKeys.INGOT) { addFromTable() }
        // Alloy
        // Other
    }

    @JvmStatic
    private fun HTMaterialContents.Builder.addFromTable() {
        // Block
        for ((part: HTMaterialPartKey, block: HTSimpleDeferredBlockAndItem) in HCBlocks.RESOURCES.column(this.key)) {
            val prefix: HTTagPrefix? = HTPartTagManager[part]
            if (prefix == null) {
                this.add(part, HTMaterialItemEntry.BlockEntry(block))
            } else {
                this.add(part, HTMaterialItemEntry.BlockEntry(block), prefix)
            }
        }
        // Item
        for ((part: HTMaterialPartKey, item: HTSimpleDeferredItem) in HCItems.RESOURCES.column(this.key)) {
            val prefix: HTTagPrefix? = HTPartTagManager[part]
            if (prefix == null) {
                this.add(part, HTMaterialItemEntry.ItemEntry(item))
            } else {
                this.add(part, HTMaterialItemEntry.ItemEntry(item), prefix)
            }
        }
    }
}
