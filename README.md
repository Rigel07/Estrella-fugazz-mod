# Estrella Mágica Fugaz (Forge 1.20.1)

Estrellas fugaces de colores (azul, rosa, verde, amarilla) cruzan el cielo nocturno.
Muy pocas llegan a caer: al hacerlo provocan una explosión pequeña y dejan fragmentos
y, con cierta probabilidad según el color, un objeto único.

## Compilar en GitHub
1. Sube TODO el contenido de esta carpeta (incluida `.github`) a un repositorio.
2. Pestaña **Actions** -> "Build Estrella Magica Fugaz" (se lanza solo al hacer push).
3. Al terminar, abre la ejecución -> **Artifacts** -> `estrella-magica-fugaz-jar` (zip con tu .jar).

## Ajustes
`event/StarSpawner.java`: CHECK_INTERVAL, SPAWN_CHANCE, IMPACT_CHANCE.
`StarColor.java`: probabilidad del objeto único por color.
`entity/ShootingStarEntity.java`: EXPLOSION_RADIUS y cantidad de fragmentos.

## Recetas con los fragmentos
Crea JSON en `src/main/resources/data/estrella_magica_fugaz/recipes/`. Ejemplo (linterna):
```json
{
  "type": "minecraft:crafting_shaped",
  "pattern": [" F ", "FTF", " F "],
  "key": {
    "F": { "item": "estrella_magica_fugaz:yellow_star_fragment" },
    "T": { "item": "minecraft:torch" }
  },
  "result": { "item": "minecraft:lantern", "count": 4 }
}
```
