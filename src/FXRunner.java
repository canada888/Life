import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FXRunner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int GRID_WIDTH = 12;
    private static final int GRID_HEIGHT = 12;
    private static final int GRID_DEPTH = 10;

    private LifeGrid lg;
    private final FlowPane buttons;
    private final Group group;
    private Point3D cursor;

    public FXRunner() {
        super();
        lg = new LifeGrid(GRID_WIDTH, GRID_HEIGHT, GRID_DEPTH);

        buttons = new FlowPane();
        group = new Group();
        cursor = new Point3D(0,0,0);
    }



    private void addButton(String text, EventHandler<ActionEvent> e){
        Button b = new Button(text);
        b.setOnAction(e);
        buttons.getChildren().add(b);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Game of Life");




        Slider slider = new Slider(.5,20,3);


        Animation timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), e ->{
                    lg.next();
                    update();
                })
        );
        timeline.setCycleCount(Animation.INDEFINITE);

        timeline.rateProperty().bind(slider.valueProperty());



        addButton("Start",e -> timeline.play());
        addButton("Stop",e -> timeline.stop());
        addButton("Next",e -> {
            lg.next();
            update();
        });
        addButton("Undo",e -> {
            lg.prev();
            update();
        });
        addButton("Clear",e -> {
            lg.clearBoard(); update();
        });

        final int bSize = 20;


        for(int i = 0; i < lg.rows(); i++) {
            for (int j = 0; j < lg.cols(); j++) {
                for (int k = 0; k < lg.depth(); k++) {
                    Box b = new Box(bSize,bSize,bSize);
                    b.translateXProperty().setValue(i * (bSize + 6));
                    b.translateYProperty().setValue(j * (bSize + 6));
                    b.translateZProperty().setValue(k * (bSize + 6));

                    group.getChildren().add(b);
                }
            }
        }

        update();


        buttons.setAlignment(Pos.CENTER);
        buttons.setOrientation(Orientation.HORIZONTAL);
        buttons.setHgap(10);


        SubScene content = new SubScene(group, WIDTH, HEIGHT);

        Camera cam = new PerspectiveCamera();
        content.setCamera(cam);
        content.setFill(Color.BISQUE);

        VBox root = new VBox(20,content, buttons,slider);

        root.setAlignment(Pos.CENTER);
        root.setFillWidth(false);

        group.translateXProperty().setValue(content.getWidth()/2 - group.getBoundsInParent().getWidth()/2);
        group.translateYProperty().setValue(content.getHeight()/2 - group.getBoundsInParent().getHeight()/2);



        Scene scene = new Scene(root, WIDTH, HEIGHT + 100);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED,e -> {
            switch (e.getCode())
            {
                case A:
                    group.setRotationAxis(Rotate.Y_AXIS);
                    group.setRotate(group.getRotate() + 5);
                    break;
                case D:
                    group.setRotationAxis(Rotate.Y_AXIS);
                    group.setRotate(group.getRotate() - 5);
                    break;
                case W:
                    group.setRotationAxis(Rotate.X_AXIS);
                    group.setRotate(group.getRotate() - 5);
                    break;
                case S:
                    group.setRotationAxis(Rotate.X_AXIS);
                    group.setRotate(group.getRotate() + 5);
                    break;
                case I:
                    cursor = new Point3D(cursor.getX(), (cursor.getY() + GRID_HEIGHT - 1) % GRID_HEIGHT, cursor.getZ());
                    break;
                case K:
                    cursor = new Point3D(cursor.getX(), (cursor.getY() + 1) % GRID_HEIGHT, cursor.getZ());
                    break;
                case J:
                    cursor = new Point3D((cursor.getX() + GRID_WIDTH - 1) % GRID_WIDTH, cursor.getY(), cursor.getZ());
                    break;
                case L:
                    cursor = new Point3D((cursor.getX() + 1) % GRID_WIDTH, cursor.getY(), cursor.getZ());
                    break;
                case U:
                    cursor = new Point3D(cursor.getX(), cursor.getY(), (cursor.getZ() + 1) % GRID_DEPTH);
                    break;
                case O:
                    cursor = new Point3D(cursor.getX(), cursor.getY(), (cursor.getZ() + GRID_DEPTH - 1) % GRID_DEPTH);
                    break;
                case P:
                    lg.change((int)cursor.getX(),(int)cursor.getY(),(int)cursor.getZ());
                    break;
            }
            update();
        });

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void update() {
        int count = 0;
        for(int i = 0; i < lg.rows(); i++) {
            for (int j = 0; j < lg.cols(); j++) {
                for (int k = 0; k < lg.depth(); k++) {

                    PhongMaterial mat = new PhongMaterial();
                    if(i == cursor.getX() && j == cursor.getY() && k == cursor.getZ()){
                        mat.setDiffuseColor(Color.rgb(54,185,255,.8));
                    } else if(lg.get(i,j,k)) {
                        mat.setDiffuseColor(Color.rgb(255,0,255,.8));
                    } else {
                        mat.setDiffuseColor(Color.rgb(0,0,0,0.05));
                    }
                    ((Box)(group.getChildren().get(count++))).setMaterial(mat);
                }
            }
        }
    }
}
