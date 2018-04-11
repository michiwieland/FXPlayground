package playground;

import javafx.beans.Observable;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.StrokeType;

/**
 * Generic component for an labeled edge with optional arrows.
 * <p>
 * Can be used to display the relation between two nodes.
 *
 * @author mwieland
 */
public class LabeledEdge extends Group {

    private final Node startNode;
    private final Node endNode;

    private final CubicCurve curve = new CubicCurve();
    private final Label label = new Label();

    private final ObjectProperty<Point2D> startCenter = new
            SimpleObjectProperty<>();
    private final ObjectProperty<Point2D> endCenter = new
            SimpleObjectProperty<>();

    public LabeledEdge(String labelText, Node startNode, Node endNode,
                       boolean directed) {

        this.startNode = startNode;
        this.endNode = endNode;

        // bind listener
        startCenter.addListener(this::updateCurvePoints);
        endCenter.addListener(this::updateCurvePoints);

        if (startNode.getBoundsInParent().getHeight() > 0 &&
                startNode.getBoundsInParent().getWidth() > 0 &&
                endNode.getBoundsInParent().getHeight() > 0 &&
                endNode.getBoundsInParent().getWidth() > 0) {

            computeEndCenterPoint(null);
            computeStartCenterPoint(null);

        } else {
            startNode.layoutBoundsProperty()
                    .addListener(this::computeStartCenterPoint);
            endNode.layoutBoundsProperty()
                    .addListener(this::computeEndCenterPoint);
        }

        initializeComponent(labelText, directed);
    }

    public void initializeComponent(String labelText, boolean directed) {
        applyStyle();
        drawLabel(labelText);
        if (directed) {
            drawArrows();
        }

        getChildren().addAll(curve, label);
    }


    private void applyStyle() {
        curve.setStrokeWidth(1);
        curve.setStroke(Color.PURPLE);
        curve.setFill(Color.TRANSPARENT);
        curve.setStrokeType(StrokeType.CENTERED);
    }

    /**
     * Draws a centered label
     *
     * @param labelText text
     */
    private void drawLabel(String labelText) {
        Binding xProperty = Bindings.createDoubleBinding(() ->
                        curve.getStartX() + curve.getEndX() / 2,
                curve.startXProperty(), curve.endXProperty());

        Binding yProperty = Bindings.createDoubleBinding(() ->
                        curve.getStartY() + curve.getEndY() / 2,
                curve.startYProperty(), curve.endYProperty());

        label.setText(labelText);
        label.layoutXProperty().bind(xProperty);
        label.layoutYProperty().bind(yProperty);
    }

    private void computeStartCenterPoint(Observable o) {
        if (startNode.getBoundsInParent().getHeight() > 0 &&
                startNode.getBoundsInParent().getWidth() > 0) {

            Point2D center = computeCenter(startNode);
            startCenter.set(center);
        }
    }


    private void computeEndCenterPoint(Observable o) {
        if (endNode.getBoundsInParent().getHeight() > 0 &&
                endNode.getBoundsInParent().getWidth() > 0) {

            Point2D center = computeCenter(endNode);
            endCenter.set(center);
        }
    }

    private Point2D computeCenter(Node node) {
        double centerX = (node.getBoundsInParent().getMinX() +
                node.getBoundsInParent().getMaxX()) / 2;
        double centerY = (node.getBoundsInParent().getMinY() +
                node.getBoundsInParent().getMaxY()) / 2;
        return new Point2D(centerX, centerY);
    }

    private void updateCurvePoints(Observable o) {
        if (startCenter.get() != null && endCenter.get() != null) {

            Point2D startIntersection = findIntersectionPoint(startNode,
                    startCenter.get(),
                    endCenter.get());

            Point2D endIntersection = findIntersectionPoint(endNode,
                    endCenter.get(),
                    startCenter.get());

            Point2D mid = startIntersection.midpoint(endIntersection);

            curve.setStartX(startIntersection.getX());
            curve.setStartY(startIntersection.getY());
            curve.setEndX(endIntersection.getX());
            curve.setEndY(endIntersection.getY());
            curve.setControlX1(mid.getX());
            curve.setControlY1(mid.getY() + 2 * startNode.getLayoutBounds()
                    .getHeight());
            curve.setControlX2(mid.getX());
            curve.setControlY2(mid.getY() + 2 * startNode.getLayoutBounds()
                    .getHeight());
        }
    }

    /**
     * Determines the intersection of the curve and the target node.
     *
     * @param targetBounds target node
     * @param outside      point outside the target node
     * @param inside       point inside the target node
     * @return intersection point
     */
    private Point2D findIntersectionPoint(Node targetBounds,
                                          Point2D inside,
                                          Point2D outside) {

        Point2D middle = outside.midpoint(inside);

        double deltaX = outside.getX() - inside.getX();
        double deltaY = outside.getY() - inside.getY();

        if (Math.hypot(deltaX, deltaY) < 1.) {
            return middle;
        } else {
            if (targetBounds.contains(middle)) {
                return findIntersectionPoint(targetBounds, middle, outside);
            } else {
                return findIntersectionPoint(targetBounds, inside, middle);
            }
        }
    }

    private void drawArrows() {
        // TODO: draw arrow
    }
}
