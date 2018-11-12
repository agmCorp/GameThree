package uy.com.agm.gamethree.assets.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by AGM on 12/22/2017.
 */

public class AssetScene2d {
    private static final String TAG = AssetScene2d.class.getName();

    private TextureRegion sliderBackground;
    private TextureRegion sliderKnob;
    private TextureRegion helpInitialManual;
    private TextureRegion helpInitialAutomatic;
    private TextureRegion helpEnemyOne;
    private TextureRegion helpEnemyTwo;
    private TextureRegion helpEnemyThree;
    private TextureRegion helpEnemyFour;
    private TextureRegion helpEnemyFive;
    private TextureRegion helpEnemySix;
    private TextureRegion helpEnemySeven;
    private TextureRegion helpEnemyEight;
    private TextureRegion helpEnemyNine;
    private TextureRegion helpEnemyTen;
    private TextureRegion helpEnemyEleven;
    private TextureRegion helpEnemyTwelve;
    private TextureRegion helpPowerOne;
    private TextureRegion helpPowerTwo;
    private TextureRegion helpPowerThree;
    private TextureRegion helpPowerFour;
    private TextureRegion helpPowerFive;
    private TextureRegion helpColOne;
    private TextureRegion helpColTwo;
    private TextureRegion helpColThree;
    private TextureRegion helpColSilverBulletLevelOne;
    private TextureRegion helpColSilverBulletLevelTwo;
    private TextureRegion helpColSilverBulletLevelThree;
    private TextureRegion helpColSilverBulletLevelFour;
    private TextureRegion helpBossOne;
    private TextureRegion helpBossTwo;
    private TextureRegion helpBossThree;
    private TextureRegion helpBossFour;
    private TextureRegion table;
    private TextureRegion wipeThemOut;
    private TextureRegion play;
    private TextureRegion playPressed;
    private TextureRegion settings;
    private TextureRegion settingsPressed;
    private TextureRegion highScores;
    private TextureRegion highScoresPressed;
    private TextureRegion help;
    private TextureRegion helpPressed;
    private TextureRegion credits;
    private TextureRegion creditsPressed;
    private TextureRegion music;
    private TextureRegion musicPressed;
    private TextureRegion musicChecked;
    private TextureRegion sound;
    private TextureRegion soundPressed;
    private TextureRegion soundChecked;
    private TextureRegion easy;
    private TextureRegion easyPressed;
    private TextureRegion medium;
    private TextureRegion mediumPressed;
    private TextureRegion hard;
    private TextureRegion hardPressed;
    private TextureRegion shooting;
    private TextureRegion shootingPressed;
    private TextureRegion shootingChecked;
    private TextureRegion back;
    private TextureRegion backPressed;
    private TextureRegion forward;
    private TextureRegion forwardPressed;
    private TextureRegion pause;
    private TextureRegion pausePressed;
    private TextureRegion tick;
    private TextureRegion tickPressed;
    private TextureRegion cross;
    private TextureRegion crossPressed;
    private TextureRegion levels;
    private TextureRegion levelsPressed;
    private TextureRegion reload;
    private TextureRegion reloadPressed;
    private TextureRegion playSmall;
    private TextureRegion playSmallPressed;
    private TextureRegion home;
    private TextureRegion homePressed;
    private TextureRegion gotIt;
    private TextureRegion gotItPressed;
    private TextureRegion goldTrophy;
    private TextureRegion silverTrophy;
    private TextureRegion bronzeTrophy;
    private TextureRegion badge;
    private TextureRegion star;
    private TextureRegion emptyStar;
    private TextureRegion vScrollbar9;
    private TextureRegion vScrollbarKnob9;
    private TextureRegion hScrollbar9;
    private TextureRegion hScrollbarKnob9;
    private TextureRegion bigBossBackground;
    private TextureRegion helpBackground;
    private TextureRegion hand;
    private AssetHeroHead heroHead;
    private AssetGoldenHeroHead goldenHeroHead;
    private AssetEndurance endurance;
    private AssetHourglass hourglass;
    private AssetVictory victory;
    private AssetStageCleared stageCleared;
    private AssetStageFailed stageFailed;
    private AssetGameOver gameOver;
    private AssetLetsGo letsGo;
    private AssetBigBoss bigBoss;

    public AssetScene2d(TextureAtlas atlasUI) {
        sliderBackground = atlasUI.findRegion("sliderBackground");
        sliderKnob = atlasUI.findRegion("sliderKnob");

        helpInitialManual = atlasUI.findRegion("helpInitialManual");
        helpInitialAutomatic = atlasUI.findRegion("helpInitialAutomatic");
        helpEnemyOne = atlasUI.findRegion("helpEnemyOne");
        helpEnemyTwo = atlasUI.findRegion("helpEnemyTwo");
        helpEnemyThree = atlasUI.findRegion("helpEnemyThree");
        helpEnemyFour = atlasUI.findRegion("helpEnemyFour");
        helpEnemyFive = atlasUI.findRegion("helpEnemyFive");
        helpEnemySix = atlasUI.findRegion("helpEnemySix");
        helpEnemySeven = atlasUI.findRegion("helpEnemySeven");
        helpEnemyEight = atlasUI.findRegion("helpEnemyEight");
        helpEnemyNine = atlasUI.findRegion("helpEnemyNine");
        helpEnemyTen = atlasUI.findRegion("helpEnemyTen");
        helpEnemyEleven = atlasUI.findRegion("helpEnemyEleven");
        helpEnemyTwelve = atlasUI.findRegion("helpEnemyTwelve");
        helpPowerOne = atlasUI.findRegion("helpPowerOne");
        helpPowerTwo = atlasUI.findRegion("helpPowerTwo");
        helpPowerThree = atlasUI.findRegion("helpPowerThree");
        helpPowerFour = atlasUI.findRegion("helpPowerFour");
        helpPowerFive = atlasUI.findRegion("helpPowerFive");
        helpColOne = atlasUI.findRegion("helpColOne");
        helpColTwo = atlasUI.findRegion("helpColTwo");
        helpColThree = atlasUI.findRegion("helpColThree");
        helpColSilverBulletLevelOne = atlasUI.findRegion("helpColSilverBulletLevelOne");
        helpColSilverBulletLevelTwo = atlasUI.findRegion("helpColSilverBulletLevelTwo");
        helpColSilverBulletLevelThree = atlasUI.findRegion("helpColSilverBulletLevelThree");
        helpColSilverBulletLevelFour = atlasUI.findRegion("helpColSilverBulletLevelFour");
        helpBossOne = atlasUI.findRegion("helpBossOne");
        helpBossTwo = atlasUI.findRegion("helpBossTwo");
        helpBossThree = atlasUI.findRegion("helpBossThree");
        helpBossFour = atlasUI.findRegion("helpBossFour");
        table = atlasUI.findRegion("table");
        wipeThemOut = atlasUI.findRegion("wipeThemOut");
        play = atlasUI.findRegion("play");
        playPressed = atlasUI.findRegion("playPressed");
        settings = atlasUI.findRegion("settings");
        settingsPressed = atlasUI.findRegion("settingsPressed");
        highScores = atlasUI.findRegion("highScores");
        highScoresPressed = atlasUI.findRegion("highScoresPressed");
        help = atlasUI.findRegion("help");
        helpPressed = atlasUI.findRegion("helpPressed");
        credits = atlasUI.findRegion("credits");
        creditsPressed = atlasUI.findRegion("creditsPressed");
        music = atlasUI.findRegion("music");
        musicPressed = atlasUI.findRegion("musicPressed");
        musicChecked = atlasUI.findRegion("musicChecked");
        sound = atlasUI.findRegion("sound");
        soundPressed = atlasUI.findRegion("soundPressed");
        soundChecked = atlasUI.findRegion("soundChecked");
        easy = atlasUI.findRegion("easy");
        easyPressed = atlasUI.findRegion("easyPressed");
        medium = atlasUI.findRegion("medium");
        mediumPressed = atlasUI.findRegion("mediumPressed");
        hard = atlasUI.findRegion("hard");
        hardPressed = atlasUI.findRegion("hardPressed");
        shooting = atlasUI.findRegion("shooting");
        shootingPressed = atlasUI.findRegion("shootingPressed");
        shootingChecked = atlasUI.findRegion("tick");
        back = atlasUI.findRegion("back");
        backPressed = atlasUI.findRegion("backPressed");
        forward = atlasUI.findRegion("forward");
        forwardPressed = atlasUI.findRegion("forwardPressed");
        pause = atlasUI.findRegion("pause");
        pausePressed = atlasUI.findRegion("pausePressed");
        tick = atlasUI.findRegion("tick");
        tickPressed = atlasUI.findRegion("tickPressed");
        cross = atlasUI.findRegion("cross");
        crossPressed = atlasUI.findRegion("crossPressed");
        levels = atlasUI.findRegion("levels");
        levelsPressed = atlasUI.findRegion("levelsPressed");
        reload = atlasUI.findRegion("reload");
        reloadPressed = atlasUI.findRegion("reloadPressed");
        playSmall = atlasUI.findRegion("playSmall");
        playSmallPressed = atlasUI.findRegion("playSmallPressed");
        home = atlasUI.findRegion("home");
        homePressed = atlasUI.findRegion("homePressed");
        gotIt = atlasUI.findRegion("gotIt");
        gotItPressed = atlasUI.findRegion("gotItPressed");
        goldTrophy = atlasUI.findRegion("goldTrophy");
        silverTrophy = atlasUI.findRegion("silverTrophy");
        bronzeTrophy = atlasUI.findRegion("bronzeTrophy");
        badge = atlasUI.findRegion("badge");
        star = atlasUI.findRegion("star");
        emptyStar = atlasUI.findRegion("emptyStar");
        vScrollbar9 = atlasUI.findRegion("vScrollbar9");
        vScrollbarKnob9 = atlasUI.findRegion("vScrollbarKnob9");
        hScrollbar9 = atlasUI.findRegion("hScrollbar9");
        hScrollbarKnob9 = atlasUI.findRegion("hScrollbarKnob9");
        bigBossBackground = atlasUI.findRegion("bigBossBackground");
        helpBackground = atlasUI.findRegion("helpBackground");
        hand = atlasUI.findRegion("hand");
        heroHead = new AssetHeroHead(atlasUI);
        goldenHeroHead = new AssetGoldenHeroHead(atlasUI);
        endurance = new AssetEndurance(atlasUI);
        hourglass = new AssetHourglass(atlasUI);
        victory = new AssetVictory(atlasUI);
        stageCleared = new AssetStageCleared(atlasUI);
        stageFailed = new AssetStageFailed(atlasUI);
        gameOver = new AssetGameOver(atlasUI);
        letsGo = new AssetLetsGo(atlasUI);
        bigBoss = new AssetBigBoss(atlasUI);
    }

    public TextureRegion getSliderBackground() {
        return sliderBackground;
    }

    public TextureRegion getSliderKnob() {
        return sliderKnob;
    }

    public TextureRegion getHelpInitialManual() {
        return helpInitialManual;
    }

    public TextureRegion getHelpInitialAutomatic() {
        return helpInitialAutomatic;
    }

    public TextureRegion getHelpEnemyOne() {
        return helpEnemyOne;
    }

    public TextureRegion getHelpEnemyTwo() {
        return helpEnemyTwo;
    }

    public TextureRegion getHelpEnemyThree() {
        return helpEnemyThree;
    }

    public TextureRegion getHelpEnemyFour() {
        return helpEnemyFour;
    }

    public TextureRegion getHelpEnemyFive() {
        return helpEnemyFive;
    }

    public TextureRegion getHelpEnemySix() {
        return helpEnemySix;
    }

    public TextureRegion getHelpEnemySeven() {
        return helpEnemySeven;
    }

    public TextureRegion getHelpEnemyEight() {
        return helpEnemyEight;
    }

    public TextureRegion getHelpEnemyNine() {
        return helpEnemyNine;
    }

    public TextureRegion getHelpEnemyTen() {
        return helpEnemyTen;
    }

    public TextureRegion getHelpEnemyEleven() {
        return helpEnemyEleven;
    }

    public TextureRegion getHelpEnemyTwelve() {
        return helpEnemyTwelve;
    }

    public TextureRegion getHelpPowerOne() {
        return helpPowerOne;
    }

    public TextureRegion getHelpPowerTwo() {
        return helpPowerTwo;
    }

    public TextureRegion getHelpPowerThree() {
        return helpPowerThree;
    }

    public TextureRegion getHelpPowerFour() {
        return helpPowerFour;
    }

    public TextureRegion getHelpPowerFive() {
        return helpPowerFive;
    }

    public TextureRegion getHelpColOne() {
        return helpColOne;
    }

    public TextureRegion getHelpColTwo() {
        return helpColTwo;
    }

    public TextureRegion getHelpColThree() {
        return helpColThree;
    }

    public TextureRegion getHelpColSilverBulletLevelOne() {
        return helpColSilverBulletLevelOne;
    }

    public TextureRegion getHelpColSilverBulletLevelTwo() {
        return helpColSilverBulletLevelTwo;
    }

    public TextureRegion getHelpColSilverBulletLevelThree() {
        return helpColSilverBulletLevelThree;
    }

    public TextureRegion getHelpColSilverBulletLevelFour() {
        return helpColSilverBulletLevelFour;
    }

    public TextureRegion getHelpBossOne() {
        return helpBossOne;
    }

    public TextureRegion getHelpBossTwo() {
        return helpBossTwo;
    }

    public TextureRegion getHelpBossThree() {
        return helpBossThree;
    }

    public TextureRegion getHelpBossFour() {
        return helpBossFour;
    }

    public TextureRegion getTable() {
        return table;
    }

    public TextureRegion getWipeThemOut() {
        return wipeThemOut;
    }

    public TextureRegion getPlay() {
        return play;
    }

    public TextureRegion getPlayPressed() {
        return playPressed;
    }

    public TextureRegion getSettings() {
        return settings;
    }

    public TextureRegion getSettingsPressed() {
        return settingsPressed;
    }

    public TextureRegion getHighScores() {
        return highScores;
    }

    public TextureRegion getHighScoresPressed() {
        return highScoresPressed;
    }

    public TextureRegion getHelp() {
        return help;
    }

    public TextureRegion getHelpPressed() {
        return helpPressed;
    }

    public TextureRegion getCredits() {
        return credits;
    }

    public TextureRegion getCreditsPressed() {
        return creditsPressed;
    }

    public TextureRegion getMusic() {
        return music;
    }

    public TextureRegion getMusicPressed() {
        return musicPressed;
    }

    public TextureRegion getMusicChecked() {
        return musicChecked;
    }

    public TextureRegion getSound() {
        return sound;
    }

    public TextureRegion getSoundPressed() {
        return soundPressed;
    }

    public TextureRegion getSoundChecked() {
        return soundChecked;
    }

    public TextureRegion getEasy() {
        return easy;
    }

    public TextureRegion getEasyPressed() {
        return easyPressed;
    }

    public TextureRegion getMedium() {
        return medium;
    }

    public TextureRegion getMediumPressed() {
        return mediumPressed;
    }

    public TextureRegion getHard() {
        return hard;
    }

    public TextureRegion getHardPressed() {
        return hardPressed;
    }

    public TextureRegion getShooting() {
        return shooting;
    }

    public TextureRegion getShootingPressed() {
        return shootingPressed;
    }

    public TextureRegion getShootingChecked() {
        return shootingChecked;
    }

    public TextureRegion getBack() {
        return back;
    }

    public TextureRegion getBackPressed() {
        return backPressed;
    }

    public TextureRegion getForward() {
        return forward;
    }

    public TextureRegion getForwardPressed() {
        return forwardPressed;
    }

    public TextureRegion getPause() {
        return pause;
    }

    public TextureRegion getPausePressed() {
        return pausePressed;
    }

    public TextureRegion getTick() {
        return tick;
    }

    public TextureRegion getTickPressed() {
        return tickPressed;
    }

    public TextureRegion getCross() {
        return cross;
    }

    public TextureRegion getCrossPressed() {
        return crossPressed;
    }

    public TextureRegion getLevels() {
        return levels;
    }

    public TextureRegion getLevelsPressed() {
        return levelsPressed;
    }

    public TextureRegion getReload() {
        return reload;
    }

    public TextureRegion getReloadPressed() {
        return reloadPressed;
    }

    public TextureRegion getPlaySmall() {
        return playSmall;
    }

    public TextureRegion getPlaySmallPressed() {
        return playSmallPressed;
    }

    public TextureRegion getHome() {
        return home;
    }

    public TextureRegion getHomePressed() {
        return homePressed;
    }

    public TextureRegion getGotIt() {
        return gotIt;
    }

    public TextureRegion getGotItPressed() {
        return gotItPressed;
    }

    public TextureRegion getGoldTrophy() {
        return goldTrophy;
    }

    public TextureRegion getSilverTrophy() {
        return silverTrophy;
    }

    public TextureRegion getBronzeTrophy() {
        return bronzeTrophy;
    }

    public TextureRegion getBadge() {
        return badge;
    }

    public TextureRegion getRankingImage(int ranking) {
        TextureRegion rankingImage = null;
        if (ranking > 0) {
            switch (ranking) {
                case 1:
                    rankingImage = goldTrophy;
                    break;
                case 2:
                    rankingImage = silverTrophy;
                    break;
                case 3:
                    rankingImage = bronzeTrophy;
                    break;
                default:
                    rankingImage = badge;
                    break;
            }
        }
        return rankingImage;
    }

    public TextureRegion getStar() {
        return star;
    }

    public TextureRegion getEmptyStar() {
        return emptyStar;
    }

    public TextureRegion getvScrollbar9() {
        return vScrollbar9;
    }

    public TextureRegion getvScrollbarKnob9() {
        return vScrollbarKnob9;
    }

    public TextureRegion gethScrollbar9() {
        return hScrollbar9;
    }

    public TextureRegion gethScrollbarKnob9() {
        return hScrollbarKnob9;
    }

    public TextureRegion getBigBossBackground() {
        return bigBossBackground;
    }

    public TextureRegion getHelpBackground() {
        return helpBackground;
    }

    public TextureRegion getHand() {
        return hand;
    }

    public AssetHeroHead getHeroHead() {
        return heroHead;
    }

    public AssetGoldenHeroHead getGoldenHeroHead() {
        return goldenHeroHead;
    }

    public AssetEndurance getEndurance() {
        return endurance;
    }

    public AssetHourglass getHourglass() {
        return hourglass;
    }

    public AssetVictory getVictory() {
        return victory;
    }

    public AssetStageCleared getStageCleared() {
        return stageCleared;
    }

    public AssetStageFailed getStageFailed() {
        return stageFailed;
    }

    public AssetGameOver getGameOver() {
        return gameOver;
    }

    public AssetLetsGo getLetsGo() {
        return letsGo;
    }

    public AssetBigBoss getBigBoss() {
        return bigBoss;
    }
}
