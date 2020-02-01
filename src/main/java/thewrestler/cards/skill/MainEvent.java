package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import thewrestler.actions.GainGoldAction;
import thewrestler.actions.cards.skill.HardwayAction;
import thewrestler.actions.cards.skill.SpringboardAction;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.signaturemoves.cards.AbstractSignatureMoveCard;
import thewrestler.signaturemoves.moveinfos.AbstractSignatureMoveInfo;
import thewrestler.util.CardUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class MainEvent extends CustomCard {
  public static final String ID = "WrestlerMod:MainEvent";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "mainevent.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.SKILL;
  private static final CardRarity RARITY = CardRarity.RARE;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final int COST = 2;
  private static final int UPGRADED_COST = 1;

  public MainEvent() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (WrestlerCharacter.hasSignatureMoveInfo()) {
      if (WrestlerCharacter.getSignatureMoveInfo().canStillTriggerCardGain()) {
        WrestlerCharacter.getSignatureMoveInfo().manuallyTriggerCardGain(true);
      } else {
        HashMap<CardGroup, AbstractCard> cardMap = CardUtil.getAllInBattleInstances(
            WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCard().cardID);

        CardGroup signatureMoveCardGroup;
        AbstractCard signatureMoveCard;
        if (!cardMap.isEmpty()) {
          signatureMoveCardGroup = (CardGroup) (cardMap.keySet().toArray())[0];
          signatureMoveCard = (AbstractCard) (cardMap.values().toArray())[0];

          if (signatureMoveCardGroup.type != CardGroup.CardGroupType.HAND) {
            if (signatureMoveCardGroup.type == CardGroup.CardGroupType.EXHAUST_PILE) {
              AbstractDungeon.actionManager.addToBottom(new ExhaustToHandAction(signatureMoveCard));
            } else {
              if (p.hand.size() < 10) {
                signatureMoveCardGroup.moveToHand(signatureMoveCard, signatureMoveCardGroup);
              } else {
                signatureMoveCardGroup.moveToDiscardPile(signatureMoveCard);
                p.createHandIsFullDialog();
              }
            }
          }
        }
      }
    }
  }

  private void handleExhaustToPlay(AbstractCard card) {
    AbstractPlayer player = AbstractDungeon.player;
    if ((AbstractDungeon.player.hasPower("Corruption")) && (card.type == AbstractCard.CardType.SKILL)) {
      card.setCostForTurn(-9);
    }
    card.stopGlowing();
    card.unhover();
    card.unfadeOut();
    player.exhaustPile.removeCard(card);
    player.hand.refreshHandLayout();
  }

  @Override
  public AbstractCard makeCopy() {
    return new MainEvent();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeBaseCost(UPGRADED_COST);
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