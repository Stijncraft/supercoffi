# Ontwikkelen

Vereist: alleen Git. Gradle downloadt zelf JDK 25.

```powershell
.\gradlew.bat runClient   # game starten om te testen
.\gradlew.bat build       # jar bouwen in build\libs
```

## Werkwijze
1. Maak een branch per wijziging, bijvoorbeeld `kleur-zwaard`.
2. Test met `runClient` in een creative-testwereld.
3. Voeg samen met `main` als het werkt.
4. Voor een release: verhoog `mod_version` in `gradle.properties`, bouw de jar en tag de commit (`v1.1.0`).

## Structuur
- `src/main/java/net/mcreator/supercoffi`: code (items, blokken, entiteit, portals)
- `src/main/resources/assets/supercoffi`: modellen, textures en vertalingen
- `src/main/resources/data/supercoffi`: recepten, dimensies en loot tables
- `src/main/templates/META-INF/neoforge.mods.toml`: modinformatie
- `gradle.properties`: modversie en Minecraft/NeoForge-versie

## Herkomst
De code is gereconstrueerd uit de gebouwde jar (decompilatie) en opgeschoond tot een NeoForge-MDK-project. Details kunnen dus afwijken van de eerste versie.

`supercoffi-1.0.0-neoforge-26.1.2.jar` in de root is de oorspronkelijke build.

Gebaseerd op de NeoForge MDK-template (zie `TEMPLATE_LICENSE.txt`).
