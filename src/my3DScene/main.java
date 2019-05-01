/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my3DScene;

import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

/**
 *
 * @author hk_th
 */
public class main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @Override
    public void start(Stage primaryStage) {

//        
        Image sky = new Image("sky.jpg");
        PhongMaterial skym = new PhongMaterial();
        skym.setDiffuseMap(sky);
        Sphere sp = new Sphere(2000);
        sp.setCullFace(CullFace.FRONT);
        sp.setMaterial(skym);
        sp.setRotationAxis(Rotate.Y_AXIS);
        
        
        Sphere galaxy = new Sphere(15000);
        PhongMaterial galaxytex = new PhongMaterial();
        galaxytex.setDiffuseMap(new Image(getClass().getResourceAsStream("/galaxy2.jpg")));
        galaxy.setMaterial(galaxytex);
        galaxy.setCullFace(CullFace.FRONT);
        
        
        Sphere earth = new Sphere(4000);
        earth = prepareEarth(earth);
        
        SmartGroup group = new SmartGroup();
        Group floor = new Group();
        Group trees = new Group();
        Group lighthouse = new Group();
        Group sign = new Group();

        floor = makeFloor(floor);
        floor.getChildren().addAll(prepareLightSource());

        trees = makeTrees(trees);

        lighthouse = makeLighthouse(lighthouse);
        
        sign = makeSign(sign);

        group.getChildren().addAll(floor, trees, lighthouse, sp, sign , earth , galaxy);
        group.depthTestProperty().set(DepthTest.ENABLE);
        myCamera camera = new myCamera();

        camera.setFarClip(10000);
        camera.setNearClip(0.01);
        camera.setTranslateZ(400);
        camera.setTranslateY(-100);
        camera.setTranslateX(200);

        Scene scene = new Scene(group, WIDTH, HEIGHT, true);
        scene.setFill(Color.SILVER);
        scene.setCamera(camera);
        

        group.translateXProperty().set(WIDTH / 2);
        group.translateYProperty().set(HEIGHT / 2);
        group.translateZProperty().set(-600);

        initMouseControl(group, scene, primaryStage);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    //group.translateZProperty().set(group.getTranslateZ() + 100);
                    camera.moveForward();
                    break;
                case DOWN:
                    camera.moveBack();
                    break;
                case LEFT:
                    camera.slideLeft();
                    break;
                case RIGHT:
                    camera.slideRight();
                    break;
                case PAGE_DOWN:
                    camera.flyDown();
                    break;
                case PAGE_UP:
                    camera.flyUp();
                    break;
                case S:
                    camera.rotateByX(5);
                    break;
                case W:
                    camera.rotateByX(-5);
                    break;
                case A:
                    camera.rotateByY(5);
                    break;
                case D:
                    camera.rotateByY(-5);
                    break;
                case K:

                default:
                    System.out.println(event.getCode());
            }
        });

        primaryStage.setTitle("my 3D Scene");
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareAnimation(sp);
        prepareAnimation(earth);
        
    }

    private void initMouseControl(SmartGroup group, Scene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        scene.setOnMouseClicked(event -> {
            System.out.println(scene.getCamera().getTranslateX());
            System.out.println(scene.getCamera().getTranslateY());
            System.out.println(scene.getCamera().getTranslateZ());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() + delta);
        });
    }

    private void prepareAnimation(Sphere sp) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                sp.rotateProperty().set(sp.getRotate() + 0.02);
            }
        };
        timer.start();
    }
    
    //-----------------------------floor------------------------------

    private Group makeFloor(Group group) {

        PhongMaterial land = new PhongMaterial();
        land.setDiffuseMap(new Image(getClass().getResourceAsStream("/wood.jpg")));

        PhongMaterial water = new PhongMaterial();
        water.setDiffuseMap(new Image(getClass().getResourceAsStream("/water.jpg")));
        water.setSpecularMap(new Image(getClass().getResourceAsStream("/waterspec.jpg")));

        //------------------------------ land---------------------------------------------
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {

                Box r = new Box(80, 40, 150);
                r.translateXProperty().set(0 + i * 80);
                r.translateYProperty().set(0);
                r.translateZProperty().set(0 + j * 150);
                r.setMaterial(land);
                r.setDepthTest(DepthTest.ENABLE);
                r.drawModeProperty().set(DrawMode.FILL);
                group.getChildren().add(r);

            }
        }

        //---------------------------- water ----------------------------------------------
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {

                Box r = new Box(80, 40, 150);
                r.translateXProperty().set(400 + i * 80);
                r.translateYProperty().set(0);
                r.translateZProperty().set(0 + j * 150);
                r.setMaterial(water);
                r.setDepthTest(DepthTest.ENABLE);
                r.drawModeProperty().set(DrawMode.FILL);
                group.getChildren().add(r);

            }
        }

        return group;
    }

    // -----------------------------light -------------------------
    private Node[] prepareLightSource() {
        PointLight pointLight = new PointLight();
        pointLight.setColor(Color.WHITE);

        pointLight.getTransforms().add(new Translate(700, -900, 1600));

        AmbientLight ambientLight = new AmbientLight();

        PhongMaterial sun = new PhongMaterial();
        sun.setDiffuseMap(new Image(getClass().getResourceAsStream("/sun.jpg")));
        Sphere sphere = new Sphere(100);
        sphere.setMaterial(sun);
        sphere.setOpacity(0.5);
        sphere.getTransforms().setAll(pointLight.getTransforms());
        return new Node[]{pointLight, sphere, ambientLight};
    }

    //-----------------------------------trees--------------------------------------
    private Group makeTrees(Group group) {
        Random rand = new Random();
        PhongMaterial trunktex = new PhongMaterial();
        trunktex.setDiffuseMap(new Image(getClass().getResourceAsStream("/trunk.jpg")));

        PhongMaterial leavestex = new PhongMaterial();
        leavestex.setDiffuseMap(new Image(getClass().getResourceAsStream("/leaves.jpg")));
        for (int i = 0; i < 15; i++) {

            Cylinder trunk = new Cylinder(12, 150);
            Sphere leaves = new Sphere(rand.nextInt(55) + 50);

            trunk.setTranslateY(-80);
            leaves.setTranslateY(-150);

            trunk.translateZProperty().set(0 + i * 100);
            leaves.translateZProperty().set(0 + i * 100);

            trunk.setMaterial(trunktex);
            leaves.setMaterial(leavestex);
            group.getChildren().addAll(trunk, leaves);
        }
        return group;
    }

    //--------------------------lighthouse-------------------------------------------
    
    private Group makeLighthouse(Group group) {
        PhongMaterial walltex = new PhongMaterial();
        walltex.setDiffuseMap(new Image(getClass().getResourceAsStream("/wall.jpg")));
        PhongMaterial toptex = new PhongMaterial();
        toptex.setDiffuseMap(new Image(getClass().getResourceAsStream("/top.jpg")));
        int y = 0;
        for (int i = 0; i < 16; i+=2) {

            Cylinder wall = new Cylinder(100-i, 150);
            if (i == 14) {
                
                wall.setMaterial(toptex);
            }else{
            wall.setMaterial(walltex);}
            //wall.getTransforms().addAll(elements)
            wall.setTranslateY(-100+y);
            wall.setTranslateX(200);
            wall.setTranslateZ(800);
            y = (int)wall.getTranslateY();
            System.out.println(wall.getTranslateY());
            group.getChildren().addAll(wall);
            
        }
        return group;
    }
    
    private Group makeSign(Group group){
        PhongMaterial planktex = new PhongMaterial();
        planktex.setDiffuseMap(new Image(getClass().getResourceAsStream("/plank.jpg")));
        PhongMaterial signtex = new PhongMaterial();
        signtex.setDiffuseMap(new Image(getClass().getResourceAsStream("/sign.jpg")));
        Box r = new Box(10, 80, 5);
        Box s = new Box(80, 40, 5);
        r.setTranslateY(-50);
        r.setTranslateX(200);
        r.setTranslateZ(20);
        s.setTranslateY(-100);
        s.setTranslateX(200);
        s.setTranslateZ(20);
        r.setMaterial(planktex);
        s.setMaterial(signtex);
        
        
        group.getChildren().addAll(r,s);
        return group;
    }
    
    //--------------------------------earth -----------------------------------------
    
    private Sphere prepareEarth(Sphere sphere) {
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("/earth-d.jpg")));
    earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("/earth-l.jpg")));
    earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("/earth-s.jpg")));
    earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("/earth-n.jpg")));

    sphere.setRotationAxis(Rotate.Y_AXIS);
    sphere.setMaterial(earthMaterial);
    return sphere;
  }

    public static void main(String[] args) {
        launch(args);
    }
}
