package test.scenegraph.binding.controls;

/*
 * Copyright (c) 2011, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */


import org.junit.BeforeClass;
import org.junit.Test;
import test.scenegraph.binding.*;
import com.oracle.jdk.sqe.cc.markup.Covers;
import com.oracle.jdk.sqe.cc.markup.Covers.Level;

public class PasswordBoxTest extends BindingTestBase {
    @BeforeClass
    public static void runUI() {
        BindingApp.factory = Factories.PasswordBox;
        BindingApp.main(null);
    }

    public static void main(String[] args) {
        runUI();
    }

    /**
    * This test verifies prefWidth property for controls.PasswordBox
    */
    @Test
    public void prefWidth() {
        commonTest(NumberConstraints.prefWidth);
    }

    /**
    * This test verifies prefHeight property for controls.PasswordBox
    */
    @Test
    public void prefHeight() {
        commonTest(NumberConstraints.prefHeight);
    }

    /**
    * This test verifies layoutX property for controls.PasswordBox
    */
    @Test
    public void layoutX() {
        commonTest(NumberConstraints.layoutX);
    }

    /**
    * This test verifies layoutY property for controls.PasswordBox
    */
    @Test
    public void layoutY() {
        commonTest(NumberConstraints.layoutY);
    }

    /**
    * This test verifies translateX property for controls.PasswordBox
    */
    @Test
    public void translateX() {
        commonTest(NumberConstraints.translateX);
    }

    /**
    * This test verifies translateY property for controls.PasswordBox
    */
    @Test
    public void translateY() {
        commonTest(NumberConstraints.translateY);
    }

    /**
    * This test verifies scaleX property for controls.PasswordBox
    */
    @Test
    public void scaleX() {
        commonTest(NumberConstraints.scaleX);
    }

    /**
    * This test verifies scaleY property for controls.PasswordBox
    */
    @Test
    public void scaleY() {
        commonTest(NumberConstraints.scaleY);
    }

    /**
    * This test verifies rotate property for controls.PasswordBox
    */
    @Test
    public void rotate() {
        commonTest(NumberConstraints.rotate);
    }

    /**
    * This test verifies blendMode property for controls.PasswordBox
    */
    @Test
    public void blendMode() {
        commonTest(ObjectConstraints.blendMode);
    }

    /**
    * This test verifies clip property for controls.PasswordBox
    */
    @Test
    public void clip() {
        commonTest(ObjectConstraints.clip);
    }

    /**
    * This test verifies effect property for controls.PasswordBox
    */
    @Test
    public void effect() {
        commonTest(ObjectConstraints.effect);
    }

    /**
    * This test verifies opacity property for controls.PasswordBox
    */
    @Test
    public void opacity() {
        commonTest(NumberConstraints.opacity);
    }

}
