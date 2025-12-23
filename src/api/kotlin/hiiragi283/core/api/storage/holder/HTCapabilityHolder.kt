package hiiragi283.core.api.storage.holder

import net.minecraft.core.Direction

/**
 * 搬出入を制御するインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.common.capabilities.holder.IHolder
 */
interface HTCapabilityHolder {
    /**
     * 指定された[面][side]に搬入できるか判定します。
     */
    fun canInsert(side: Direction?): Boolean

    /**
     * 指定された[面][side]から搬出できるか判定します。
     */
    fun canExtract(side: Direction?): Boolean
}
