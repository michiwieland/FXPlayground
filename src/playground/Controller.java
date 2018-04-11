package playground;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.util.stream.Stream;

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
        Circle circle1 = new Circle();
        Circle circle2 = new Circle();
        Circle circle3 = new Circle();
        Circle circle4 = new Circle();

        Stream.of(circle1, circle2, circle3, circle4).forEach(c -> c
                .setRadius(10));

        LabeledEdge relation = new LabeledEdge("A", circle1, circle2,
                true);
        LabeledEdge relation2 = new LabeledEdge("B", circle2, circle4,
                true);

        HBox container = new HBox();
        container.getChildren().addAll(circle1, circle2, circle3, circle4);

        scalePaneLeft.addChildren(container, relation, relation2);

    }

    private void fillRightContent() {
        Circle circle1 = new Circle(100, 200, 20);
        Circle circle2 = new Circle(150, 200, 20);
        Circle circle3 = new Circle(200, 200, 20);
        Circle circle4 = new Circle(250, 200, 20);

        scalePaneRight.addChildren(circle1, circle2, circle3, circle4);
    }
}
