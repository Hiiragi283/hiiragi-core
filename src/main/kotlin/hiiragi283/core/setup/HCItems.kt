package hiiragi283.core.setup

import com.mojang.logging.LogUtils
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.common.item.endgame.HTAmbrosiaItem
import hiiragi283.core.common.item.endgame.HTCreativeItem
import hiiragi283.core.common.item.endgame.HTEternalUpgradeItem
import hiiragi283.core.common.item.endgame.HTInfinityPotionItem
import hiiragi283.lib.collection.Table
import hiiragi283.lib.collection.buildTable
import hiiragi283.lib.item.component.buildItemAttributeModifiers
import hiiragi283.lib.item.component.consumables
import hiiragi283.lib.material.CommonMaterialKeys
import hiiragi283.lib.material.CommonPartKeys
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.HTMaterialPartKey
import hiiragi283.lib.material.VanillaMaterialKeys
import hiiragi283.lib.registry.HTDeferredItemRegister
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.util.HTTextResult
import hiiragi283.lib.util.Identity
import hiiragi283.lib.util.identity
import hiiragi283.lib.util.printError
import hiiragi283.lib.util.right
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
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
    val RESOURCES: Table<HTMaterialPartKey, HTMaterialKey, HTSimpleDeferredItem> = buildTable {
        fun register(part: HTMaterialPartKey, material: HTMaterialKey, operator: Identity<Item.Properties> = identity()) {
            val name: String = material.identifier().path
            val path: String = when (part) {
                CommonPartKeys.RAW -> "raw_$name"
                else -> "${name}_${part.name}"
            }
            this[part, material] = REGISTER.registerSimpleItem(path, operator)
        }

        // Vanilla
        register(CommonPartKeys.NUGGET, VanillaMaterialKeys.NETHERITE) { it.fireResistant() }

        register(CommonPartKeys.DUST, VanillaMaterialKeys.WOOD)
        register(CommonPartKeys.DUST, VanillaMaterialKeys.OBSIDIAN)

        register(CommonPartKeys.DUST, VanillaMaterialKeys.ENDER_PEARL)
        // Common
        fun registerMetals(material: HTMaterialKey, addRaw: Boolean = false) {
            if (addRaw) register(CommonPartKeys.RAW, material)
            register(CommonPartKeys.DUST, material)
            register(CommonPartKeys.INGOT, material)
            register(CommonPartKeys.NUGGET, material)
        }

        registerMetals(CommonMaterialKeys.TIN, true)
        registerMetals(CommonMaterialKeys.IRIDIUM, true)
        registerMetals(CommonMaterialKeys.PLATINUM, true)
        registerMetals(CommonMaterialKeys.LEAD)
        // Hiiragi Core
    }

    @JvmStatic
    operator fun get(part: HTMaterialPartKey, material: HTMaterialKey): HTSimpleDeferredItem? = RESOURCES[part, material]

    @JvmStatic
    fun getResult(part: HTMaterialPartKey, material: HTMaterialKey): HTTextResult<HTSimpleDeferredItem> {
        val result: HTTextResult<HTSimpleDeferredItem> = get(part, material)?.right() ?: HTTextResult("Unregistered part $part for ${material.identifier()}")
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
