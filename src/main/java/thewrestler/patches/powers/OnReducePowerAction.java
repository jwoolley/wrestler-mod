package thewrestler.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thewrestler.WrestlerMod;

//@SpirePatch(
//    clz = ReducePowerAction.class,
//    method = SpirePatch.CONSTRUCTOR,
//    paramtypez = {
//        AbstractCreature.class,
//        AbstractCreature.class,
//        AbstractPower.class,
//        int.class
//    }
//)
//
//public class OnReducePowerAction {
//  public static void Prefix(ReducePowerAction __instance, AbstractCreature target, AbstractCreature source,
//                            AbstractPower powerToReduce, int amount) {
//    Logger logger = WrestlerMod.logger;
//    logger.info("OnReducePowerAction::Prefix (powerInstance) called. source: " + source + " target: " + target
//        + " powerToReduce: " + powerToReduce + " amount: " + amount);
//  }
//}