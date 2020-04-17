package thewrestler.cards.skill;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.utility.ExhaustToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.characters.WrestlerCharacter;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.util.CardUtil;

import java.util.HashMap;

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

  private static final int COST = 1;
  private static final int BRAVADO_GAIN = 2;

  public MainEvent() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_WRESTLER_ORANGE, RARITY, TARGET);
    this.exhaust = true;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    if (WrestlerCharacter.hasSignatureMoveInfo()) {
      if (WrestlerCharacter.getSignatureMoveInfo().canStillTriggerCardGain()) {
        WrestlerCharacter.getSignatureMoveInfo().manuallyTriggerCardGain(true, this.upgraded);
      } else {
        HashMap<CardGroup, AbstractCard> cardMap = CardUtil.getAllInBattleInstances(
            WrestlerCharacter.getSignatureMoveInfo().getSignatureMoveCardReference().cardID);

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

  @Override
  public AbstractCard makeCopy() {
    return new MainEvent();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.rawDescription = getDescription(this.upgraded);
      initializeDescription();
    }
  }

  public static String getDescription(boolean isUpgraded) {
    return DESCRIPTION
        + (!isUpgraded ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1])
        + EXTENDED_DESCRIPTION[2];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}