package hiiragi283.core.api.storage

import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * [hiiragi283.core.api.storage.stack.HTStackSlot]へのIOを識別するフラグ
 * @see mekanism.api.Action
 */
enum class HTStorageAction {
    EXECUTE,
    SIMULATE,
    ;

    companion object {
        /**
         * [Boolean]から[HTStorageAction]に変換します。
         */
        @JvmStatic
        fun of(simulate: Boolean): HTStorageAction = when (simulate) {
            true -> SIMULATE
            false -> EXECUTE
        }

        /**
         * [IFluidHandler.FluidAction]から[HTStorageAction]に変換します。
         */
        @JvmStatic
        fun of(action: IFluidHandler.FluidAction): HTStorageAction = when (action) {
            IFluidHandler.FluidAction.EXECUTE -> EXECUTE
            IFluidHandler.FluidAction.SIMULATE -> SIMULATE
        }
    }

    /**
     * 処理を仮想で行うかどうか
     */
    fun execute(): Boolean = this == EXECUTE

    /**
     * 処理を仮想で行うかどうか
     */
    fun simulate(): Boolean = this == SIMULATE

    fun combine(execute: Boolean): HTStorageAction = of(!(this.execute() && execute))

    fun toFluid(): IFluidHandler.FluidAction = when (this) {
        EXECUTE -> IFluidHandler.FluidAction.EXECUTE
        SIMULATE -> IFluidHandler.FluidAction.SIMULATE
    }
}
