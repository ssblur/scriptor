# Datapacks
*All the datapack-driven features you can play with in Scriptor*

## Spell Tomes

Tomes are default generated Spellbooks, and are the way that spell
components are discovered in Scriptor.
These can be created or modified using datapacks.

Tomes are located in `data/[namespace]/tomes/[tome].json` in a 
datapack.
The following is an example of 
`data/[namespace]/tomes/amethyst_explosion.json`, a default Tome 
that ships with Scriptor.

```json
{
  "name": "Gempact",
  "author": "Snark",
  "tier": 3,
  "spell": {
    "action": "explosion",
    "subject": "projectile",
    "descriptors": [
      "purple",
      "strong",
      "reagent.scriptor.amethyst"
    ]
  }
}
```

`name` is the plain text name of the corresponding Spellbook. 
This is not localized.

`author` is the plain text name to show in the author field under
the title. 
This is not localized.

`tier` is the tier of Ancient Tome this Spellbook will generate from.
The base mod supports 1-4.

`spell` defines the spell to be generated. 
This format is subject to change.

* `action` defines the action to attach to this spell.
* `subject` defines the subject to attach to this spell.
* `descriptors` is a list of descriptors attached to this spell. 
    Any descriptors that support duplication can be included 
    multiple times.

In order to disable a default Tome, you can add a copy of the
default Tome and add the value `true` to the `disabled` key,
as follows.

```json
{
  "disabled": true,
  "name": "Gempact",
  "author": "Snark",
  "tier": 3,
  "spell": {
    "action": "explosion",
    "subject": "projectile",
    "descriptors": [
      "purple",
      "strong",
      "reagent.scriptor.amethyst"
    ]
  }
}
```

A spell component can be disabled by disabling all Tomes which use
it.

## Reagents

Reagents are special spell components that consume items in order to 
reduce the cost of a spell. 
These can be created or modified using datapacks.

Reagents are located in `data/[namespace]/reagents/[reagent].json` in a 
datapack.
The following is an example of `data/scriptor/reagents/amethyst.json`, a 
default reagent that ships with Scriptor.

```json
{
  "item": "minecraft:amethyst_shard",
  "cost": 100
}
```

In this example, the `item` key is the name of the Item to consume.

`cost` is the value the spell should be reduced by.

In order to disable a default Reagent, you can add a copy of the 
default Reagent and add the value `true` to the `disabled` key, 
as follows.

```json
{
  "item": "minecraft:amethyst_shard",
  "cost": 100,
  "disabled": true
}
```

Reagents can be used in Tomes by referencing `reagent.[namespace].[reagent]`
in the Tome's `spell.descriptors` field, as seen [above](#spell-tomes).