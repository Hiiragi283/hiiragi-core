package hiiragi283.core.util

import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.plugin.HTPlugin
import net.neoforged.fml.ModList
import net.neoforged.neoforgespi.language.ModFileScanData
import org.objectweb.asm.Type
import java.lang.reflect.Constructor

data object HTPluginLoader {
    @JvmField
    val ANNOTATION_TYPE: Type = Type.getType(HTPlugin::class.java)

    @JvmStatic
    inline fun <reified T : Any> collectPlugins(): Sequence<T> = ModList
        .get()
        .allScanData
        .asSequence()
        .flatMap(ModFileScanData::getAnnotations)
        .filter { it.annotationType == ANNOTATION_TYPE }
        .map(ModFileScanData.AnnotationData::clazz)
        .map(Type::getClassName)
        .mapNotNull { className: String ->
            runCatching {
                val clazz: Class<*> = Class.forName(className)
                val asmClazz: Class<out T> = clazz.asSubclass(T::class.java)
                // Try to load from singleton instance
                asmClazz.kotlin.objectInstance ?: run {
                    // Try to load from constructor with no parameter
                    val constructors: Array<out Constructor<*>> = asmClazz.constructors
                    check(constructors.size == 1) { "Plugin class must have exactly 1 public constructor, found ${constructors.size}" }
                    constructors.first().newInstance()as T
                }
            }.onFailure { throwable: Throwable ->
                HiiragiCoreAPI.LOGGER.error("Failed to construct {}", className, throwable)
            }.getOrNull()
        }
}
