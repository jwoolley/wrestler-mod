package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.blue.Buffer;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import thewrestler.actions.cards.attack.TornadoTagAction;
import thewrestler.cards.AbstractCardWithPreviewCard;
import thewrestler.cards.skill.EyePoke;
import thewrestler.cards.skill.HotTag;
import thewrestler.effects.utils.VFXActionTemplate;
import thewrestler.enums.AbstractCardEnum;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class TornadoTag extends AbstractCardWithPreviewCard {
  public static final String ID = "WrestlerMod:TornadoTag";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "tornadotag.png";

  private static final int DAMAGE = 6;

  private static final int NUM_ATTACKS = 3;
  private static final int NUM_ATTACKS_UPGRADE  = 1;

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.UNCOMMON;
  private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

  private static final int COST = 1;

  public TornadoTag() {
    super(ID, NAME, getCardResourcePath(IMG_PATH), COST, getDescription(HotTag.NAME), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
        RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = NUM_ATTACKS;
  }

  @Override
  public AbstractCard getPreviewCard() {
    return getOtherCard();
  }

  private AbstractCard getOtherCard() {
    AbstractCard otherCard = getBasePreviewCard().makeCopy();
    if (this.upgraded) {
      otherCard.upgrade();
    }
    return otherCard;
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

    AbstractDungeon.getCurrRoom().souls.onToBottomOfDeck(this.getOtherCard());
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
      this.rawDescription = getDescription(this.getOtherCard().name);
      initializeDescription();
    }
  }

  public static String getDescription(String otherCardName) {
    return DESCRIPTION + otherCardName + EXTENDED_DESCRIPTION[0];
  }

  private static final AbstractCard getBasePreviewCard() { return new HotTag(); }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}