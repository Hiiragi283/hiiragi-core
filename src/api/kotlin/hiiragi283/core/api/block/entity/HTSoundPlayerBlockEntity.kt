package hiiragi283.core.api.block.entity

import hiiragi283.core.api.sounds.HTSoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * [SE][SoundEvent]を再生可能な[BlockEntity]に実装するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.4.0
 * @see mekanism.common.tile.interfaces.ITileSound
 */
interface HTSoundPlayerBlockEntity : HTBlockEntityAccess {
    fun getSoundPos(): BlockPos = getBlockPos()

    fun getSoundSource(): SoundSource = SoundSource.BLOCKS

    fun playSound(sound: SoundEvent, volume: Float = 1f, pitch: Float = 1f) {
        getLevel()?.playSound(null, getSoundPos(), sound, getSoundSource(), volume, pitch)
    }

    /**
     * @since 21.1.1.0
     */
    fun playSound(instance: HTSoundInstance) {
        val (sound: Holder<SoundEvent>, volume: Float, pitch: Float) = instance
        playSound(sound.value(), volume, pitch)
    }
}
