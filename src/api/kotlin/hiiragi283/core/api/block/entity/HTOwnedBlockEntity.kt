package hiiragi283.core.api.block.entity

import com.mojang.authlib.GameProfile
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.common.UsernameCache
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import java.util.UUID

/**
 * 所有者を保持するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see net.minecraft.world.entity.OwnableEntity
 */
fun interface HTOwnedBlockEntity {
    companion object {
        /**
         * [偽のプレイヤー][FakePlayer]で使用される[プロファイル][GameProfile]のインスタンス
         */
        @JvmField
        val FAKE_PROFILE = GameProfile(UUID.fromString("32fe5dc2-f03b-4230-baf2-1ffc07d3d818"), "[Hiiragi Core]")
    }

    /**
     * 保持している所有者の[UUID]を取得します。
     * @return 所有者がいない場合は`null`
     */
    fun getOwner(): UUID?

    /**
     * 保持している所有者の名前を取得します。
     * @return 所有者がいない場合は`???`
     */
    fun getOwnerName(): String = getOwner()?.let(UsernameCache::getLastKnownUsername) ?: "???"

    /**
     * 保持している所有者の[インスタンス][ServerPlayer]を取得します。
     * @return 所有者がいない場合は`null`
     */
    fun getOwnerPlayer(level: ServerLevel): ServerPlayer? = getOwnerPlayer(level.server)

    /**
     * 保持している所有者の[インスタンス][ServerPlayer]を取得します。
     * @return 所有者がいない場合は`null`
     */
    fun getOwnerPlayer(server: MinecraftServer): ServerPlayer? = getOwner()?.let(server.playerList::getPlayer)

    /**
     * 保持している所有者の[UUID]から[プロファイル][GameProfile]を取得します。
     * @return 所有者がいない場合は[HTOwnedBlockEntity.FAKE_PROFILE]
     */
    private fun getOwnerProfile(): GameProfile {
        val owner: UUID = getOwner() ?: return FAKE_PROFILE
        return GameProfile(owner, getOwnerName())
    }

    /**
     * [getOwnerProfile]に基づいて[偽のプレイヤー][FakePlayer]を取得します。
     */
    fun getFakePlayer(level: ServerLevel): ServerPlayer = FakePlayerFactory.get(level, getOwnerProfile())

    /**
     * 保持している所有者の[インスタンス][ServerPlayer]を取得します。
     * @return [getOwnerPlayer]の戻り値が`null`の場合は，[getFakePlayer]に基づいた[偽のプレイヤー][FakePlayer]のインスタンス
     */
    fun getOwnerOrFake(level: ServerLevel): ServerPlayer = getOwnerPlayer(level) ?: getFakePlayer(level)
}
