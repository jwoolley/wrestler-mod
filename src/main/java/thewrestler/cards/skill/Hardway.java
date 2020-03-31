package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import thewrestler.actions.cards.skill.HardwayAction;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CardUtil;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Hardway extends CustomCard {
  public static final String ID = "WrestlerMod:Hardway";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "hardway.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int STRENGTH_LOSS = 2;
  private static final int STRENGTH_LOSS_UPGRADE = 1;

  public Hardway() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.exhaust = true;
    this.magicNumber = this.baseMagicNumber = STRENGTH_LOSS;
    CardUtil.makeCardDirty(this, this.type);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    // TODO: add VFX/SFX
    //   AbstractDungeon.actionManager.addToBottom(new SFXAction("BOO_CROWD_1"));

    AbstractDungeon.actionManager.addToBottom(
        new VFXAction(new ClawEffect(m.hb.cX, m.hb.cY, Color.SCARLET, Color.RED), 0.2F));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new StrengthPower(m, -this.magicNumber), -this.magicNumber, true));

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(p, p, new ThornsPower(p, this.magicNumber), this.magicNumber, true));

    AbstractDungeon.actionManager.addToBottom(
        new LoseHPAction(p, p, this.magicNumber, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
  }

  @Override
  public AbstractCard makeCopy() {
    return new Hardway();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(STRENGTH_LOSS_UPGRADE);
      initializeDescription();
    }
  }

  public static String getDescription() {
    return DESCRIPTION;
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}