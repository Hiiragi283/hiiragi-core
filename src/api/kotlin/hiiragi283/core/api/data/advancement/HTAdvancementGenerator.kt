package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.HTBuilderMarker
import net.minecraft.core.HolderLookup
import net.neoforged.neoforge.common.data.ExistingFileHelper

/**
 * 進捗を登録するクラスです。
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
abstract class HTAdvancementGenerator {
    /**
     * [進捗の出力先][output]のインスタンス
     */
    protected lateinit var output: HTAdvancementOutput
        private set

    /**
     * [HTAdvancementProvider.run]内で呼び出されるメソッドです。
     */
    fun generate(provider: HolderLookup.Provider, output: HTAdvancementOutput, helper: ExistingFileHelper) {
        this.output = output
        generate(provider)
    }

    /**
     * 進捗を生成します。
     */
    protected abstract fun generate(registries: HolderLookup.Provider)

    //    Extension    //

    @HTBuilderMarker
    protected inline fun root(key: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
        HTAdvancementBuilder.root().apply(builderAction).save(output, key)
    }

    @HTBuilderMarker
    protected inline fun child(key: HTAdvancementKey, parent: HTAdvancementKey, builderAction: HTAdvancementBuilder.() -> Unit) {
        HTAdvancementBuilder.child(parent).apply(builderAction).save(output, key)
    }
}
