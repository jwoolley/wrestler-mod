package thewrestler.cards.attack;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import thewrestler.actions.cards.attack.TornadoTagAction;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.cards.skill.SafetyTag;
import thewrestler.effects.utils.VFXActionTemplate;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TornadoTag extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:TornadoTag";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "tornadotag.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
  private static AbstractCard BASE_PREVIEW_CARD;
  private static AbstractCard UPGRADED_PREVIEW_CARD;

  private static final int DAMAGE = 7;

  private static final int NUM_ATTACKS = 2;
  private static final int NUM_ATTACKS_UPGRADE  = 1;

  private static final int COST = 1;

  public TornadoTag() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(SafetyTag.NAME), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_ATTACKS;
    this.exhaust = true;
  }

  @Override
  public AbstractCard getPreviewCard() {
    return  this.upgraded ? UPGRADED_PREVIEW_CARD : BASE_PREVIEW_CARD;
  }

  @Override
  public void use(AbstractPlayer p, AbstractMonster m) {
    AbstractDungeon.actionManager.addToBottom(new TornadoTagAction(
            p,
            AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng),
            this.damage,
            this.magicNumber,
            new VFXActionTemplate() {
              @Override
              public VFXAction getAction(float x, float y) {
                return new VFXAction(p, new LightningEffect(x, y), 0.1F);
              }
            },
            new SFXAction("THUNDERCLAP", 0.025f)
        )
    );

    AbstractDungeon.actionManager.addToBottom(
        new MakeTempCardInDiscardAction(this.getPreviewCard().makeStatEquivalentCopy(), 1));
  }

  @Override
  public AbstractCard makeCopy() {
    return new TornadoTag();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.upgradeMagicNumber(NUM_ATTACKS_UPGRADE);
      this.rawDescription = getDescription(this.getPreviewCard().name);
      initializeDescription();
    }
  }

  private static AbstractCard getPreviewCard(boolean upgraded) {
    if (!upgraded) {
      if (BASE_PREVIEW_CARD == null) {
        BASE_PREVIEW_CARD = new SafetyTag();
      }
      return BASE_PREVIEW_CARD;
    } else {
      if (UPGRADED_PREVIEW_CARD == null) {
        UPGRADED_PREVIEW_CARD = new SafetyTag();
        UPGRADED_PREVIEW_CARD.upgrade();
      }
      return UPGRADED_PREVIEW_CARD;
    }
  }

  public static String getDescription(String otherCardName) {
    return DESCRIPTION + otherCardName + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    BASE_PREVIEW_CARD = new SafetyTag();
    UPGRADED_PREVIEW_CARD = new SafetyTag();
    UPGRADED_PREVIEW_CARD.upgrade();
  }
}