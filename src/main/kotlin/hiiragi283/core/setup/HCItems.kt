package hiiragi283.core.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.endgame.HTAmbrosiaItem
import hiiragi283.core.common.item.endgame.HTCreativeItem
import hiiragi283.core.common.item.endgame.HTEternalUpgradeItem
import hiiragi283.core.common.item.endgame.HTInfinityPotionItem
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildSetMultiMap
import hiiragi283.lib.collection.flatMapTable
import hiiragi283.lib.item.component.buildItemAttributeModifiers
import hiiragi283.lib.item.component.consumables
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.material.name
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.printError
import hiiragi283.lib.util.toTextResult
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Consumables
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.common.NeoForgeMod
import org.slf4j.Logger

data object HCItems {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @JvmField
    val REGISTER = HTDeferredItemRegister(HiiragiCoreAPI.MOD_ID)

    @JvmStatic
    fun register(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    //    Resources    //

    @JvmField
    val RESOURCES: Table<HTMaterialPartKey, HTMaterialKey, HTSimpleDeferredItem> = buildSetMultiMap {
        // Vanilla
        put(VanillaMaterialKeys.COAL, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.CHARCOAL, CommonPartKeys.DUST)

        put(VanillaMaterialKeys.LAPIS, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.QUARTZ, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.AMETHYST, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.DIAMOND, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.EMERALD, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.ECHO, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.PRISMARINE, CommonPartKeys.DUST)

        put(VanillaMaterialKeys.COPPER, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.IRON, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.GOLD, CommonPartKeys.DUST)

        put(VanillaMaterialKeys.NETHERITE, CommonPartKeys.NUGGET)

        put(VanillaMaterialKeys.WOOD, CommonPartKeys.DUST)
        put(VanillaMaterialKeys.OBSIDIAN, CommonPartKeys.DUST)

        put(VanillaMaterialKeys.ENDER_PEARL, CommonPartKeys.DUST)
        // Common
        val rawSet: Set<HTMaterialPartKey> = setOf(CommonPartKeys.RAW)
        val commonMetals: Set<HTMaterialPartKey> = setOf(CommonPartKeys.DUST, CommonPartKeys.NUGGET, CommonPartKeys.INGOT)

        putAll(CommonMaterialKeys.TIN, rawSet + commonMetals)
        putAll(CommonMaterialKeys.IRIDIUM, rawSet + commonMetals)
        putAll(CommonMaterialKeys.PLATINUM, rawSet + commonMetals)
        putAll(CommonMaterialKeys.LEAD, commonMetals)
    }.flatMapTable { (material: HTMaterialKey, parts: Collection<HTMaterialPartKey>) ->
        parts.sorted().map { part: HTMaterialPartKey ->
            val name: String = material.name
            val path: String = when (part) {
                CommonPartKeys.RAW -> "raw_$name"
                else -> "${name}_${part.name}"
            }
            Triple(part, material, REGISTER.registerSimpleItem(path))
        }
    }

    @JvmStatic
    operator fun get(part: HTMaterialPartKey, material: HTMaterialKey): HTSimpleDeferredItem? = RESOURCES[part, material]

    @JvmStatic
    fun getResult(part: HTMaterialPartKey, material: HTMaterialKey): HTTextResult<HTSimpleDeferredItem> {
        val result: HTTextResult<HTSimpleDeferredItem> = get(part, material).toTextResult { "Unregistered part $part for ${material.identifier()}" }
        return result.printError(LOGGER)
    }

    // Mobs
    @JvmField
    val ELDER_HEART: HTSimpleDeferredItem = REGISTER.registerSimpleItem("elder_heart") { it.rarity(Rarity.UNCOMMON) }

    //    Ingredients    //

    // Synthetic
    @JvmField
    val SYNTHETIC_FEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_feather")

    @JvmField
    val SYNTHETIC_FIBER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_fiber")

    @JvmField
    val SYNTHETIC_LEATHER: HTSimpleDeferredItem = REGISTER.registerSimpleItem("synthetic_leather")

    //    End Game    //

    @JvmField
    val IRIDESCENT_POWDER: HTSimpleDeferredItem = REGISTER.registerItem("iridescent_powder", ::HTCreativeItem)

    @JvmField
    val AMBROSIA: HTSimpleDeferredItem = REGISTER.registerItem("ambrosia", ::HTAmbrosiaItem) { it.food(HCFoods.AMBROSIA) }

    @JvmField
    val ETERNAL_UPGRADE: HTSimpleDeferredItem = REGISTER.registerItem("eternal_upgrade", ::HTEternalUpgradeItem)

    @JvmField
    val POTION_OF_INFINITY: HTSimpleDeferredItem = REGISTER.registerItem("potion_of_infinity", ::HTInfinityPotionItem) { it.consumables(Consumables.DEFAULT_DRINK) }

    @JvmField
    val RING_OF_HYPERION: HTSimpleDeferredItem = REGISTER.registerItem("ring_of_hyperion", ::HTCreativeItem) {
        it.attributes(
            buildItemAttributeModifiers {
                add(
                    NeoForgeMod.CREATIVE_FLIGHT,
                    AttributeModifier(
                        HiiragiCoreAPI.id("ring_of_hyperion"),
                        1.0,
                        AttributeModifier.Operation.ADD_VALUE,
                    ),
                    EquipmentSlotGroup.OFFHAND,
                )
            },
        )
    }
}
