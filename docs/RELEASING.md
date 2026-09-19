# Publishing Reinforced Minecarts 1.0.0 on GitHub

These steps are for the dedicated repository:

`mavrikakis9099-boop/reinforced-minecarts`

Do not publish Minecarts as a release of the Reinforced Storage repository.

## 1. Publish the repository source

Commit the repository source, README, license, documentation and showcase image to `main`.

Do **not** commit the compiled release JAR to the normal source tree. Release binaries belong under
GitHub Releases.

Suggested commit message:

`Publish Reinforced Minecarts 1.0.0 source`

## 2. Create the release

1. Open **Releases** → **Draft a new release**.
2. Create tag `v1.0.0+26.2` from `main`.
3. Release title: `Reinforced Minecarts 1.0.0 for Minecraft 26.2`.
4. Paste `docs/RELEASE_NOTES_1.0.0.md` into the release description.
5. Attach `reinforced-minecarts-1.0.0+26.2.jar`.
6. Do not mark it as a prerelease.
7. Mark it as the latest release.
8. Publish.

GitHub will automatically provide **Source code (zip)** and **Source code (tar.gz)** from the tag,
so a second hand-made source ZIP is not required.

## 3. Verify

After publishing:

- download the JAR from the release;
- confirm the filename is `reinforced-minecarts-1.0.0+26.2.jar`;
- open the JAR's `fabric.mod.json` and confirm its homepage/source/issues URLs point to the dedicated
  `reinforced-minecarts` repository;
- confirm the README links to the Reinforced Storage dependency;
- confirm GitHub shows the MIT license;
- confirm the release is shown as the latest Minecarts release.

Only after this is verified should the old Minecarts release/tag be removed from the Reinforced
Storage repository.
