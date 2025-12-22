package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture

/**
 * データパック向けに[HTDataGenerator]を実装したクラスです。
 * @param generator [DataProvider]の登録先
 * @param registries レジストリを保持するインスタンス
 * @param fileHelper 指定したリソースが存在するかを判定するインスタンス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
data class HTDataPackGenerator(
    private val generator: DataGenerator.PackGenerator,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
) : HTDataGenerator {
    override fun <DATA : DataProvider> addProvider(factory: DataProvider.Factory<DATA>): DATA = generator.addProvider(factory)

    override fun <DATA : DataProvider> addProvider(factory: GatherDataEvent.DataProviderFromOutputLookup<DATA>): DATA =
        addProvider { output: PackOutput -> factory.create(output, registries) }

    override fun <DATA : DataProvider> addProvider(factory: HTDataGenerator.Factory<DATA>): DATA =
        addProvider { output: PackOutput -> factory.create(HTDataGenContext(output, registries, fileHelper)) }
}
