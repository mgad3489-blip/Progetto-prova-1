# Curated Asset Structure

This version uses folders based on the actual extracted art.

## Root
- Assets/
- Sprite-Sheets/
- Preview/
- Docs/

## Asset folders
- Assets/Backgrounds/
- Assets/Characters/Heroes/
- Assets/Enemies/Orcs/
- Assets/Structures/Towers/
- Assets/Tiles/Ground/
- Assets/Tiles/Paths/
- Assets/Props/Bridges/
- Assets/Props/Fences/
- Assets/Props/Nature/
- Assets/Props/Signs/
- Assets/Resources/
- Assets/UI/Banners/
- Assets/UI/Buttons/
- Assets/UI/Icons/

## Naming rules
- lowercase_with_underscores
- use content names like `magic_crystal_tower.png`
- avoid generic names like `gen_tower_001.png`

## Sprite sheets
- Sprite-Sheets/master_sprite_sheet.png
- Sprite-Sheets/characters_sheet.png
- Sprite-Sheets/environment_sheet.png
- Sprite-Sheets/ui_sheet.png

## Metadata
Track each file in `PACK_MANIFEST.csv` with:
- category
- subcategory
- filename
- resolution
- pivot
- short description
