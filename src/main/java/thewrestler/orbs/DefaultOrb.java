package thewrestler.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;
import thewrestler.WrestlerMod;
import thewrestler.util.TextureLoader;
import thewrestler.util.info.penaltycard.PenaltyCardInfo;

import static thewrestler.WrestlerMod.makeOrbPath;

public class DefaultOrb extends AbstractOrb {

    // Standard ID/Description
    public static final String ORB_ID = WrestlerMod.makeID("DefaultOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;

    private static final Texture IMG = TextureLoader.getTexture(makeOrbPath("bluecardorb2.png"));
    // Animation Rendering Numbers - You can leave these at default, or play around with them and see what they change.
    private float vfxTimer = 1.0f;
    private float vfxIntervalMin = 0.1f;
    private float vfxIntervalMax = 0.4f;
    private static final float ORB_WAVY_DIST = 0.04f;
    private static final float PI_4 = 12.566371f;

    public DefaultOrb() {

        ID = ORB_ID;
        name = orbString.NAME;
        img = IMG;

        evokeAmount = baseEvokeAmount = 1;
        passiveAmount = basePassiveAmount = 3;

        updateDescription();

        angle = MathUtils.random(5.0f); // More Animation-related Numbers
        channelAnimTimer = 0.5f;
    }

    @Override
    public void updateDescription() { // Set the on-hover description of the orb
        applyFocus(); // Apply Focus (Look at the next method)
        description = DESC[0] + evokeAmount + DESC[1] + passiveAmount + DESC[2]; // Set the description
    }

    @Override
    public void applyFocus() {
        passiveAmount = basePassiveAmount;
        evokeAmount = baseEvokeAmount;
    }

    @Override
    public void onEvoke() { // 1.On Orb Evoke

    }

    @Override
    public void onStartOfTurn() {

    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        vfxTimer -= Gdx.graphics.getDeltaTime();
        if (vfxTimer < 0.0f) {
            vfxTimer = MathUtils.random(vfxIntervalMin, vfxIntervalMax);
        }
    }

    // Render the orb.
    private boolean hadWarningCard = false;
    private boolean alphaWaning = false;
    private final float ORB_ALPHA_MAX = 1.5f;
    private final float ORB_ALPHA_MIN = 1.0f;
    private final float ORB_ALPHA_DELTA = 0.01f;
    private final float ORB_ALPHA_NO_WARNING = 2.0f;
    private float orbAlphaModifier = ORB_ALPHA_MAX;

    @Override
    public void render(SpriteBatch sb) {

        if (PenaltyCardInfo.hasWarningCard()) {
            if (!hadWarningCard) {
                orbAlphaModifier = ORB_ALPHA_MAX;
                hadWarningCard = true;
            }
            sb.setColor(new Color(1.0f, 1.0f, 1.0f, c.a /orbAlphaModifier));

            if (alphaWaning) {
                orbAlphaModifier -= ORB_ALPHA_DELTA;
                if (orbAlphaModifier <= ORB_ALPHA_MIN) {
                    alphaWaning = false;
                }
            } else {
                orbAlphaModifier += ORB_ALPHA_DELTA;
                if (orbAlphaModifier >= ORB_ALPHA_MAX) {
                    alphaWaning = true;
                }
            }
        } else {
            sb.setColor(new Color(0.75f, 0.75f, 0.75f, c.a / ORB_ALPHA_NO_WARNING));
            hadWarningCard = false;
        }

        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, scale, angle, 0, 0, 96, 96, false, false);

        //        sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.c.a / 2.0f));
//        sb.setBlendFunction(770, 1);
//        sb.draw(img, cX - 48.0f, cY - 48.0f + bobEffect.y, 48.0f, 48.0f, 96.0f, 96.0f, scale, scale + MathUtils.sin(angle / PI_4) * ORB_WAVY_DIST * Settings.scale, -angle, 0, 0, 96, 96, false, false);
//        sb.setBlendFunction(770, 771);
//        renderText(sb);
        hb.render(sb);
    }


    @Override
    public void triggerEvokeAnimation() { // The evoke animation of this orb is the dark-orb activation effect.
        AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(cX, cY));
    }

    @Override
    public void playChannelSFX() { // When you channel this orb, the ATTACK_FIRE effect plays ("Fwoom").
        CardCrawlGame.sound.play("ATTACK_FIRE", 0.1f);
    }

    @Override
    public AbstractOrb makeCopy() {
        return new DefaultOrb();
    }
}
