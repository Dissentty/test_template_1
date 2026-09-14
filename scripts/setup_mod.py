#!/usr/bin/env python3
from __future__ import annotations
import argparse, re
from pathlib import Path
ROOT=Path(__file__).resolve().parents[1]
PROPS=ROOT/'gradle.properties'

def read_props():
    out={}
    for line in PROPS.read_text(encoding='utf-8').splitlines():
        if '=' in line and not line.lstrip().startswith('#'):
            k,v=line.split('=',1); out[k.strip()]=v.strip()
    return out

def set_props(updates):
    text=PROPS.read_text(encoding='utf-8')
    for k,v in updates.items():
        text,n=re.subn(rf'(?m)^{re.escape(k)}=.*$', f'{k}={v}', text, count=1)
        if n!=1: raise SystemExit(f'Property not found: {k}')
    PROPS.write_text(text,encoding='utf-8')

def move_package(old,new):
    oldp=Path(*old.split('.')); newp=Path(*new.split('.'))
    for module in ('Common','Fabric','Forge','NeoForge'):
        src=ROOT/module/'src'
        if not src.exists(): continue
        for side in src.iterdir():
            jr=side/'java'; od=jr/oldp
            if not od.exists(): continue
            nd=jr/newp; nd.parent.mkdir(parents=True,exist_ok=True)
            if nd.exists(): raise SystemExit(f'Target exists: {nd}')
            od.rename(nd)
            cur=od.parent
            while cur!=jr and cur.exists():
                try: cur.rmdir()
                except OSError: break
                cur=cur.parent
    for f in ROOT.rglob('*.java'):
        f.write_text(f.read_text(encoding='utf-8').replace(old,new),encoding='utf-8')

def rename_resources(old,new):
    a=ROOT/'Common/src/main/resources/assets'/old
    b=ROOT/'Common/src/main/resources/assets'/new
    if a.exists() and a!=b:
        if b.exists(): raise SystemExit(f'Target exists: {b}')
        a.rename(b)

    old_mixin=ROOT/'Common/src/main/resources'/f'{old}.mixins.json'
    new_mixin=ROOT/'Common/src/main/resources'/f'{new}.mixins.json'
    if old_mixin.exists() and old_mixin!=new_mixin:
        if new_mixin.exists(): raise SystemExit(f'Target exists: {new_mixin}')
        old_mixin.rename(new_mixin)

    resources=ROOT/'Common/src/main/resources'
    for f in resources.rglob('*'):
        if f.is_file() and f.suffix in {'.json','.mcmeta','.txt'}:
            f.write_text(f.read_text(encoding='utf-8').replace(old,new),encoding='utf-8')

def main():
    ap=argparse.ArgumentParser(description='Rename the Minecraft universal mod template.')
    ap.add_argument('--id',required=True,dest='mod_id')
    ap.add_argument('--name',required=True)
    ap.add_argument('--package',required=True,dest='base_package')
    ap.add_argument('--version',default='1.0.0')
    ap.add_argument('--authors',default='YourName')
    ap.add_argument('--description',default='A Minecraft mod.')
    ap.add_argument('--license',default='MIT',dest='license_name')
    a=ap.parse_args()
    if not re.fullmatch(r'[a-z][a-z0-9_]{1,63}',a.mod_id): raise SystemExit('Invalid mod id')
    if not re.fullmatch(r'[A-Za-z_][A-Za-z0-9_]*(\.[A-Za-z_][A-Za-z0-9_]*)+',a.base_package): raise SystemExit('Invalid Java package')
    p=read_props(); oldid=p['mod_id']; oldpkg=p['base_package']
    if oldpkg!=a.base_package: move_package(oldpkg,a.base_package)
    if oldid!=a.mod_id: rename_resources(oldid,a.mod_id)
    group='.'.join(a.base_package.split('.')[:-1])
    set_props({'mod_id':a.mod_id,'mod_name':a.name,'mod_version':a.version,'mod_description':a.description,'mod_authors':a.authors,'mod_license':a.license_name,'maven_group':group,'base_package':a.base_package})
    print(f'Renamed template -> {a.mod_id} / {a.base_package}')
    print('Next: ./gradlew universalJar')
if __name__=='__main__': main()
