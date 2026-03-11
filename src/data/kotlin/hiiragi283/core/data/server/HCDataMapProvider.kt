package hiiragi283.core.data.server

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.map.HTDataMapProvider
import hiiragi283.core.setup.HCBlocks
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel

class HCDataMapProvider(context: HTDataGenContext) : HTDataMapProvider(context) {
    override fun gatherInternal() {
        furnaceFuel {
            addHolder(HCBlocks.OIL_SAND, FurnaceFuel(20 * 10 * 4))
            addHolder(HCBlocks.OIL_SHALE, FurnaceFuel(20 * 10 * 4))
        }
    }
}
