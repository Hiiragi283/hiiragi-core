package hiiragi283.core.api.storage

import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * スロットへの搬入/搬出の処理を区別するフラグを表すクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 * @see mekanism.api.Action
 */
enum class HTStorageAction {
    /**
     * 実際に処理を行う
     */
    EXECUTE,

    /**
     * 仮想的に処理を行う
     */
    SIMULATE,
    ;

    companion object {
        /**
         * [Boolean]から[HTStorageAction]を取得します。
         */
        @JvmStatic
        fun of(simulate: Boolean): HTStorageAction = when (simulate) {
            true -> SIMULATE
            false -> EXECUTE
        }

        /**
         * [IFluidHandler.FluidAction]から[HTStorageAction]を取得します。
         */
        @JvmStatic
        fun of(action: IFluidHandler.FluidAction): HTStorageAction = when (action) {
            IFluidHandler.FluidAction.EXECUTE -> EXECUTE
            IFluidHandler.FluidAction.SIMULATE -> SIMULATE
        }
    }

    /**
     * 処理を仮想で行うか判定します。
     */
    fun execute(): Boolean = this == EXECUTE

    /**
     * 処理を仮想で行うか判定します。
     */
    fun simulate(): Boolean = this == SIMULATE

    fun combine(execute: Boolean): HTStorageAction = of(!(this.execute() && execute))

    /**
     * [IFluidHandler.FluidAction]に変換します。
     */
    fun toFluid(): IFluidHandler.FluidAction = when (this) {
        EXECUTE -> IFluidHandler.FluidAction.EXECUTE
        SIMULATE -> IFluidHandler.FluidAction.SIMULATE
    }
}
