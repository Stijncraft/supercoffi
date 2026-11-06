# supercoffi

Minecraft-mod "Super Coffi" voor **Minecraft 26.1.2** met **NeoForge 26.1.2** (Java 25).

Bevat o.a. zwaarden, pantsers, een pikhouweel, twee portals (Diamont en Irond), het blok Starbuks en de mob Soulskeleton.

## Spelen
- Installeer [NeoForge 26.1.2](https://neoforged.net) (via een launcher of de installer).
- Zet `supercoffi-1.0.0.jar` in de map `mods` van het NeoForge-profiel.

`supercoffi-1.0.0-neoforge-26.1.2.jar` in de root is de oorspronkelijke build. De bron staat nu in `src/`.

## Ontwikkelen
Vereist: alleen Git. Gradle downloadt zelf JDK 25.

```powershell
.\gradlew.bat runClient   # game starten om te testen
.\gradlew.bat build       # jar bouwen in build\libs
```

Werkwijze: maak een branch per wijziging (bijv. `kleur-zwaard`), test met `runClient`, voeg daarna samen met `main`.

## Herkomst
De code is gereconstrueerd uit de gebouwde jar (decompilatie) en opgeschoond tot een NeoForge-MDK-project. Het is dus mogelijk dat details afwijken van de eerste versie.

Gebaseerd op de NeoForge MDK-template (zie `TEMPLATE_LICENSE.txt`).
