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
    "subject": "projectile",
    "spells": [
      {
        "action": "explosion",
        "descriptors": [
          "purple",
          "strong",
          "reagent.scriptor.amethyst"
        ]
      }
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

* `subject` defines the subject to attach to this spell.
* `spells` contains all the effect this spell applies to a target.
  * `action` defines the action to attach to this spell.
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
    "subject": "projectile",
    "spells": [
      {
        "action": "explosion",
        "descriptors": [
          "purple",
          "strong",
          "reagent.scriptor.amethyst"
        ]
      }
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

## Token Generators

A generator is used to create the words that populate your world.
By default, there are two generator types and two generators available. 
The default generator types are `static_token`, which is used by 
`scriptor:static_generator`, and `mixed_groups`, which is used by 
`scriptor:mixed_groups`.

Here are examples of generators for each default generator type.
(You can also view the default generators 
[here](../../common/src/main/resources/data/scriptor/scriptor/generators))

Token Generators are defined in `scriptor/generators` in your data pack.

### Static Token

The Static Token generator type (`static_token`) is the simplest, 
but requires configuration for each individual word which uses it.

As such, this generator **cannot be used as a default generator**,
since it requires configuration for each individual word. 

Below is an example generator, copied from the default one:

```json
{
  "generator": "static_token",
  "default": false,
  "parameters": {
    "collisionStrategy": "FALLBACK"
  }
}
```

`default` is optional, and will default to `false` if not defined.
With `static_token` as the generator, this **cannot** be true.

There is only one generator-specific parameter for `static_token`
generators, `collisionStrategy`, which determines what happens when
a word is generated that already exists.
There are 3 valid strategies:

* `FALLBACK` - When a word is generated that already exists,
use the default generator.
* `FAIL` - Useful for modpack dev, this causes the data pack to 
fail to load and output an error message if a collision is encountered.
* `SHORTEN` - A requested strategy, attempts to shorten a word until
a unique word is found on collision, failing if no valid words are found.
This can be useful when mixing `static_token` with another default.

### Mixed Groups

The Mixed Group generator type (`mixed_groups`) generator is a 
sensible default generator which combines strings from multiple
groups into unique tokens.

This generator can be used as a default generator, and an instance
is shipped as default.

Below is an example generator:

```json
{
  "generator": "mixed_groups",
  "default": true,
  "parameters": {
    "minTokens": 3,
    "maxTokens": 11,
    "maxConsecutiveGroups": 1,
    "groups": [
      {
        "weight": 1,
        "tokens": [
          "b",
          "c"
        ]
      },
      {
        "weight": 1,
        "tokens": [
          "a",
          "e"
        ]
      }
    ]
  }
}
```

This generator can produce tokens by mixing strings from each group.
This example can produce tokens such as `bace`, `caca`, and `ceba`.

The generator parameter `minTokens` defines the minimum number of 
strings that must be combined to create a new word.
This **must** be less than or equal to `maxTokens`.

The generator parameter `maxTokens` is `minTokens`'s counterpart,
and defines the max number of strings to create a new word.
This **must** be greater than or equal to `minTokens`.

`maxConsecutiveGroups` defines how many strings from one group
can appear in a row. 
In this example, since it is one, strings like `bb`, `bc`, or `ae`
will never appear in a generated word.
This **must** be greater than or equal to `1`.

`groups` defines each group of tokens which can be used to generate
a word. 
This is a list of objects, each of which **must** contain the 
following parameters:

* `weight` is the chance that this group will be chosen next in
sequence. 
This is as a proportion of the combined weight of all groups.
* `tokens` is a list of strings in this group, which can be 
combined to create new words.

## Generator Bindings

A generator binding is used to define how a *specific word* is 
generated.
You can use such a binding to either specify parameters for a
specific word, or to use a non-default generator.

Each binding file can contain numerous bindings for the same 
generator.

Generator Bindings are located in `scriptor/bindings` in your
data pack.

### Static Token

Bindings are required for each word which uses a Static Token 
Generator.

Below is an example of a static token binding which ships with
Scriptor.

```json
{
  "generator": "scriptor:static_generator",
  "bindings": [
    {
      "word": "color.scriptor.enby",
      "parameters": {
        "token": "biur"
      }
    }
  ]
}
```

Static Token generators only have one parameter, `token`, which 
is required.
This parameter is used to determine the token which will be used 
for this word, straight up and down.
In this case, it is an Easter egg that sets the word for one of the
dynamic color sets to "biur", a username I often use that I don't 
think stands out among other generated words.

### Mixed Groups

Bindings are optional for words using a Mixed Groups generator.

Here is an example of a mixed group binding which shipped with
Scriptor.

```json
{
  "generator": "scriptor:mixed_groups",
  "bindings": [
    {
      "word": "other:and",
      "parameters": {
        "minTokens": 2,
        "maxTokens": 2
      }
    }
  ]
}
```

This uses the generator `mixed_groups` defined within the
`scriptor` data pack, and only rebinds the special word
`other:and`. 

Mixed Group bindings only support 2 parameters, which allow
the minimum and maximum tokens used to be modified.
In this case, this special word is expected to be shorter
than most words, so this binding is used to shorten it.