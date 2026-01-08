package hiiragi283.core.setup

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.item.HTEquipmentMaterial
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.core.common.item.HTAmbrosiaItem
import hiiragi283.core.common.item.HTCaptureEggItem
import hiiragi283.core.common.item.HTCreativeItem
import hiiragi283.core.common.item.HTToolType
import hiiragi283.core.common.item.HTTraderCatalogItem
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.HCMaterialKeys
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.common.registry.HTDeferredItem
import hiiragi283.core.common.registry.HTSimpleDeferredItem
import hiiragi283.core.common.registry.register.HTDeferredItemRegister
import hiiragi283.core.common.text.HCTranslation
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

object HCItems {
    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        eventBus.addListener(::modifyComponents)
    }

    //    Materials   //

    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTSimpleDeferredItem> = buildTable {
        fun register(prefix: HTPrefixLike, key: HTMaterialKey, path: String = prefix.createPath(key)) {
            this[prefix.asMaterialPrefix(), key] = REGISTER.registerSimpleItem(path)
        }

        // Dusts
        arrayOf(
            // Vanilla
            VanillaMaterialKeys.COAL,
            VanillaMaterialKeys.CHARCOAL,
            VanillaMaterialKeys.LAPIS,
            VanillaMaterialKeys.QUARTZ,
            VanillaMaterialKeys.AMETHYST,
            VanillaMaterialKeys.DIAMOND,
            VanillaMaterialKeys.EMERALD,
            VanillaMaterialKeys.ECHO,
            VanillaMaterialKeys.ENDER,
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.IRON,
            VanillaMaterialKeys.GOLD,
            VanillaMaterialKeys.NETHERITE,
            VanillaMaterialKeys.WOOD,
            VanillaMaterialKeys.STONE,
            VanillaMaterialKeys.OBSIDIAN,
            // Common
            CommonMaterialKeys.COAL_COKE,
            CommonMaterialKeys.CARBIDE,
            CommonMaterialKeys.CINNABAR,
            CommonMaterialKeys.SALT,
            CommonMaterialKeys.SALTPETER,
            CommonMaterialKeys.SULFUR,
            CommonMaterialKeys.STEEL,
            // Hiiragi Core
            HCMaterialKeys.AZURE,
            HCMaterialKeys.CRIMSON_CRYSTAL,
            HCMaterialKeys.WARPED_CRYSTAL,
            HCMaterialKeys.ELDRITCH,
            HCMaterialKeys.NIGHT_METAL,
            HCMaterialKeys.AZURE_STEEL,
            HCMaterialKeys.DEEP_STEEL,
        ).forEach { register(HCMaterialPrefixes.DUST, it) }
        // Raws
        arrayOf(
            // Common
            CommonMaterialKeys.PLASTIC,
            CommonMaterialKeys.RUBBER,
            // Hiiragi Core
            HCMaterialKeys.NIGHT_METAL,
        ).forEach { register(HCMaterialPrefixes.RAW_MATERIAL, it) }
        // Fuels
        arrayOf(
            // Common
            CommonMaterialKeys.COAL_COKE,
            CommonMaterialKeys.CARBIDE,
        ).forEach { register(HCMaterialPrefixes.FUEL, it) }
        // Gems
        register(HCMaterialPrefixes.GEM, HCMaterialKeys.AZURE, "azure_shard")
        arrayOf(
            // Hiiragi Core
            HCMaterialKeys.CRIMSON_CRYSTAL,
            HCMaterialKeys.WARPED_CRYSTAL,
        ).forEach { register(HCMaterialPrefixes.GEM, it) }
        // Pearls
        register(HCMaterialPrefixes.PEARL, HCMaterialKeys.ELDRITCH)
        // Ingots
        arrayOf(
            // Common
            CommonMaterialKeys.STEEL,
            // Hiiragi Core
            HCMaterialKeys.NIGHT_METAL,
            HCMaterialKeys.AZURE_STEEL,
            HCMaterialKeys.DEEP_STEEL,
        ).forEach { register(HCMaterialPrefixes.INGOT, it) }
        // Nuggets
        arrayOf(
            // Vanilla
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.NETHERITE,
            // Common
            CommonMaterialKeys.STEEL,
            // Hiiragi Core
            HCMaterialKeys.NIGHT_METAL,
            HCMaterialKeys.AZURE_STEEL,
            HCMaterialKeys.DEEP_STEEL,
        ).forEach { register(HCMaterialPrefixes.NUGGET, it) }
        // Gears
        arrayOf(
            // Vanilla
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.GOLD,
            VanillaMaterialKeys.IRON,
            VanillaMaterialKeys.NETHERITE,
            // Common
            CommonMaterialKeys.STEEL,
            // Hiiragi Core
            HCMaterialKeys.NIGHT_METAL,
            HCMaterialKeys.AZURE_STEEL,
            HCMaterialKeys.DEEP_STEEL,
        ).forEach { register(HCMaterialPrefixes.GEAR, it) }
        // Plates
        arrayOf(
            // Vanilla
            VanillaMaterialKeys.WOOD,
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.IRON,
            VanillaMaterialKeys.GOLD,
            VanillaMaterialKeys.NETHERITE,
            // Common
            CommonMaterialKeys.STEEL,
            CommonMaterialKeys.PLASTIC,
            CommonMaterialKeys.RUBBER,
            // Hiiragi Core
            HCMaterialKeys.NIGHT_METAL,
            HCMaterialKeys.AZURE_STEEL,
            HCMaterialKeys.DEEP_STEEL,
        ).forEach { register(HCMaterialPrefixes.PLATE, it) }
        // Rods
        arrayOf(
            // Metals
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.IRON,
            VanillaMaterialKeys.GOLD,
            HCMaterialKeys.NIGHT_METAL,
            // Alloys
            VanillaMaterialKeys.NETHERITE,
            CommonMaterialKeys.STEEL,
            HCMaterialKeys.AZURE_STEEL,
            HCMaterialKeys.DEEP_STEEL,
        ).forEach { register(HCMaterialPrefixes.ROD, it) }
        // Scrap
        register(HCMaterialPrefixes.SCRAP, HCMaterialKeys.DEEP_STEEL)
        // Wire
        arrayOf(
            // Metals
            VanillaMaterialKeys.COPPER,
            VanillaMaterialKeys.GOLD,
        ).forEach { register(HCMaterialPrefixes.WIRE, it) }
    }.let(::HTMaterialTable)

    @JvmField
    val BAMBOO_CHARCOAL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("bamboo_charcoal")

    @JvmField
    val COMPRESSED_SAWDUST: HTSimpleDeferredItem = REGISTER.registerSimpleItem("compressed_sawdust")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    // Mob
    @JvmField
    val LUMINOUS_PASTE: HTSimpleDeferredItem = REGISTER.registerSimpleItem("luminous_paste")

    @JvmField
    val MAGMA_SHARD: HTSimpleDeferredItem = REGISTER.registerSimpleItem("magma_shard")

    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") {
        it.description(HCTranslation.ELDER_HEART)
    }

    @JvmField
    val WITHER_DOLL: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_doll")

    @JvmField
    val WITHER_STAR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wither_star")

    //    Foods   //

    @JvmField
    val WHEAT_FLOUR: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wheat_flour")

    @JvmField
    val WHEAT_DOUGH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("wheat_dough")

    @JvmField
    val ANIMAL_FAT: HTSimpleDeferredItem = REGISTER.registerSimpleItem("animal_fat")

    @JvmField
    val PULPED_FISH: HTSimpleDeferredItem = REGISTER.registerSimpleItem("pulped_fish")

    @JvmField
    val PULPED_SEED: HTSimpleDeferredItem = REGISTER.registerSimpleItem("pulped_seed")

    //    Tools   //

    @JvmStatic
    val TOOLS: HTMaterialTable<HTToolType<*>, HTDeferredItem<*>> = buildTable {
        fun register(toolType: HTToolType<*>, material: HTEquipmentMaterial) {
            this[toolType, material.asMaterialKey()] = toolType.createItem(REGISTER, material)
        }
    }.let(::HTMaterialTable)

    //    Utilities    //

    @JvmField
    val ELDRITCH_EGG: HTSimpleDeferredItem = REGISTER.registerItem("eldritch_egg", ::HTCaptureEggItem) {
        it.description(HCTranslation.ELDRITCH_EGG)
    }

    @JvmField
    val SLOT_COVER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("slot_cover") {
        it.description(HCTranslation.SLOT_COVER)
    }

    @JvmField
    val TRADER_CATALOG: HTSimpleDeferredItem = REGISTER.registerItem("trader_catalog", ::HTTraderCatalogItem) {
        it.description(HCTranslation.TRADER_CATALOG)
    }

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTCreativeItem) {
        it.description(HCTranslation.IRIDESCENT_POWDER)
    }

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem("ambrosia", ::HTAmbrosiaItem) {
        it.description(HCTranslation.AMBROSIA)
    }

    @JvmField
    val ETERNAL_TICKET: HTSimpleDeferredItem = REGISTER.registerItem("eternal_ticket", ::HTCreativeItem) {
        it.description(HCTranslation.ETERNAL_TICKET)
    }

    //    Event    //

    @JvmStatic
    private fun modifyComponents(event: ModifyDefaultComponentsEvent) {
        fun <T : Any> modify(item: ItemLike, type: DataComponentType<T>, value: T) {
            event.modify(item) { builder: DataComponentPatch.Builder -> builder.set(type, value) }
        }
    }

    //    Extensions    //

    private fun Item.Properties.description(translation: HTTranslation): Item.Properties =
        this.component(HCDataComponents.DESCRIPTION, translation)
}
