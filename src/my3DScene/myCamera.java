/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my3DScene;

import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

/**
 *
 * @author hk_th
 */
public class myCamera extends PerspectiveCamera {
    
    Rotate r;
    Transform t = new Rotate();
    
    Translate pivot = new Translate();
    Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    
   
    
    public void slideRight(){
        
    super.translateXProperty().set(super.getTranslateX() + 50); }
    
    public void slideLeft(){
        
   super.translateXProperty().set(super.getTranslateX() - 50);    }
   
    public void flyUp(){
        
    super.translateYProperty().set(super.getTranslateY() - 50);    }
    
    public void flyDown(){
        
    super.translateYProperty().set(super.getTranslateY() + 50);    }
    
    public void moveBack(){
        
    super.translateZProperty().set(super.getTranslateZ() - 100);    }
    
    public void moveForward(){
        
    super.translateZProperty().set(super.getTranslateZ() + 50);
    super.setRotationAxis(Rotate.X_AXIS);
    }
  

    void rotateByX(int ang) {
      r = new Rotate(ang, Rotate.X_AXIS);
      t = t.createConcatenation(r);
      super.getTransforms().clear();
      super.getTransforms().addAll(t);
    }

    void rotateByY(int ang) {
      r = new Rotate(ang, Rotate.Y_AXIS);
      t = t.createConcatenation(r);
      super.getTransforms().clear();
      super.getTransforms().addAll(t);
    }
//    public void flyUp(){
//        
//    super.setRotationAxis(Rotate.X_AXIS);
//    super.rotateProperty().se
//    }
}
