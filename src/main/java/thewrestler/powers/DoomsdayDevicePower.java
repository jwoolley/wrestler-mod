package thewrestler.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashPowerEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import javafx.util.Pair;
import thewrestler.WrestlerMod;
import thewrestler.cards.skill.AbstractPenaltyCardListener;
import thewrestler.signaturemoves.cards.skill.ChopBlock;
import thewrestler.util.CreatureUtils;

public class DoomsdayDevicePower extends AbstractWrestlerPower implements CloneablePowerInterface {
  private static final Color PALE_YELLOW = new Color(1.0F, 1.0F, .75F, 1.0F);

  public static final String POWER_ID = WrestlerMod.makeID("DoomsdayDevicePower");
  public static final String IMG = "doomsday.png";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static final PowerType POWER_TYPE = PowerType.BUFF;

  public DoomsdayDevicePower(int amount) {
    super(POWER_ID, NAME, IMG, AbstractDungeon.player,  AbstractDungeon.player, amount, POWER_TYPE);
  }

  @Override
  public void stackPower(int amount) {
    this.amount += amount;
    updateDescription();
  }

  @Override
  public void atStartOfTurnPostDraw() {
      AbstractDungeon.actionManager.addToBottom(new DoomsdayDeviceAction(this.amount));
  }

  private static class DoomsdayDeviceAction extends AbstractGameAction {
    private static final float DURATION = Settings.ACTION_DUR_XFAST + 0.25f;

    final AbstractPlayer player;

    private boolean tickedOnce = false;

    DoomsdayDeviceAction(int numCards) {
      this.duration = DURATION;
      this.actionType = ActionType.DAMAGE;
      this.amount = numCards;
      this.player = AbstractDungeon.player;
    }

    @Override
    public void update() {
      if (!tickedOnce) {
        AbstractDungeon.effectsQueue.add(new FlashPowerEffect(this.player.getPower(POWER_ID)));
        AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.9F, 3.0f, true));
        CardCrawlGame.sound.play("TIME_BOMB_1");
        tickedOnce = true;
      } else if (this.duration < Settings.ACTION_DUR_XFAST) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.amount, new DoomsdayDamageAction()));
        this.isDone = true;
      }
      tickDuration();
    }

    class DoomsdayDamageAction extends AbstractGameAction {
      public DoomsdayDamageAction() {
        this.duration = Settings.ACTION_DUR_XLONG;
      }

      @Override
      public void update() {
        if (this.duration < Settings.ACTION_DUR_XFAST) {

          final int handSize = AbstractDungeon.player.hand.group.size();

          AbstractDungeon.effectsQueue.add(new RoomTintEffect(PALE_YELLOW, 0.99F));
          AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.WHITE.cpy(), false));

          AbstractDungeon.actionManager.addToTop(new ShakeScreenAction(0.0F,
              ScreenShake.ShakeDur.XLONG, ScreenShake.ShakeIntensity.HIGH));

          AbstractDungeon.actionManager.addToTop(
              new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(2 * handSize, true),
                  DamageInfo.DamageType.THORNS, AttackEffect.FIRE, true));

          AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(player, player, POWER_ID));
          this.isDone = true;
        }
        tickDuration();
      }
    }
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  @Override
  public AbstractPower makeCopy() {
    return new DoomsdayDevicePower(this.amount);
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}