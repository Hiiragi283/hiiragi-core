package hiiragi283.core.api.data.tank

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.HTDataGenContext
import net.minecraft.data.PackOutput
import net.minecraft.server.packs.PackType
import net.neoforged.neoforge.common.data.JsonCodecProvider

/**
 * @author Hiiragi Tsubasa
 * @since 0.14.0
 */
abstract class HTTankInteractionProvider(context: HTDataGenContext, modId: String) :
    JsonCodecProvider<HTTankInteraction.Serializable>(
        context.output,
        PackOutput.Target.DATA_PACK,
        HTConst.TANK_INTERACTION,
        PackType.SERVER_DATA,
        HTTankInteraction.Serializable.CODEC,
        context.registries,
        modId,
        context.fileHelper,
    )
