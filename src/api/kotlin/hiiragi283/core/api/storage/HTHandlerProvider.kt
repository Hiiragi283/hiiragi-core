package hiiragi283.core.api.storage

import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * ストレージ関連のキャパビリティを取得できるオブジェクトを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface HTHandlerProvider {
    /**
     * 指定した[面][direction]から[IItemHandler]を取得します。
     * @return 取得できなかった場合は`null`
     */
    fun getItemHandler(direction: Direction?): IItemHandler?

    /**
     * 指定した[面][direction]から[IFluidHandler]を取得します。
     * @return 取得できなかった場合は`null`
     */
    fun getFluidHandler(direction: Direction?): IFluidHandler?

    /**
     * 指定した[面][direction]から[IEnergyStorage]を取得します。
     * @return 取得できなかった場合は`null`
     */
    fun getEnergyStorage(direction: Direction?): IEnergyStorage?
}
