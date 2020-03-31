package thewrestler.ui.trademarkmovepanel;

import thewrestler.cards.EndOfCombatListener;
import thewrestler.cards.StartOfCombatListener;
import thewrestler.ui.CardPreviewElement;
import thewrestler.ui.CustomInfoPanel;

public interface WrestlerSignatureMovePanelInterface
    extends CustomInfoPanel, CardPreviewElement, StartOfCombatListener, EndOfCombatListener  {
}