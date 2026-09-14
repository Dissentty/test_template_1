#!/usr/bin/env python3
from __future__ import annotations
import json, re
from pathlib import Path
ROOT = Path(__file__).resolve().parents[1]

def props():
    out={}
    for line in (ROOT/'gradle.properties').read_text(encoding='utf-8').splitlines():
        if '=' in line and not line.lstrip().startswith('#'):
            k,v=line.split('=',1); out[k.strip()]=v.strip()
    return out

def expand(text, values):
    return re.sub(r'\$\{([^}]+)\}', lambda m: values[m.group(1)], text)

def main():
    p=props(); errors=[]
    base=Path(*p['base_package'].split('.'))
    if not (ROOT/'Common/src/main/java'/base).exists(): errors.append('base_package/source tree mismatch')
    for src in (ROOT/'Common/src/main/java', ROOT/'Common/src/server/java'):
        for f in src.rglob('*.java'):
            if 'net.minecraft.client' in f.read_text(encoding='utf-8'):
                errors.append(f'client reference outside client source set: {f.relative_to(ROOT)}')
    json_values={k:(json.dumps(v)[1:-1] if isinstance(v,str) else v) for k,v in p.items()}
    json_files=(
        'Fabric/src/main/resources/fabric.mod.json',
        f"Common/src/main/resources/{p['mod_id']}.mixins.json",
    )
    for rel in json_files:
        try: json.loads(expand((ROOT/rel).read_text(encoding='utf-8'),json_values))
        except Exception as exc: errors.append(f'{rel}: {exc}')
    for rel in ('Common/build.gradle','Fabric/build.gradle','Forge/build.gradle','NeoForge/build.gradle','gradle/universal.gradle','Forge/src/main/resources/META-INF/mods.toml','NeoForge/src/main/resources/META-INF/neoforge.mods.toml'):
        if not (ROOT/rel).exists(): errors.append(f'missing {rel}')
    if errors:
        print('Static verification FAILED:')
        for e in errors: print(' -',e)
        raise SystemExit(1)
    print('Static template verification passed.')
    print('Full Gradle compilation still requires loader/Minecraft dependency resolution.')
if __name__=='__main__': main()
