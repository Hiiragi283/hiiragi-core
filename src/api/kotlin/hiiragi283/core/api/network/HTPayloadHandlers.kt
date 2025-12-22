package hiiragi283.core.api.network

import hiiragi283.core.api.text.HTCommonTranslation
import net.minecraft.client.Minecraft
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.handling.IPayloadContext

/**
 * [HTCustomPayload]の処理をまとめたクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
object HTPayloadHandlers {
    fun <T : HTCustomPayload.S2C> handleS2C(payload: T, context: IPayloadContext) {
        context
            .enqueueWork {
                val clientPlayer: AbstractClientPlayer = context.player() as? AbstractClientPlayer ?: return@enqueueWork
                payload.handle(clientPlayer, Minecraft.getInstance())
            }.exceptionally { throwable: Throwable ->
                context.disconnect(HTCommonTranslation.INVALID_PACKET_S2C.translate(throwable.localizedMessage))
                null
            }
    }

    fun <T : HTCustomPayload.C2S> handleC2S(payload: T, context: IPayloadContext) {
        context
            .enqueueWork {
                val serverPlayer: ServerPlayer = context.player() as? ServerPlayer ?: return@enqueueWork
                payload.handle(serverPlayer, serverPlayer.server)
            }.exceptionally { throwable: Throwable ->
                context.disconnect(HTCommonTranslation.INVALID_PACKET_C2S.translate(throwable.localizedMessage))
                null
            }
    }

    fun <T> handleBoth(payload: T, context: IPayloadContext) where T : HTCustomPayload.S2C, T : HTCustomPayload.C2S {
        if (context.player().level().isClientSide) {
            handleS2C(payload, context)
        } else {
            handleC2S(payload, context)
        }
    }
}
