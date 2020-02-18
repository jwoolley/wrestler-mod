package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import basemod.abstracts.CustomSavable;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.util.info.sportsmanship.SportsmanshipInfo;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Screwjob extends CustomCard implements CustomSavable<Integer> {
  public static final String ID = "WrestlerMod:Screwjob";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "screwjob.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 5;

  private static final int DAMAGE_INCREASE_PER_UNSPORTING = 1;

  public Screwjob() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.baseDamage = this.damage = this.misc = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DAMAGE_INCREASE_PER_UNSPORTING;
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(
        new ScrewjobAction(
            m, new DamageInfo(p, this.damage, damageTypeForTurn),
            this.magicNumber * (this.upgraded ? 2 : 1), this.uuid, this));
  }

  @Override
  public Integer onSave() {
    return this.baseDamage;
  }

  @Override
  public void onLoad(Integer baseDamage) {
    this.baseDamage = this.damage = this.misc = baseDamage;
    this.rawDescription = getDescription();
    this.initializeDescription();
  }

  private static class ScrewjobAction extends AbstractGameAction {
    private static final float SOUND_DURATION = 0.35f;
    private static final float DAMAGE_DURATION = 0.1f;
    private int increasePerUnsporting;
    private DamageInfo info;
    private UUID uuid;
    private boolean playedSound;
    private boolean tickedDamageOnce = false;
    private Screwjob card;

    public ScrewjobAction(AbstractCreature target, DamageInfo info, int incAmount, UUID targetUUID, Screwjob card) {
      this.info = info;
      setValues(target, info);
      this.increasePerUnsporting = incAmount;
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.duration = SOUND_DURATION + DAMAGE_DURATION;
      this.uuid = targetUUID;
      this.playedSound = false;
      this.card = card;
    }

    public void update() {
      if (this.target != null) {
        if (!this.playedSound && this.duration <= SOUND_DURATION) {
          if (SportsmanshipInfo.isUnsporting()) {
            this.card.flash();
          }
          CardCrawlGame.sound.play("DRILL_SPIN_1");
          playedSound = true;
        } else if (this.duration <= DAMAGE_DURATION ) {
          AbstractDungeon.effectList.add(
              new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_DIAGONAL));
          this.target.damage(this.info);

          if (!tickedDamageOnce) {
            tickedDamageOnce = true;
          } else {
            if (SportsmanshipInfo.isUnsporting()) {
              final int damageIncrease = SportsmanshipInfo.getAmount() * this.increasePerUnsporting;
              for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.uuid.equals(this.uuid)) {
                  c.misc += damageIncrease;
                  c.applyPowers();
                  c.baseDamage = c.misc;
                  c.isDamageModified = false;
                }
              }
              for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
                c.misc += damageIncrease;
                c.applyPowers();
                c.baseDamage = c.misc;
              }
            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
              AbstractDungeon.actionManager.clearPostCombatActions();
            }
            this.isDone = true;
          }
        }
      } else {
        this.isDone = true;
      }
      tickDuration();
    }
  }

  @Override
  public AbstractCard makeCopy() {
    return new Screwjob();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription();
      this.initializeDescription();
    }
  }

  private static String getDescription() {
    return DESCRIPTION;
  }

  private static List<AbstractTooltipKeyword> EXTRA_KEYWORDS = Arrays.asList(
      CustomTooltipKeywords.getTooltipKeyword(CustomTooltipKeywords.PENALTY_CARD)
  );

  @Override
  public List<TooltipInfo> getCustomTooltips() {
    return TooltipKeywords.getTooltipInfos(EXTRA_KEYWORDS);
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}