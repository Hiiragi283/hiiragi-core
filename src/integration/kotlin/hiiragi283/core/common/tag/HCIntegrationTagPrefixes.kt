package hiiragi283.core.common.tag

import hiiragi283.core.api.resource.toId
import hiiragi283.core.api.tag.HTTagPrefix
import hiiragi283.core.api.tag.RawTagKey
import hiiragi283.core.common.integration.HCIConstants

/**
 * 参照 : [HiiragiCore - CommonTagPrefixes][hiiragi283.core.api.tag.CommonTagPrefixes], [Mekanism - ResourceType](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/resource/ResourceType.java)
 */
data object HCIntegrationTagPrefixes {
    //    Immersive    //

    @JvmField
    val SHEETMETAL: HTTagPrefix = HTTagPrefix("sheetmetals", "sheetmetals/%s")

    //    Mekanism    //

    @JvmField
    val ALLOY: HTTagPrefix = HTTagPrefix("alloys", "alloys/%s")

    @JvmField
    val CIRCUIT: HTTagPrefix = HTTagPrefix("circuit", "circuits/%s")

    @JvmField
    val DIRTY_DUST: HTTagPrefix = createMek("dirty_dusts", "dirty_dusts/%s")

    @JvmField
    val CLUMP: HTTagPrefix = createMek("clumps", "clumps/%s")

    @JvmField
    val SHARD: HTTagPrefix = createMek("shards", "shards/%s")

    @JvmField
    val CRYSTAL: HTTagPrefix = createMek("crystals", "crystals/%s")

    @JvmStatic
    private fun createMek(path: String, tagPath: String): HTTagPrefix = HTTagPrefix(RawTagKey.create(HCIConstants.MEKANISM.toId(path)), tagPath)
}
