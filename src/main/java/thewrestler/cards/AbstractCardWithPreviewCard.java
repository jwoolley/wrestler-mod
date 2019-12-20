package thewrestler.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

abstract public class AbstractCardWithPreviewCard extends CustomCard {
  private MultiLock renderPreviewCard;
  public AbstractCardWithPreviewCard(String id, String name, String img, int cost, String rawDescription, CardType type,
                                     CardColor color, CardRarity rarity, CardTarget target) {
    super(id, name, img, cost, rawDescription, type, color, rarity, target);
    renderPreviewCard = MultiLock.ZERO;
  }

  abstract public AbstractCard getPreviewCard();

  public void renderCardTip(SpriteBatch sb) {
    super.renderCardTip(sb);

    // TODO: render cards in compendium (done), shop (untested), rewards screen (untested), event (untested), ...?
    // TODO: reposition if card is offscreen (most likely off right side?)

    if (shouldRenderPreviewCard()) {
      final AbstractCard _previewCard = getPreviewCard();
      final float cardScale = this.drawScale * Settings.scale;
      if (_previewCard != null) {
        _previewCard.current_x = _previewCard.hb.x = (this.hb.x - this.hb.width * .44f) * cardScale;
        _previewCard.current_y = _previewCard.hb.y = (this.hb.y + this.hb.height * 0.64f) * cardScale;
        _previewCard.render(sb);
      }
    }
  }



  @Override
  public void hover() {
    super.hover();
    if (renderPreviewCard != MultiLock.TWO) {
      renderPreviewCard = MultiLock.values()[renderPreviewCard.ordinal() + 1];
    }
  }

  @Override
  public void unhover() {
    if (renderPreviewCard != MultiLock.ZERO) {
      renderPreviewCard = MultiLock.values()[renderPreviewCard.ordinal() - 1];
    }
    super.unhover();
  }

  @Override
  public void updateHoverLogic() {
    super.updateHoverLogic();

    if (Settings.hideCards) {
      renderPreviewCard = MultiLock.ZERO;
    }
  }

  enum MultiLock {
    ZERO, ONE, TWO;
  }

  private boolean shouldRenderPreviewCard() {
    return this.renderPreviewCard == MultiLock.TWO
        && (AbstractDungeon.player == null
          || (this.isHoveredInHand(Settings.scale) && !AbstractDungeon.player.isDraggingCard));
  }

  @Override
  public void untip() {
    super.untip();
    renderPreviewCard = MultiLock.ZERO;
  }
}
