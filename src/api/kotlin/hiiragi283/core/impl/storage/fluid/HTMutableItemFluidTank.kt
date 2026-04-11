package hiiragi283.core.impl.storage.fluid

import hiiragi283.core.api.storage.fluid.HTMutableFluidTank

/**
 * [HTItemFluidTank]に対応した[HTMutableFluidTank]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 0.15.0
 */
abstract class HTMutableItemFluidTank :
    HTMutableFluidTank(),
    HTItemFluidTank
