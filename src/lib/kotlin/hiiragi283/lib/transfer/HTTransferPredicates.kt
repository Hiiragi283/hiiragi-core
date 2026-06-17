package hiiragi283.lib.transfer

import java.util.function.BiPredicate
import java.util.function.Predicate
import net.neoforged.neoforge.transfer.resource.Resource

/**
 * ストレージ周りで使用する条件をまとめたクラスです。
 *
 * 参照 : [Mekanism - ConstantPredicates](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/functions/ConstantPredicates.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
object HTTransferPredicates {
    fun interface ResourcePredicate<out RESOURCE : Resource> : Predicate<@UnsafeVariance RESOURCE> {
        override fun test(resource: @UnsafeVariance RESOURCE): Boolean
    }

    fun interface ResourceBiPredicate<out RESOURCE : Resource> : BiPredicate<@UnsafeVariance RESOURCE, HTHandlerAccess> {
        override fun test(resource: @UnsafeVariance RESOURCE, access: HTHandlerAccess): Boolean
    }

    @JvmStatic
    private val ALWAYS_TRUE: ResourcePredicate<Nothing> = ResourcePredicate { true }

    @JvmStatic
    private val ALWAYS_TRUE_BI: ResourceBiPredicate<Nothing> = ResourceBiPredicate { _, _ -> true }

    @JvmStatic
    private val ALWAYS_FALSE: ResourcePredicate<Nothing> = ResourcePredicate { false }

    @JvmStatic
    private val ALWAYS_FALSE_BI: ResourceBiPredicate<Nothing> = ResourceBiPredicate { _, _ -> false }

    @JvmStatic
    private val INTERNAL_ONLY: ResourceBiPredicate<Nothing> = ResourceBiPredicate { _, access: HTHandlerAccess -> access == HTHandlerAccess.INTERNAL }

    @JvmStatic
    private val NOT_EXTERNAL: ResourceBiPredicate<Nothing> = ResourceBiPredicate { _, access: HTHandlerAccess -> access != HTHandlerAccess.EXTERNAL }

    @JvmStatic
    private val MANUAL_ONLY: ResourceBiPredicate<Nothing> = ResourceBiPredicate { _, access: HTHandlerAccess -> access == HTHandlerAccess.MANUAL }

    /**
     * 常に`true`を返す[ResourcePredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> alwaysTrue(): ResourcePredicate<RESOURCE> = ALWAYS_TRUE

    /**
     * 常に`true`を返す[ResourceBiPredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> alwaysTrueBi(): ResourceBiPredicate<RESOURCE> = ALWAYS_TRUE_BI

    /**
     * 常に`false`を返す[ResourcePredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> alwaysFalse(): ResourcePredicate<RESOURCE> = ALWAYS_FALSE

    /**
     * 常に`false`を返す[ResourceBiPredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> alwaysFalseBi(): ResourceBiPredicate<RESOURCE> = ALWAYS_FALSE_BI

    /**
     * 内部へのアクセスのみを許可する[ResourceBiPredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> internalOnly(): ResourceBiPredicate<RESOURCE> = INTERNAL_ONLY

    /**
     * 外部以外へのアクセスを許可する[ResourceBiPredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> notExternal(): ResourceBiPredicate<RESOURCE> = NOT_EXTERNAL

    /**
     * GUIを介したアクセスのみを許可する[ResourceBiPredicate]を返します。
     */
    @JvmStatic
    fun <RESOURCE : Resource> manualOnly(): ResourceBiPredicate<RESOURCE> = MANUAL_ONLY
}
