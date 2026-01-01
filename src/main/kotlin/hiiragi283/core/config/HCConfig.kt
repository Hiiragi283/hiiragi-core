package hiiragi283.core.config

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.config.definePositiveInt
import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

object HCConfig {
    @JvmField
    val COMMON_SPEC: ModConfigSpec

    @JvmField
    val COMMON: Common

    init {
        val commonPair: Pair<Common, ModConfigSpec> = ModConfigSpec.Builder().configure(::Common)
        COMMON_SPEC = commonPair.right
        COMMON = commonPair.left
    }

    class Common(builder: ModConfigSpec.Builder) {
        @JvmField
        val disableMilkCure: ModConfigSpec.BooleanValue = builder.define("disableMilkCure", false)

        @JvmField
        val expConversionRatio: ModConfigSpec.IntValue = builder.definePositiveInt("expConversionRatio", 20)

        @JvmField
        val tagOutputPriority: ModConfigSpec.ConfigValue<List<String>> =
            builder
                .worldRestart()
                .defineList(
                    "tagOutputModIds",
                    listOf(
                        HiiragiCoreAPI.MOD_ID,
                        HTConst.MINECRAFT,
                        "alltheores",
                        "mekanism",
                    ),
                    { "" },
                    { obj: Any -> obj is String },
                )
    }
}
