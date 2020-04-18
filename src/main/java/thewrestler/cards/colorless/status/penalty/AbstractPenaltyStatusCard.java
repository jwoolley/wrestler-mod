package thewrestler.cards.colorless.status.penalty;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.Collect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.keywords.CustomTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.orbs.BasePenaltyOrb;
import thewrestler.powers.ShortarmPower;
import thewrestler.relics.RefereesWhistle;
import thewrestler.ui.UiHelper;
import thewrestler.util.CardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static thewrestler.WrestlerMod.getCardResourcePath;

public abstract class AbstractPenaltyStatusCard extends CustomCard {
  private static final CardType TYPE = CardType.STATUS;
  private static final CardRarity RARITY = CardRarity.SPECIAL;
  private static final CardTarget TARGET = CardTarget.NONE;

  private static final String PANEL_IMG_DIR_PATH = "penaltycardinfopanel/";
  private static final String PANEL_IMG_PREVIEW_PREFIX = "preview-";
  private static final String PANEL_IMG_WARNING_PREFIX = "warning-";
  private static final String IMG_PATH_PREFIX = "penaltycards/";

  private final CustomTooltipKeyword keyword;

  private final String panelPreviewImgPath;
  private final String panelWarningImgPath;

  public AbstractPenaltyStatusCard(String id, String name, String imgPath, String panelImgKey, String description,
                                   String tooltipKeywordKey) {
    super(id, name, imgPath,2, description, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.panelPreviewImgPath = getInfoPanelPreviewImgPath(panelImgKey);
    this.panelWarningImgPath = getInfoPanelPreviewWarningImgPath(panelImgKey);
    this.keyword = CustomTooltipKeywords.getTooltipKeyword(tooltipKeywordKey);
    this.tags.add(WrestlerCardTags.PENALTY);
    this.selfRetain = true;
    this.exhaust = true;
  }

  public String getInfoPanelNoWarningImagePath() {
    return panelPreviewImgPath;
  }

  public String getInfoPanelWarningImagePath() {
    return panelWarningImgPath;
  }

  public CustomTooltipKeyword getTooltipKeyword() {
    return keyword;
  }

  public abstract void triggerOnEndOfTurn();
  public abstract void triggerOnCardUsed(AbstractPlayer p, AbstractMonster m);

  public void use(AbstractPlayer p, AbstractMonster m) {
      final AbstractPlayer player = AbstractDungeon.player;
      this.triggerOnCardUsed(p, m);
      if (player.hasPower(ShortarmPower.POWER_ID)) {
        player.getPower(ShortarmPower.POWER_ID).flash();
        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(player, player, ShortarmPower.POWER_ID, 1));
        this.triggerOnCardUsed(p, m);
      }
  }

  public static void triggerPenaltyCardsEndOfTurn() {
    final List<AbstractPenaltyStatusCard> cards = new ArrayList<>();

    AbstractDungeon.player.hand.group.stream()
      .filter(c -> c instanceof AbstractPenaltyStatusCard)
      .forEach(c -> cards.add((AbstractPenaltyStatusCard) c));


    if (!cards.isEmpty()) {
      AbstractDungeon.actionManager.addToBottom(new PenaltyCardsEndOfTurnAction(cards));
    }
  }

  private static class PenaltyCardsEndOfTurnAction extends AbstractGameAction {
    private static final float ACTION_DURATION = Settings.ACTION_DUR_FAST;
    private static final float INITIAL_DELAY = Settings.ACTION_DUR_MED;

    private final List<AbstractPenaltyStatusCard> penaltyCards;

    public PenaltyCardsEndOfTurnAction(List<AbstractPenaltyStatusCard> penaltyCards) {
      this(penaltyCards, INITIAL_DELAY);
    }

    public PenaltyCardsEndOfTurnAction(List<AbstractPenaltyStatusCard> penaltyCards, float initialDelay) {
      this.penaltyCards = new ArrayList<>(penaltyCards);
      this.duration = ACTION_DURATION + initialDelay;
      this.actionType = ActionType.USE;
    }

    @Override
    public void update() {
      if (penaltyCards.isEmpty()) {
        this.isDone = true;
        return;
      }
      if (this.duration <= 0.1f) {
        AbstractPenaltyStatusCard card = this.penaltyCards.get(0);
        card.superFlash(card.getFlashColor());
        CardCrawlGame.sound.play("WHISTLE_BLOW_SHORT_1");
        card.triggerOnEndOfTurn();

        if (AbstractDungeon.player.hasRelic(RefereesWhistle.ID)) {
          card.flash(Color.GOLD);
          card.modifyCostForCombat(-1);
        }

        card.superFlash(card.getFlashColor());

        if (penaltyCards.size() > 1) {
          AbstractDungeon.actionManager.addToBottom(
              new PenaltyCardsEndOfTurnAction(penaltyCards.subList(1, penaltyCards.size()), 0.0f));
        }
        this.isDone = true;
      }
      this.tickDuration();
    }
  }

  abstract protected Color getFlashColor();

  public abstract BasePenaltyOrb getOrb();

  static String getPenaltyCardImgPath(String imgFilename) {
    return getCardResourcePath(IMG_PATH_PREFIX + imgFilename);
  }
  static String getInfoPanelPreviewImgPath(String panelImgKey) {
    return UiHelper.getUiImageResourcePath(PANEL_IMG_DIR_PATH + PANEL_IMG_PREVIEW_PREFIX + panelImgKey + ".png");
  }

  static String getInfoPanelPreviewWarningImgPath(String panelImgKey) {
    return UiHelper.getUiImageResourcePath(PANEL_IMG_DIR_PATH + PANEL_IMG_WARNING_PREFIX + panelImgKey + ".png");
  }
}