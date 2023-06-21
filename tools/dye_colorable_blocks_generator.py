#usr/bin/env python
template = """public void register$2() {
    $1.add(Blocks.BLACK_$1, DyeColor.BLACK);
    $1.add(Blocks.WHITE_$1, DyeColor.WHITE);
    $1.add(Blocks.BLUE_$1, DyeColor.BLUE);
    $1.add(Blocks.BROWN_$1, DyeColor.BROWN);
    $1.add(Blocks.CYAN_$1, DyeColor.CYAN);
    $1.add(Blocks.GRAY_$1, DyeColor.GRAY);
    $1.add(Blocks.GREEN_$1, DyeColor.GREEN);
    $1.add(Blocks.LIGHT_BLUE_$1, DyeColor.LIGHT_BLUE);
    $1.add(Blocks.LIGHT_GRAY_$1, DyeColor.LIGHT_GRAY);
    $1.add(Blocks.LIME_$1, DyeColor.LIME);
    $1.add(Blocks.MAGENTA_$1, DyeColor.MAGENTA);
    $1.add(Blocks.ORANGE_$1, DyeColor.ORANGE);
    $1.add(Blocks.PINK_$1, DyeColor.PINK);
    $1.add(Blocks.PURPLE_$1, DyeColor.PURPLE);
    $1.add(Blocks.RED_$1, DyeColor.RED);
    $1.add(Blocks.YELLOW_$1, DyeColor.YELLOW);
    $1.register();
  }"""

def generateTemplate(string: str):
  print(template.replace("$1", string.upper()).replace("$2", string.replace("_", " ").title().replace(" ", "")))
  print()

generateTemplate("wool")
generateTemplate("terracotta")
generateTemplate("glazed_terracotta")
generateTemplate("bed")
generateTemplate("candle")
generateTemplate("carpet")
generateTemplate("concrete_powder")
generateTemplate("shulker_box")
generateTemplate("stained_glass")
generateTemplate("stained_glass_pane")
generateTemplate("concrete")
