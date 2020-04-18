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

  public AbstractPenaltyStatusCard(String id, String name, int cost, String imgPath, String panelImgKey, String description,
                                   String tooltipKeywordKey) {
    super(id, name, imgPath, cost, description, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.panelPreviewImgPath = getInfoPanelPreviewImgPath(panelImgKey);
    this.panelWarningImgPath = getInfoPanelPreviewWarningImgPath(panelImgKey);
    this.keyword = CustomTooltipKeywords.getTooltipKeyword(tooltipKeywordKey);
    this.tags.add(WrestlerCardTags.PENALTY);
    this.selfRetain = true;
    this.exhaust = true;
  }

  public AbstractPenaltyStatusCard(String id, String name, String imgPath, String panelImgKey, String description,
                                   String tooltipKeywordKey) {
    this(id, name, 2, imgPath, panelImgKey, description, tooltipKeywordKey);
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

  public abstract void triggerOnCardGained();
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