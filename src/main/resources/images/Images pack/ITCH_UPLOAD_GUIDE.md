# Fantasy Tower Defense Starter Pack - itch.io Upload Guide

This guide helps you publish this pack on itch.io quickly.

## What this pack includes
- `Assets/` with 30 curated PNG files
- `Sprite-Sheets/` with 4 preview sheets
- `Preview/preview_sheet.png`
- `README.txt`
- `LICENSE.txt`
- `PRODUCT_PAGE_TEXT.txt`
- release notes and manifest docs

## Important listing note
This version is a curated starter pack exported from the preview source image. It includes named PNG assets and overview sprite sheets, but it is not yet a full animation-ready production pack.

## Recommended itch.io settings
- Project type: `Assets`
- Classification: `Game assets`
- Pricing: Your choice (`$0+` works well for early traction)
- Platforms: Leave platform binaries unchecked unless you also upload a playable demo
- Visibility: `Draft` first, then `Public` when finalized

## Suggested tags
`2D`, `Fantasy`, `Tower Defense`, `Sprites`, `Unity`, `Tileset`, `UI`, `Top-Down`

## Upload steps
1. Run `build_itch_package.ps1` from this folder.
2. Find output zip in `Itch-Release/`.
3. On itch.io, create a new project and upload that zip as the downloadable file.
4. Paste/edit text from `PRODUCT_PAGE_TEXT.txt`.
5. Add screenshots from `Sprite-Sheets/` and the main `Preview/preview_sheet.png`.
6. Add AI disclosure in the description.

## AI disclosure example
This asset pack includes AI-assisted artwork that was curated, organized, and packaged by the creator.

## Next upgrade path
For a stronger commercial release:
- add more unique towers, enemies, and terrain tiles
- export transparent variants and animation strips
- replace preview-derived assets with dedicated production files
