package playground;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class Controller {
    @FXML
    public AutoScalePane scalePaneLeft;

    @FXML
    public AutoScalePane scalePaneRight;

    @FXML
    public void initialize() {
        fillLeftContent();
        fillRightContent();
    }

    private void fillLeftContent() {
        Circle circle1 = new Circle(100, 300, 10);
        Circle circle2 = new Circle(150, 300, 10);
        Circle circle3 = new Circle(200, 300, 10);
        Circle circle4 = new Circle(250, 300, 10);

        LabeledEdge relation = new LabeledEdge("extends", circle1, circle2,
                true);
        LabeledEdge relation2 = new LabeledEdge("extends", circle1, circle3,
                true);

        scalePaneLeft.addChildren(relation, relation2, circle1, circle2,
                circle3, circle4);

    }

    private void fillRightContent() {
        Circle circle1 = new Circle(100, 200, 20);
        Circle circle2 = new Circle(150, 200, 20);
        Circle circle3 = new Circle(200, 200, 20);
        Circle circle4 = new Circle(250, 200, 20);

        scalePaneRight.addChildren(circle1, circle2, circle3, circle4);
    }
}
