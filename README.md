# Hiiragi Core

[![GitHub License](https://img.shields.io/github/license/hiiragi283/hiiragi-core?style=for-the-badge&color=333333&logo=github)](https://github.com/Hiiragi283/hiiragi-core)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1432470?style=flat-square&color=cc6600&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/hiiragi-core)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/hiiragi-core?style=flat-square&color=339966&logo=modrinth)](https://modrinth.com/mod/hiiragi-core)

## About

- A library mod for Hiiragi's mods
- Supported mod loader: NeoForge
- Supported mc version: MC1.21.1

## Feature

- Material Blocks/Items
  - Vanilla
    - Fuels: Coal, Charcoal
    - Minerals: (Redstone), (Glowstone), Calcite
    - Gems: Lapis, Quartz, Amethyst, Diamond, Emerald, Echo, Prismarine
    - Pearls: Ender
    - Metals: Iron, Copper, Gold
    - Alloys: Netherite
    - Crops: Wheat,
    - Others: Wood, Glass, Stone, Obsidian
  - Common
    - Fuels: Coal Coke
    - Minerals: Bauxite, Salt, Saltpeter, Sulfur
    - Gems: Cinnabar, (Fluorite), (Peridot), (Ruby), (Sapphire)
    - Metals
      - 2nd Period: (Lithium), (Beryllium)
      - 3rd Period: (Sodium), (Magnesium), Aluminum, (Silicon)
      - 4th Period: (Titanium), (Vanadium), (Chromium), (Manganese), (Cobalt), (Nickel), Zinc
      - 5th Period: (Molybdenum), (Palladium), (Silver), Tin, (Antimony)
      - 6th Period: (Tungsten), (Osmium), Iridium, (Platinum), (Lead)
      - 7th Period: (Uranium), (Plutonium)
    - Alloys: Steel, (Invar), (Electrum), Brass, (Constantan), Bronze, (Signalum), (Lumium), (Enderium)
    - Others: Ash, Carbon, Plastic, Rubber
  - Original
    - Gems: Azure, Crimson Crystal, Warped Crystal
    - Pearls: Eldritch
    - Alloys: Azure Steel

- Blocks
  - Warped Wart: Clears one bad effect randomly when eaten.
- Items
  - Compressed Sawdust: Recycles Sawdust into Charcoal
  - Trader's Catalog: Opens trading menu of Wandering Trader
- End-Game Items
  - Almighty Pickaxe: A mining tool suitable for ALL BLOCKS
  - Ambrosia: NON-CONSUMABLE Food
  - Eternal Smithing Template: Make any equipment UNBREAKABLE
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
