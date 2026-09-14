# Validation status

Generated on 2026-09-14.

## Passed locally

- `python3 scripts/verify_template.py`
- Python syntax check for `setup_mod.py` and `verify_template.py`
- POSIX shell syntax check for `gradlew`
- rename smoke test on a copy of the project (`examplemod` -> `testmod`, package move included)
- presence checks for Fabric / Forge / NeoForge metadata
- presence checks for the Serilum-style relocation tasks in `gradle/universal.gradle`

## Full Gradle build

A real `./gradlew tasks` was attempted in the generation environment, but the environment cannot resolve `services.gradle.org`:

```text
curl: (6) Could not resolve host: services.gradle.org
```

Therefore `./gradlew build` / Minecraft client launches were **not** falsely marked as passed here.

On a normal internet-connected development machine, run:

```bash
./gradlew build
```

The root build depends on `inspectUniversalJar`, which verifies the final universal archive structure after compilation.
