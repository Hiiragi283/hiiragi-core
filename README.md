# Hiiragi Core

[![GitHub License](https://img.shields.io/github/license/hiiragi283/hiiragi-core?style=for-the-badge&color=333333&logo=github)](https://github.com/Hiiragi283/hiiragi-core)
![CurseForge Downloads](https://img.shields.io/curseforge/dt/0?style=flat-square&color=cc6600&logo=curseforge)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/hiiragi-core?style=flat-square&color=339966&logo=modrinth)](https://modrinth.com/mod/hiiragi-core)

## About

- A library mod for Hiiragi's mods
- Supported mod loader: NeoForge
- Supported mc version: MC1.21.1

## Feature

- Common Material Blocks/Items
  - Fuels: Coal, Charcoal, Coal Coke, Carbide,
  - Minerals: Redstone, Glowstone, Cinnabar, Salt, Saltpeter, Sulfur
  - Gems: Lapis, Quartz, Amethyst, Diamond, Emerald, Echo Shard
  - Pearls: Ender Pearl
  - Metals: Copper, Iron, Gold, Silver
  - Alloys: Netherite, Steel
  - Plates: Plastic, Rubber
  - Misc: Wood, Obsidian

- Blocks
  - Warped Wart: Clears one bad effect randomly when eaten.
- Items
  - Compressed Sawdust: Recycles Sawdust into Charcoal
  - Trader's Catalog: Opens trading menu of Wandering Trader
  - Ambrosia: NON-CONSUMABLE FOOD
  - Eternal Ticket: Make any tool UNBREAKABLE
- Recipe Types
  - Charging: Fired when Lightning Strike hit on item entity
  - Anvil Crushing: Fired when Anvil fallen on item entity
  - Exploding: Fired when explosion hit on item entity

## Maven

### Groovy

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.hiiragi283/hiiragi-core?style=for-the-badge)](https://search.maven.org/artifact/io.github.hiiragi283/hiiragi-core)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "io.github.hiiragi283:hiiragi-core:VERSION"
}
```

### Kotlin

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.hiiragi283:hiiragi-core:VERSION")
}
```
