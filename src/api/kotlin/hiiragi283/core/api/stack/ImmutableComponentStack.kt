package hiiragi283.core.api.stack

import net.minecraft.core.component.DataComponentHolder
import net.minecraft.core.component.DataComponentPatch

/**
 * コンポーネントを保持する[ImmutableRegistryStack]の拡張インターフェースです。
 * @param TYPE 保持する種類のクラス
 * @param STACK [ImmutableComponentStack]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
interface ImmutableComponentStack<TYPE : Any, STACK : ImmutableComponentStack<TYPE, STACK>> :
    ImmutableRegistryStack<TYPE, STACK>,
    DataComponentHolder {
    /**
     * このスタックの[DataComponentPatch]を返します。
     */
    fun componentsPatch(): DataComponentPatch
}
