package thewrestler.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import thewrestler.cards.colorless.skill.FairPlay;
import thewrestler.cards.colorless.skill.FoulPlay;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.patches.rewards.TrademarkMoveRewardPatch;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.cards.Chokeslam;
import thewrestler.signaturemoves.cards.Piledriver;
import thewrestler.signaturemoves.upgrades.UpgradeType;
import thewrestler.ui.UiHelper;
import thewrestler.util.TextureLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TrademarkMoveReward extends CustomReward {
  private static final String ICON_IMG_PATH =  UiHelper.getUiImageResourcePath("rewards/trademarkmove.png");

  private static final Texture ICON_IMG = TextureLoader.getTexture(ICON_IMG_PATH);

  public TrademarkMoveReward() {
    super(ICON_IMG, "Trademark Move Upgrade", TrademarkMoveRewardPatch.WRESTLER_TRADEMARK_MOVE_REWARD);
  }

  public static List<AbstractSignatureMoveCard> options = new ArrayList<>();

  public boolean isFinished = false;

  @Override
  public boolean claimReward() {
   if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
      ArrayList<AbstractCard> options = new ArrayList<>();
      options.add(new Chokeslam());
      options.add(new Piledriver());

      for (AbstractCard option : options) {
        ((AbstractSignatureMoveCard) option).setUpgradeSelectedCallback(aVoid -> {
          CardCrawlGame.sound.play("BOXING_BELL_DOUBLE_1");

          final ShowCardBrieflyEffect effect = new ShowCardBrieflyEffect(option.makeStatEquivalentCopy());
          effect.duration *= 0.8;
          AbstractDungeon.topLevelEffects.add(effect);
          AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

          WrestlerCharacter.getSignatureMoveInfo().upgradeMove(UpgradeType.COST_REDUCTION);

          isFinished = true;
          return null;
        });
      }

      AbstractDungeon.cardRewardScreen.chooseOneOpen(options);

      AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
    }
    return true;
  }
}
