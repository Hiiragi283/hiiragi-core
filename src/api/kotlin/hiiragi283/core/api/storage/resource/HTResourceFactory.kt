package hiiragi283.core.api.storage.resource

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch

/**
 * [HTResourceType]を作成するメソッドをまとめたインターフェースです。
 * @param TYPE [HTResourceType]が保持する種類のクラス
 * @param STACK スタックのクラス
 * @param RESOURCE [HTResourceType]を実装したクラス
 * @author Hiiragi Tsubasa
 * @since 0.7.0
 */
abstract class HTResourceFactory<TYPE : Any, STACK : Any, RESOURCE : HTResourceType<TYPE>> {
    /**
     * 指定した[type]から[RESOURCE]を作成します。
     * @return [type]が空の値の場合は`null`
     */
    abstract fun fromType(type: TYPE): RESOURCE?

    /**
     * 指定した[stack]から[RESOURCE]を作成します。
     * @return [stack]が空の場合は`null`
     */
    abstract fun fromStack(stack: STACK): RESOURCE?

    /**
     * 指定した[resource]と[amount]から[STACK]を作成します。
     */
    abstract fun createStack(resource: RESOURCE?, amount: Int = getDefaultAmount()): STACK

    /**
     * デフォルトの数量を取得します。
     */
    abstract fun getDefaultAmount(): Int

    /**
     * 指定した[holder]から[RESOURCE]を作成します。
     * @return [holder]の値が空の場合は`null`
     */
    fun fromHolder(holder: Holder<TYPE>): RESOURCE? = fromType(holder.value())

    /**
     * 指定した[type]から[RESOURCE]を作成します。
     * @throws IllegalStateException [type]が空の値の場合
     */
    fun fromTypeOrThrow(type: TYPE): RESOURCE = fromType(type) ?: error("Empty Type: $type")

    /**
     * 指定した[holder]から[RESOURCE]を作成します。
     * @throws IllegalStateException [holder]の値が空の場合
     */
    fun fromHolderOrThrow(holder: Holder<TYPE>): RESOURCE = fromHolder(holder) ?: error("Empty Holder: $holder")

    /**
     * 指定した[stack]から[RESOURCE]を作成します。
     * @throws IllegalStateException [stack]が空の場合
     */
    fun fromStackOrThrow(stack: STACK): RESOURCE = fromStack(stack) ?: error("Empty Stack: $stack")

    //    DataComponent    //

    /**
     * [HTResourceType.DataComponent]に対応した[HTResourceFactory]の拡張インターフェースです。
     * @author Hiiragi Tsubasa
     * @since 0.7.0
     */
    abstract class DataComponent<TYPE : Any, STACK : Any, RESOURCE : HTResourceType.DataComponent<TYPE>> :
        HTResourceFactory<TYPE, STACK, RESOURCE>() {
        /**
         * 指定した[type]と[patch]から[RESOURCE]を作成します。
         * @return [type]が空の値の場合は`null`
         */
        abstract fun create(type: TYPE, patch: DataComponentPatch): RESOURCE?

        /**
         * 指定した[holder]と[patch]から[RESOURCE]を作成します。
         * @return [holder]の値が空の場合は`null`
         */
        fun create(holder: Holder<TYPE>, patch: DataComponentPatch): RESOURCE? = create(holder.value(), patch)

        /**
         * 指定した[type]と[patch]から[RESOURCE]を作成します。
         * @throws IllegalStateException [type]が空の値の場合
         */
        fun createOrThrow(type: TYPE, patch: DataComponentPatch): RESOURCE =
            create(type, patch) ?: error("Empty Type: $type and Patch: $patch")

        override fun fromType(type: TYPE): RESOURCE? = create(type, DataComponentPatch.EMPTY)
    }
}
