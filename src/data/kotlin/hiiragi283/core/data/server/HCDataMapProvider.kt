package hiiragi283.core.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.material.HTMaterialLike
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.math.fraction
import hiiragi283.core.api.math.times
import hiiragi283.core.common.material.HCMaterial
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.setup.HCBlocks
import hiiragi283.core.setup.HCItems
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.DataMapProvider
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps
import org.apache.commons.lang3.math.Fraction

class HCDataMapProvider(context: HTDataGenContext) : DataMapProvider(context.output, context.registries) {
    override fun gather(provider: HolderLookup.Provider) {
        compostables()
        furnaceFuels()
    }

    private fun compostables() {
        builder(NeoForgeDataMaps.COMPOSTABLES)
            .add(HCBlocks.WARPED_WART.itemHolder, Compostable(0.5f), false)
    }

    private fun furnaceFuels() {
        val furnace: Builder<FurnaceFuel, Item> = builder(NeoForgeDataMaps.FURNACE_FUELS)

        fun addFuels(material: HTMaterialLike, time: Int) {
            // Block
            for ((prefix: HTMaterialPrefix, _) in HCBlocks.MATERIALS.column(material)) {
                furnace.add(prefix.itemTagKey(material), FurnaceFuel(time * 10), false)
            }
            // Item
            for ((prefix: HTMaterialPrefix, _) in HCItems.MATERIALS.column(material)) {
                val modifier: Fraction = when (prefix) {
                    HCMaterialPrefixes.NUGGET -> fraction(1, 10)
                    else -> Fraction.ONE
                }
                furnace.add(prefix.itemTagKey(material), FurnaceFuel((time * modifier).toInt()), false)
            }
        }

        addFuels(HCMaterial.Fuels.COAL, 20 * 10 * 8)
        addFuels(HCMaterial.Fuels.CHARCOAL, 20 * 10 * 8)
        addFuels(HCMaterial.Fuels.COAL_COKE, 20 * 10 * 16)
        addFuels(HCMaterial.Fuels.CARBIDE, 20 * 10 * 24)
        addFuels(HCMaterial.Gems.CRIMSON_CRYSTAL, 20 * 10 * 24)
        addFuels(HCMaterial.Wood, 20 * 15)
    }
}
