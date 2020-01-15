package thewrestler.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import thewrestler.enums.AbstractCardEnum;
import thewrestler.powers.SprainPower;
import thewrestler.util.CreatureUtils;

import java.util.Arrays;

import static thewrestler.WrestlerMod.getCardResourcePath;

public class Powerbomb extends CustomCard {
    public static final String ID = "WrestlerMod:Powerbomb";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = getCardResourcePath("powerbomb.png");

    private static final CardStrings cardStrings;

    private static final AbstractCard.CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int COST = 3;

    private static final int DAMAGE = 20;
    private static final int DAMAGE_UPGRADE = 8;
    private static final int DEBUFF_AMOUNT_1 = 2;
    private static final int DEBUFF_AMOUNT_2 = 5;
    private static final int DEBUFF_AMOUNT_1_UPGRADE = 1;
    private static final int DEBUFF_AMOUNT_2_UPGRADE = 5;

    // TODO: make dynamic variable for sprained amount (so it will be highlighted on card updates).
    private final int baseSprainedAmount;
    private int sprainedAmount;


    public Powerbomb() {
        super(ID, NAME, IMG_PATH, COST, getDescription(DEBUFF_AMOUNT_2), TYPE, AbstractCardEnum.THE_WRESTLER_ORANGE,
            RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.isMultiDamage = true;
        this.baseMagicNumber = this.magicNumber = DEBUFF_AMOUNT_1;
        this.baseSprainedAmount = this.sprainedAmount = DEBUFF_AMOUNT_2;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f)));
        CardCrawlGame.sound.play("BOMB_DROP_EXPLODE_1");
        AbstractDungeon.actionManager.addToBottom((new WaitAction(0.8F)));

        AbstractDungeon.actionManager.addToBottom(new PowerbombAction(p, this.multiDamage, this.damageType,
            AbstractGameAction.AttackEffect.FIRE, this.magicNumber, this.sprainedAmount, false));

        /*
        PowerbombAction(AbstractPlayer source, int[] multiDamage, DamageInfo.DamageType damageType,
        AttackEffect attackEffect, int numDebuffs, int numSprained, boolean isFast) {
        */

        /*
        CreatureUtils.getLivingMonsters().forEach(mo -> {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(mo, p,
                    new WeakPower(mo, this.magicNumber, false), this.magicNumber));

            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(mo, p,
                    new VulnerablePower(mo, this.magicNumber, false), this.magicNumber));

            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(mo, p,
                    new SprainPower(mo, this.sprainedAmount), this.sprainedAmount));
        });
        */
    }

    public static String getDescription(int sprainedAmount) {
        return DESCRIPTION + sprainedAmount + EXTENDED_DESCRIPTION[0];
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(DAMAGE_UPGRADE);
            upgradeMagicNumber(DEBUFF_AMOUNT_1_UPGRADE);
            this.sprainedAmount += DEBUFF_AMOUNT_2_UPGRADE;
        }
    }

    private static class PowerbombAction extends AbstractGameAction {
        private static final float DURATION_1 = Settings.ACTION_DUR_FASTER;
        private static final float DURATION_2 = 0.6f;
        private final int[] multiDamage;
        private final int numDebuffs;
        private final int numSprained;
        private final boolean isFast;

        PowerbombAction(AbstractPlayer source, int[] multiDamage, DamageInfo.DamageType damageType,
                        AttackEffect attackEffect, int numDebuffs, int numSprained, boolean isFast) {
            this.duration = DURATION_2 + DURATION_1;
            this.actionType = ActionType.DAMAGE;
            this.source = source;
            this.multiDamage = Arrays.copyOf(multiDamage, multiDamage.length);
            this.damageType = damageType;
            this.numDebuffs = numDebuffs;
            this.numSprained = numSprained;
            this.attackEffect = attackEffect;
            this.isFast = isFast;
        }

        @Override
        public void update() {
            if (this.duration <= DURATION_1) {
                AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(
                        this.source, this.multiDamage, this.damageType, this.attackEffect, this.isFast));

                CreatureUtils.getLivingMonsters().forEach(mo -> {

                    AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(mo, this.source,
                            new VulnerablePower(mo, this.numDebuffs, false), this.numDebuffs));

                    AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(mo, this.source,
                            new SprainPower(mo, this.numSprained), this.numSprained));
                });
                this.isDone = true;
            }
            this.tickDuration();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }
}