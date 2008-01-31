@echo off
echo Removing build history...
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\builds
for /d %%i in (work\jobs\*) do del /s /q %%i\nextBuildNumber
for /d %%i in (work\jobs\*) do rmdir /s /q %%i\modules
rmdir /s /q work\fingerprints
del work\queue.txt
del work\secret.key