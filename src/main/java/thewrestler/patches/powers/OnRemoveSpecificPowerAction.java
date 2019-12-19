package thewrestler.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;
import thewrestler.relics.Headgear;
//
//@SpirePatch(
//    clz = RemoveSpecificPowerAction.class,
//    method = SpirePatch.CONSTRUCTOR,
//    paramtypez = {
//        AbstractCreature.class,
//        AbstractCreature.class,
//        AbstractPower.class
//    }
//)
//
//public class OnRemoveSpecificPowerAction {
//  public static void Prefix(RemoveSpecificPowerAction __instance, AbstractCreature target, AbstractCreature source,
//                            AbstractPower powerInstance) {
//    Logger logger = WrestlerMod.logger;
//    logger.info("OnRemoveSpecificPowerAction::Prefix (powerInstance) called. source: " + source + " target: " + target
//      + " powerToApply: " + powerInstance);
//  }
//}