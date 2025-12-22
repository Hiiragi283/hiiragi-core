package hiiragi283.core.api.data

import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.DataGenerator
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import java.util.concurrent.CompletableFuture
import java.util.function.BooleanSupplier

/**
 * [HTDataGenerator]を実装したクラスです。
 * @param generator [DataProvider]の登録先
 * @param doRun 登録を実行するかどうか判定するブロック
 * @param registries レジストリを保持するインスタンス
 * @param fileHelper 指定したリソースが存在するかを判定するインスタンス
 * @author Hiiragi Tsubasa
 * @since 0.1.0
 */
@ConsistentCopyVisibility
@JvmRecord
data class HTRootDataGenerator private constructor(
    private val generator: DataGenerator,
    private val doRun: BooleanSupplier,
    val registries: CompletableFuture<HolderLookup.Provider>,
    val fileHelper: ExistingFileHelper,
) : HTDataGenerator {
    companion object {
        /**
         * 指定した[イベント][event]から[HTRootDataGenerator]を作成します。
         * @return サーバー向けとクライアント向けの[HTRootDataGenerator]
         */
        @JvmStatic
        fun withDataPack(event: GatherDataEvent): Pair<HTRootDataGenerator, HTRootDataGenerator> {
            val generator: DataGenerator = event.generator
            val registries: CompletableFuture<HolderLookup.Provider> = event.lookupProvider
            val fileHelper: ExistingFileHelper = event.existingFileHelper
            return Pair(
                HTRootDataGenerator(generator, event::includeServer, registries, fileHelper),
                HTRootDataGenerator(generator, event::includeClient, registries, fileHelper),
            )
        }

        /**
         * 指定した[イベント][event]から[HTRootDataGenerator]を作成します。
         * @param builderAction 動的レジストリに要素を追加するブロック
         * @return サーバー向けとクライアント向けの[HTRootDataGenerator]
         */
        @JvmStatic
        fun withDataPack(
            event: GatherDataEvent,
            builderAction: RegistrySetBuilder.() -> Unit,
        ): Pair<HTRootDataGenerator, HTRootDataGenerator> {
            val generator: DataGenerator = event.generator
            val registries: CompletableFuture<HolderLookup.Provider> = generator
                .addProvider(event.includeServer()) { output: PackOutput ->
                    DatapackBuiltinEntriesProvider(
                        output,
                        event.lookupProvider,
                        RegistrySetBuilder().apply(builderAction),
                        event.mods,
                    )
                }.registryProvider
            val fileHelper: ExistingFileHelper = event.existingFileHelper
            return Pair(
                HTRootDataGenerator(generator, event::includeServer, registries, fileHelper),
                HTRootDataGenerator(generator, event::includeClient, registries, fileHelper),
            )
        }
    }

    /**
     * 指定した[id]でデータパックを作成します。
     * @return データパック向けの[HTDataPackGenerator]
     */
    fun createDataPackGenerator(id: ResourceLocation): HTDataPackGenerator = HTDataPackGenerator(
        generator.getBuiltinDatapack(doRun.asBoolean, id.namespace, id.path),
        registries,
        fileHelper,
    )

    override fun <DATA : DataProvider> addProvider(factory: DataProvider.Factory<DATA>): DATA =
        generator.addProvider(doRun.asBoolean, factory)

    override fun <DATA : DataProvider> addProvider(factory: GatherDataEvent.DataProviderFromOutputLookup<DATA>): DATA =
        addProvider { output: PackOutput -> factory.create(output, registries) }

    override fun <DATA : DataProvider> addProvider(factory: HTDataGenerator.Factory<DATA>): DATA =
        addProvider { output: PackOutput -> factory.create(HTDataGenContext(output, registries, fileHelper)) }
}
