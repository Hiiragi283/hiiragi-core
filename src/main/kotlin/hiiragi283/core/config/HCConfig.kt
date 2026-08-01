package hiiragi283.core.config

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.config.definePositiveInt
import net.neoforged.neoforge.common.ModConfigSpec

data object HCConfig {
    @JvmField
    val COMMON_SPEC: ModConfigSpec

    @JvmField
    val SERVER_SPEC: ModConfigSpec

    @JvmField
    val COMMON: Common

    @JvmField
    val SERVER: Server

    init {
        val (common: Common, commonSpec: ModConfigSpec) = ModConfigSpec.Builder().configure(::Common)
        COMMON_SPEC = commonSpec
        COMMON = common
        val (server: Server, serverSpec: ModConfigSpec) = ModConfigSpec.Builder().configure(::Server)
        SERVER_SPEC = serverSpec
        SERVER = server
    }

    class Common(builder: ModConfigSpec.Builder) {
        @JvmField
        val enableDebugFeatures: ModConfigSpec.BooleanValue = builder.define("enableDebugFeatures", false)
    }

    class Server(builder: ModConfigSpec.Builder) {
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
