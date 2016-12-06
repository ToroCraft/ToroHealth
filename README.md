
#ToroHealth Damage Indicators

With ToroHealth Damage Indicators, damage given, received, or mitigated will be displayed as a number that pops off of the entity.

Also, health bars will appear in the top left corner for the entity in the player's crosshairs.

Customization options are available for changing the color of the damage numbers and the display style of the health bar (Compact or Standard).  I will soon be adding options to re-position the health bars and may make some visual tweaks to them.

###[Download from here](https://minecraft.curseforge.com/projects/torohealth-damage-indicators)

![Screenshot](https://i.imgur.com/C9oBhZ5.png)

##Development Environment Setup
Download the desired version of Forge MDK from https://files.minecraftforge.net/ and unzip the MDK into a new dirctory. After the MDK is unzipped, remove the `main` folder from the `src` folder and clone this repo into the `src` directory as `main`. Then you will need to either copy or link the `build.gradle` from the repository to the root of the MDK, replacing the original one. 

###Setup Example
Replace `<MC_VERSION>` with the Minecraft version of the MDK (for example `~/mdk_1.10.2`) and `<MDK_FILE>` with the file name of the MDK you downloaded (for example `forge-1.10.2-12.18.2.2099-mdk.zip`)

```
mkdir ~/mdk_<MC_VERSION>
cd ~/mdk_<MC_VERSION>
cp <MDK_FILE> .
unzip <MDK_FILE>
rm -rf src/main
git clone https://github.com/ToroCraft/ToroHealth.git
mv build.gradle build.default.gradle
ln -s src/main/build.gradle build.gradle
./gradlew setupDecompWorkspace
./gradlew eclipse
```
