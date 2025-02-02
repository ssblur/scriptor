# Casting Crystal

![Casting Crystal Icon](../../common/src/main/resources/assets/scriptor/textures/item/caster_crystal.png)
*A Casting Crystal*

Casting crystals can be crafted by surrounding an Amethyst Shard with redstone,
and then crafted with a tag to change the target type.

Casting crystals are used to change the "self", or focus, of a Casting Lectern.
This can be used, for example, to change the target of a self-targeting
inflame spell, or the inventory an item targeted by a "bring" spell is
teleported into.

Targets can be reset by placing a crystal by itself in a crafting grid.

Casting Crystals are limited to a range of 16 blocks.

## Coordinate-Focused Crystal

![Coordinate-Focused Crystal Icon](../../common/src/main/resources/assets/scriptor/textures/item/tagged_caster_crystal.png)
*A Coordinate-Focused Casting Crystal*

Coordinate-Focused Crystals can be crafted using a Coordinate Tag.

These can hold up to 4 sets of coordinates and directions,
which will be rotated between.
New data points can be added by right-clicking. The face of the block
clicked and the position targeted will be recorded, and treated as if
the focus were the target of a Touch spell used on this face of a
block.

## Player-Focused Crystal

![Player-Focused Crystal Icon](../../common/src/main/resources/assets/scriptor/textures/item/tagged_caster_crystal.png)
*A Player-Focused Casting Crystal*

Coordinate-Focused Crystals can be crafted using a Player Tag.

Right clicking with one will target the player using the crystal.
The spell will treat the focus as if the player in question were casting
the spell while they are in range.