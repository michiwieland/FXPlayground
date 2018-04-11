package playground;

import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Region;

/**
 * Auto-scales its content according to the available space of the Pane.
 * The content is always centered
 * <p>
 * By extending the Pane to Region we restrict the access to the
 * children list to protected, which prevents users from modifying it.
 *
 * @author mwieland
 */
public class AutoScalePane extends Region {

    private static final double MAX_SCALE = 1.5;
    private Group content = new Group();

    public AutoScalePane() {
        // avoid enforcing the size by content
        content.setManaged(false);
        getChildren().add(content);
    }

    /**
     * Adds nodes to the AutoScalePane
     *
     * @param children nodes
     */
    public void addChildren(Node... children) {
        content.getChildren().addAll(children);
        requestLayout();
    }

    @Override
    protected void layoutChildren() {

        final double paneWidth = getWidth();
        final double paneHeight = getHeight();
        final double insetTop = getInsets().getTop();
        final double insetRight = getInsets().getRight();
        final double insetLeft = getInsets().getLeft();
        final double insertBottom = getInsets().getBottom();

        final double contentWidth = (paneWidth - insetLeft - insetRight);
        final double contentHeight = (paneHeight - insetTop - insertBottom);

        // zoom
        final Bounds groupBounds = content.getBoundsInLocal();
        double factorX = contentWidth / groupBounds.getWidth();
        double factorY = contentHeight / groupBounds.getHeight();
        double factor = Math.min(factorX, factorY);
        factor = Math.min(factor, MAX_SCALE);
        content.setScaleX(factor);
        content.setScaleY(factor);

        layoutInArea(content, insetLeft, insetTop, contentWidth, contentHeight,
                getBaselineOffset(), HPos.CENTER, VPos.CENTER);

    }
}
