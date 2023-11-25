#usr/bin/env python

from os import mkdir

colors = [
  "black",
  "white",
  "blue",
  "brown",
  "cyan",
  "gray",
  "green",
  "light_blue",
  "light_gray",
  "lime",
  "magenta",
  "orange",
  "pink",
  "purple",
  "red",
  "yellow"
]

template = (
  "public void register$2() {\n"
  + "\n".join([f"    $1.add(ScriptorBlocks.{c.upper()}_$1.get(), DyeColor.{c.upper});" for c in colors])
  + "    $1.register();\n  }"
)

model_template = """{
  "parent": "minecraft:block/cube_all",
  "render_type": "translucent",
  "textures": {
    "all": "scriptor:block/$1_$2"
  }
}"""

blockstate_template = """{
  "variants": {
    "": {
      "model": "scriptor:block/$1_$2"
    }
  }
}"""

def generateTemplate(string: str):
  print(template.replace("$1", string.upper()).replace("$2", string.replace("_", " ").title().replace(" ", "")))
  print()
  for c in colors:
    with open(f"out/models/block/{c}_{string}.json", "w") as f:
      f.write(model_template.replace("$1", c).replace("$2", string))
    with open(f"out/blockstates/{c}_{string}.json", "w") as f:
      f.write(blockstate_template.replace("$1", c).replace("$2", string))
  

mkdir("out")
mkdir("out/models")
mkdir("out/models/block")
mkdir("out/blockstates")
generateTemplate("magic_block")

