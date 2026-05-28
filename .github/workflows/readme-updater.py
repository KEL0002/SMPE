import re
import pathlib


readme = pathlib.Path("readme.md").read_text()

def replace_details_block(name: str, new_content: str):
    pattern = (
        rf"(<details>\s*<summary>{re.escape(name)}</summary>\s*```YAML\n)"
        rf".*?"
        rf"(```\s*</details>)"
    )
    replacement = rf"\g<1>{new_content}\n\2"
    result, count = re.subn(pattern, replacement, readme, flags=re.DOTALL)
    if count == 0:
        raise ValueError(f"Could not find details block for {name}")
    return result

config = pathlib.Path("src/main/resources/config.yml").read_text().rstrip("\n")
lang = pathlib.Path("src/main/resources/lang.yml").read_text().rstrip("\n")

readme = replace_details_block("Config", config)
readme = replace_details_block("Language Config", lang)

pathlib.Path("readme.md").write_text(readme)
print("README updated!")