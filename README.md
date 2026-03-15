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
    - Minerals: (Redstone), (Glowstone)
    - Gems: Lapis, Quartz, Amethyst, Diamond, Emerald, Echo, Prismarine
    - Pearls: Ender
    - Metals: Iron, Copper, Gold
    - Alloys: Netherite
    - Others: Wood, Glass, (Stone), Obsidian, (Blaze), (Breeze), Brick, Nether Brick
  - Common
    - Fuels: Coal Coke
    - Minerals: Salt, Saltpeter, Bauxite, Sulfur, Cinnabar, Galena
    - Gems: (Fluorite), (Peridot), (Ruby), (Sapphire)
    - Metals
      - 2nd Period: (Lithium), (Beryllium)
      - 3rd Period: (Sodium), (Magnesium), Aluminum, (Silicon)
      - 4th Period: (Titanium), (Vanadium), (Chromium), (Manganese), (Cobalt), (Nickel), Zinc
      - 5th Period: (Molybdenum), Ruthenium, Rhodium, Palladium, Silver, Tin, (Antimony)
      - 6th Period: (Tungsten), Osmium, Iridium, Platinum, Lead
      - 7th Period: (Uranium), (Plutonium)
    - Alloys: Steel, (Invar), Brass, (Constantan), Bronze, (Electrum), (Signalum), (Lumium), (Enderium)
    - Others: Ash, Carbon, Plastic, Rubber
  - Original
    - Gems: Azure, Crimson Crystal, Warped Crystal
    - Pearls: Eldritch
    - Alloys: Azure Steel
  - (`Material`): No contents added by Hiiragi Core

- Blocks
  - Latex Cauldron: Drops Raw Rubber randomly
  - Tree Tap: Extracts Latex from attached logs to below cauldron
  - Warped Wart: Clears one bad effect randomly when eaten.
- Items
  - Bamboo Charcoal: A substitute for Charcoal made from Bamboo
  - Bomb: Throwable explosive
  - Particle Board: Recycled Planks made from Sawdust
  - Synthetic Feather/Fiber/Leather: A substitute for Feather/String/Leather made from Plastic
  - Trader's Catalog: Opens trading menu of Wandering Trader
  - Experience Tome: Store your experience or release it
- End-Game Items
  - Almighty Pickaxe: A mining tool suitable for ALL BLOCKS
  - Ambrosia: NON-CONSUMABLE Food
  - Eternal Smithing Template: Make any equipment UNBREAKABLE
- Recipe Types
  - Charging: Fired when Lightning Strike hit on item entity
  - Anvil Crushing: Fired when Anvil fallen on item entity
  - Exploding: Fired when explosion hit on item entity

## Maven

- Use [Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven)

### Groovy

![Modrinth Version](https://img.shields.io/modrinth/v/hiiragi-core?style=for-the-badge)

```groovy
repositories {
    maven {
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    implementation "maven.modrinth:hiiragi-core:VERSION"
}
```

### Kotlin

```kotlin
repositories {
    maven(url = "https://api.modrinth.com/maven") {
        content { 
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    implementation("maven.modrinth:hiiragi-core:VERSION")
}
```
