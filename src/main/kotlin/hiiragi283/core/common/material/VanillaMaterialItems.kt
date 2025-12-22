package hiiragi283.core.common.material

import hiiragi283.core.api.collection.buildTable
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.HTMaterialTable
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.material.prefix.HTPrefixLike
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.core.api.registry.toHolderLike
import net.minecraft.world.item.Items
import net.minecraft.world.level.ItemLike

object VanillaMaterialItems {
    @JvmStatic
    val MATERIALS: HTMaterialTable<HTMaterialPrefix, HTItemHolderLike<*>> = buildTable {
        fun add(prefix: HTPrefixLike, material: HTMaterialLike, item: ItemLike) {
            this.add(prefix.asMaterialPrefix(), material.asMaterialKey(), item.toHolderLike())
        }

        // Fuel
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Fuels.COAL, Items.COAL_BLOCK)
        add(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.COAL, Items.COAL)

        add(HCMaterialPrefixes.FUEL, HCMaterial.Fuels.CHARCOAL, Items.CHARCOAL)
        // Minerals
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Minerals.REDSTONE, Items.REDSTONE_BLOCK)
        add(HCMaterialPrefixes.DUST, HCMaterial.Minerals.REDSTONE, Items.REDSTONE)

        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Minerals.GLOWSTONE, Items.GLOWSTONE)
        add(HCMaterialPrefixes.DUST, HCMaterial.Minerals.GLOWSTONE, Items.GLOWSTONE_DUST)
        // Gem
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.LAPIS, Items.LAPIS_BLOCK)
        add(HCMaterialPrefixes.GEM, HCMaterial.Gems.LAPIS, Items.LAPIS_LAZULI)

        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.QUARTZ, Items.QUARTZ_BLOCK)
        add(HCMaterialPrefixes.GEM, HCMaterial.Gems.QUARTZ, Items.QUARTZ)

        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.AMETHYST, Items.AMETHYST_BLOCK)
        add(HCMaterialPrefixes.GEM, HCMaterial.Gems.AMETHYST, Items.AMETHYST_SHARD)

        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.DIAMOND, Items.DIAMOND_BLOCK)
        add(HCMaterialPrefixes.GEM, HCMaterial.Gems.DIAMOND, Items.DIAMOND)

        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Gems.EMERALD, Items.EMERALD_BLOCK)
        add(HCMaterialPrefixes.GEM, HCMaterial.Gems.EMERALD, Items.EMERALD)

        add(HCMaterialPrefixes.GEM, HCMaterial.Gems.ECHO, Items.ECHO_SHARD)
        // Pearl
        add(HCMaterialPrefixes.PEARL, HCMaterial.Pearls.ENDER, Items.ENDER_PEARL)
        // Metals
        add(HCMaterialPrefixes.ORE, HCMaterial.Metals.COPPER, Items.COPPER_ORE)
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW, HCMaterial.Metals.COPPER, Items.RAW_COPPER_BLOCK)
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Metals.COPPER, Items.COPPER_BLOCK)
        add(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.COPPER, Items.RAW_COPPER)
        add(HCMaterialPrefixes.INGOT, HCMaterial.Metals.COPPER, Items.COPPER_INGOT)

        add(HCMaterialPrefixes.ORE, HCMaterial.Metals.IRON, Items.IRON_ORE)
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW, HCMaterial.Metals.IRON, Items.RAW_IRON_BLOCK)
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Metals.IRON, Items.IRON_BLOCK)
        add(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.IRON, Items.RAW_IRON)
        add(HCMaterialPrefixes.INGOT, HCMaterial.Metals.IRON, Items.IRON_INGOT)
        add(HCMaterialPrefixes.NUGGET, HCMaterial.Metals.IRON, Items.IRON_NUGGET)

        add(HCMaterialPrefixes.ORE, HCMaterial.Metals.GOLD, Items.GOLD_ORE)
        add(HCMaterialPrefixes.STORAGE_BLOCK_RAW, HCMaterial.Metals.GOLD, Items.RAW_GOLD_BLOCK)
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Metals.GOLD, Items.GOLD_BLOCK)
        add(HCMaterialPrefixes.RAW_MATERIAL, HCMaterial.Metals.GOLD, Items.RAW_GOLD)
        add(HCMaterialPrefixes.INGOT, HCMaterial.Metals.GOLD, Items.GOLD_INGOT)
        add(HCMaterialPrefixes.NUGGET, HCMaterial.Metals.GOLD, Items.GOLD_NUGGET)
        // Alloys
        add(HCMaterialPrefixes.STORAGE_BLOCK, HCMaterial.Alloys.NETHERITE, Items.NETHERITE_BLOCK)
        add(HCMaterialPrefixes.SCRAP, HCMaterial.Alloys.NETHERITE, Items.NETHERITE_SCRAP)
        add(HCMaterialPrefixes.INGOT, HCMaterial.Alloys.NETHERITE, Items.NETHERITE_INGOT)
    }.let(::HTMaterialTable)
}
