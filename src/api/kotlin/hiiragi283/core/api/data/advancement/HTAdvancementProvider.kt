package hiiragi283.core.api.data.advancement

import hiiragi283.core.api.data.allOf
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
        val advancements: MutableMap<ResourceLocation, WithConditions<Advancement>> = hashMapOf()
        this.registries = registries
        this.exporter = HTAdvancementExporter { id: ResourceLocation, advancement: Advancement, conditions: List<ICondition> -> check(advancements.put(id, WithConditions(conditions, advancement)) == null) { "Duplicate advancement $id" } }
        buildAdvancements()
        advancements
            .map { (id: ResourceLocation, value: WithConditions<Advancement>) -> DataProvider.saveStable(output, registries, Advancement.CONDITIONAL_CODEC, Optional.of(value), pathProvider.json(id)) }
            .allOf()
    }

    /**
     * 進捗を生成します。
     */
    protected abstract fun buildAdvancements()
}
