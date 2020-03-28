package thewrestler.cards.colorless.status.penalty;

import basemod.abstracts.CustomCard;
import thewrestler.cards.WrestlerCardTags;
import thewrestler.keywords.CustomTooltipKeyword;
import thewrestler.keywords.CustomTooltipKeywords;
import thewrestler.ui.UiHelper;

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
    super(id, name, imgPath,1, description, TYPE, CardColor.COLORLESS, RARITY, TARGET);
    this.panelPreviewImgPath = getInfoPanelPreviewImgPath(panelImgKey);
    this.panelWarningImgPath = getInfoPanelPreviewWarningImgPath(panelImgKey);
    this.keyword = CustomTooltipKeywords.getTooltipKeyword(tooltipKeywordKey);
    this.tags.add(WrestlerCardTags.PENALTY);
    this.selfRetain = true;
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