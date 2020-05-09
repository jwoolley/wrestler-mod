package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.keywords.AbstractTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.keywords.TooltipKeywords;
import thewrestler.powers.InjuredPower;
import thewrestler.util.CardUtil;

import java.util.Arrays;
import java.util.List;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class StomachClaw extends CustomCard {
  public static final String ID = "WrestlerMod:StomachClaw";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "stomachclaw.png";

  private static final int DEBUFF_AMOUNT = 4;
  private static final int DEBUFF_AMOUNT_UPGRADE  = 2;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;

  public StomachClaw() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseMagicNumber = this.magicNumber = DEBUFF_AMOUNT;
    this.exhaust = true;
    CardUtil.makeCardDirty(this);
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
//    if (m != null) {
//      AbstractDungeon.actionManager.addToBottom(new VFXAction(
//          new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Color.ORANGE.cpy()), 0.1F));
//    }

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, p, new InjuredPower(m, p, this.magicNumber), this.magicNumber));
  }

  @Override
  public AbstractCard makeCopy() {
    return new StomachClaw();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(DEBUFF_AMOUNT_UPGRADE);
      this.rawDescription = getDescription();
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