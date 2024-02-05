from os import walk
from os.path import join

from json import load, dump

for root, dirs, files in walk("."):
    for file in files:
        if file[-5:] == ".json":
            name = ""
            json = None
            with open(join(root, file), "r") as f:
                json = load(f)
                name = json["name"]
                print(f'"tome.scriptor.{file[:-5]}": "{name}",')
            if json:
                with open(join(root, file), "w") as f:
                    json["name"] = f"tome.scriptor.{file[:-5]}"
                    dump(json, f, indent=2)