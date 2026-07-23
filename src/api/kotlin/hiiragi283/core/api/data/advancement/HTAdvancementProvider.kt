package hiiragi283.core.api.data.advancement

import java.util.Optional
import java.util.concurrent.CompletableFuture
import net.minecraft.advancements.Advancement
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions

abstract class HTAdvancementProvider(packOutput: PackOutput, private val future: CompletableFuture<HolderLookup.Provider>, protected val modId: String) : DataProvider {
    private val pathProvider: PackOutput.PathProvider = packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT)

    /**
     * レジストリへのアクセス
     *
     * [buildAdvancements]の前に初期化されます。
     */
    protected lateinit var registries: HolderLookup.Provider
        private set

    /**
     * 進捗の出力先
     *
     * [buildAdvancements]の前に初期化されます。
     */
    protected lateinit var exporter: HTAdvancementExporter
        private set

    final override fun run(output: CachedOutput): CompletableFuture<*> = future.thenCompose { registries: HolderLookup.Provider ->
        val advancements: MutableSet<ResourceLocation> = hashSetOf()
        val tasks: MutableList<CompletableFuture<*>> = mutableListOf()
        this.registries = registries
        this.exporter = HTAdvancementExporter { id: ResourceLocation, advancement: Advancement, conditions: List<ICondition> ->
            check(advancements.add(id)) { "Duplicate advancement $id" }
            tasks += DataProvider.saveStable(output, registries, Advancement.CONDITIONAL_CODEC, Optional.of(WithConditions(conditions, advancement)), pathProvider.json(id))
        }
        buildAdvancements()
        CompletableFuture.allOf(*tasks.toTypedArray())
    }

    /**
     * 進捗を生成します。
     */
    protected abstract fun buildAdvancements()
}
