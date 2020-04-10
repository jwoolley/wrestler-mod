package thewrestler.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.patches.rewards.TrademarkMoveRewardPatch;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.upgrades.AbstractSignatureMoveUpgrade;
import thewrestler.ui.UiHelper;
import thewrestler.util.TextureLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrademarkMoveReward extends CustomReward {
  private static final String ICON_IMG_PATH =  UiHelper.getUiImageResourcePath("rewards/trademarkmove.png");

  private static final Texture ICON_IMG = TextureLoader.getTexture(ICON_IMG_PATH);

  public TrademarkMoveReward() {
    super(ICON_IMG, "Trademark Move Upgrade", TrademarkMoveRewardPatch.WRESTLER_TRADEMARK_MOVE_REWARD);
  }

  public boolean isFinished = false;

  private static final int NUM_REWARDS = 3;
  @Override
  public boolean claimReward() {
   if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {

      final AbstractSignatureMoveCard originalCard = WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCardReference();

      List<AbstractSignatureMoveUpgrade> upgrades = originalCard.selectRandomUpgrades(NUM_REWARDS);

      Map<AbstractSignatureMoveUpgrade, AbstractSignatureMoveCard> upgradeMap = new HashMap<>();

      upgrades.forEach(u -> upgradeMap.put(u, getUpgradedCopy(originalCard, u)));

      for (AbstractSignatureMoveUpgrade upgrade : upgradeMap.keySet()) {
        final AbstractSignatureMoveCard option = upgradeMap.get(upgrade);
        option.setUpgradeSelectedCallback(aVoid -> {
          CardCrawlGame.sound.play("BOXING_BELL_DOUBLE_1");

          final ShowCardBrieflyEffect effect = new ShowCardBrieflyEffect(option.makeStatEquivalentCopy());

          effect.duration *= 0.8;
          AbstractDungeon.topLevelEffects.add(effect);
          AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));

          WrestlerCharacter.getSignatureMoveInfo().applyUpgrade(upgrade);
          isFinished = true;
          return null;
        });
      }

      AbstractDungeon.cardRewardScreen.chooseOneOpen(new ArrayList<>(upgradeMap.values()));

      AbstractDungeon.previousScreen = AbstractDungeon.CurrentScreen.COMBAT_REWARD;
    }
    return true;
  }

  private AbstractSignatureMoveCard getUpgradedCopy(AbstractSignatureMoveCard card, AbstractSignatureMoveUpgrade upgrade) {
    AbstractSignatureMoveCard upgradedCopy = (AbstractSignatureMoveCard) card.makeStatEquivalentCopy();
    upgradedCopy.applyUpgrade(upgrade);
    return upgradedCopy;
  }
}
