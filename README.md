# Minecraft 26.2 Universal Mod Template

Один исходный проект → один итоговый JAR для **Fabric / Quilt / Forge / NeoForge**.

```bash
./gradlew universalJar
```

Результат:

```text
build/libs/examplemod-1.0.0+26.2.jar
```

## Архитектура

```text
Common/
  src/main/    # общий код, обе физические стороны
  src/client/  # только клиент
  src/server/  # только dedicated server
Fabric/
Forge/
NeoForge/
gradle/universal.gradle
```

Quilt отдельным модулем не компилируется: Quilt Loader загружает Fabric-часть итогового JAR через Fabric compatibility.

## Как устроен universal JAR

Шаблон повторяет ключевую идею Serilum-style bundling:

1. Fabric, Forge и NeoForge компилируются своими toolchain.
2. Каждый loader компилирует исходники `Common` в свою loader-сборку.
3. Shadow-задачи `relocateFabric`, `relocateForge`, `relocateNeoForge` изолируют пакеты:

```text
com.example.examplemod_common_fabric
com.example.examplemod_common_forge
com.example.examplemod_common_neoforge
```

4. Общий `examplemod.mixins.json` превращается в три файла:

```text
examplemod_fabric.mixins.json
examplemod_forge.mixins.json
examplemod_neoforge.mixins.json
```

5. `universalJar` сливает loader-JAR и возвращает только loader-adapter классы в стабильные пакеты:

```text
com.example.examplemod.fabric
com.example.examplemod.forge
com.example.examplemod.neoforge
```

Итоговый архив одновременно содержит:

```text
fabric.mod.json
META-INF/mods.toml
META-INF/neoforge.mods.toml
```

## Common / Client / Server

`Common/src/main` — код для обеих сторон. Не импортируй здесь `net.minecraft.client.*`.

`Common/src/client` — `Minecraft`, GUI, render, keybindings, client-only Mixins.

`Common/src/server` — dedicated-server-only bootstrap и Mixins.

`./gradlew checkCommonSides` автоматически ловит прямые `net.minecraft.client` ссылки в `Common/main` и `Common/server`.

## Быстро создать новый мод из шаблона

```bash
python scripts/setup_mod.py \
  --id mymod \
  --name "My Mod" \
  --package com.yourname.mymod \
  --version 1.0.0 \
  --authors "Your Name" \
  --description "Description"
```

Windows:

```bat
py scripts\setup_mod.py --id mymod --name "My Mod" --package com.yourname.mymod
```

Скрипт меняет `gradle.properties`, переносит Java package, исправляет package/import и переименовывает namespace в `assets/`.

## Централизованные настройки

Основное меняется в `gradle.properties`:

```properties
minecraft_version=26.2
fabric_loader_version=0.19.5
forge_version=65.1.3
neoforge_version=26.2.0.87

mod_id=examplemod
mod_name=Example Mod
mod_version=1.0.0
mod_description=...
mod_authors=YourName
mod_license=MIT
maven_group=com.example
base_package=com.example.examplemod
```

`ModConstants.java` генерируется при сборке из этих значений. Версию/ID не надо повторять вручную в Java.

## Запуск из dev-среды

Fabric:

```bash
./gradlew :Fabric:runClient
./gradlew :Fabric:runServer
```

Forge:

```bash
./gradlew :Forge:runClient
./gradlew :Forge:runServer
```

NeoForge:

```bash
./gradlew :NeoForge:runClient
./gradlew :NeoForge:runServer
```

Открывай в IntelliJ IDEA только корневую папку проекта.

## Сборка

Loader-specific промежуточные JAR:

```bash
./gradlew :Fabric:remapJar :Forge:jar :NeoForge:jar
```

Universal:

```bash
./gradlew universalJar
```

Universal + проверка содержимого:

```bash
./gradlew inspectUniversalJar
```

`./gradlew build` тоже зависит от этой проверки.

`inspectUniversalJar` проверяет наличие трех metadata-файлов, трех Mixin-конфигов, трех relocated Common-копий и стабильных entrypoint-классов.

## Mixins

```text
Common/src/main/java/.../mixin/          # common
Common/src/client/java/.../mixin/client/ # client
Common/src/server/java/.../mixin/server/ # server
```

Все списки находятся в одном `Common/src/main/resources/examplemod.mixins.json`; bundler сам делает loader-specific копии.

## Platform abstraction

`PlatformHelper` уже содержит минимальный набор:

```java
String loaderName();
boolean isModLoaded(String modId);
boolean isDevelopmentEnvironment();
Path configDirectory();
```

Не абстрагируй ванильный Minecraft API без причины. Интерфейс нужен только там, где loaders реально различаются.

## Блоки, предметы, команды, events

`RegistrationBridge` показывает место расширения. Для будущего мода:

- поведение/модели/vanilla-логика → `Common`;
- loader-specific регистрация → соответствующий `Fabric/Forge/NeoForge` пакет;
- если различие повторяется, вынеси узкий интерфейс в Common.

Шаблон намеренно не тянет Architectury API, Fabric API, Cloth Config и другие библиотеки.

## Networking / GUI / rendering

Payload/data и общие handlers можно держать в Common, регистрацию сети — в loader adapters.

GUI model logic — Common. Screen/render/keybindings — `Common/src/client`.

## Fabric API

Шаблону Fabric API не нужен. Если будущему моду он понадобится — добавь dependency только в `Fabric/build.gradle`. Не shade Fabric API в universal JAR.

## Access widener / access transformer

Для сложных модов используй loader-specific файлы:

```text
Fabric/src/main/resources/<modid>.accesswidener
Forge/src/main/resources/META-INF/accesstransformer.cfg
NeoForge/src/main/resources/META-INF/accesstransformer.cfg
```

и включай механизм в соответствующем `build.gradle`.

## Service files

Шаблон не shade-ит сторонние библиотеки, поэтому сейчас ему нечего объединять в `META-INF/services/*`. Если позже начнешь shadow-ить библиотеку с service providers — добавь `mergeServiceFiles()` и отдельно проверь duplicate strategy. Не объединяй loader services вслепую.

## Ограничение universal JAR

Relocation устраняет конфликт одинаковых классов, но не делает loader API взаимозаменяемыми. Common никогда не должен импортировать Fabric/Forge/NeoForge API. Loader-specific API остается в loader modules.

## Quilt

Quilt Loader официально совместим с почти всеми Fabric-модами. Поэтому итоговый JAR содержит Fabric metadata/entrypoints, которые использует и Quilt. Если будущая Fabric-зависимость не совместима с Quilt, это надо решать отдельно; фиктивный четвертый модуль не исправит несовместимость автоматически.

## Проверка без Gradle dependencies

```bash
python scripts/verify_template.py
```

Это проверяет структуру, side separation и JSON после подстановки properties.

Полная проверка требует:

```bash
./gradlew build
```

потому что Gradle должен скачать Minecraft и toolchains loaders.

## Зафиксированные версии на 14.09.2026

- Minecraft 26.2
- Java 25
- Gradle 9.5.1
- Fabric Loader 0.19.5
- Loom 1.17-SNAPSHOT
- Forge 65.1.3
- NeoForge 26.2.0.87
- ModDevGradle 2.0.147
- Shadow 9.6.1

Перед переносом шаблона на следующую версию Minecraft пересверь toolchain versions.
