/*
 * Copyright (c) 2009, 2012, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation. Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 */
package test.scenegraph.functional.controls.events;

import com.sun.glass.ui.Robot;
import javafx.event.ActionEvent;
import javafx.geometry.Bounds;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import org.jemmy.Point;
import org.jemmy.action.GetAction;
import org.jemmy.control.Wrap;
import org.jemmy.fx.NodeDock;
import org.jemmy.fx.Root;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.LabeledDock;
import org.jemmy.fx.control.TabDock;
import org.jemmy.fx.control.TabPaneDock;
import org.jemmy.interfaces.Keyboard;
import org.jemmy.interfaces.Mouse;
import org.jemmy.lookup.LookupCriteria;
import org.jemmy.timing.State;
import org.junit.Before;
import org.junit.Test;
import test.javaclient.shared.JemmyUtils;
import test.javaclient.shared.TestBase;
import test.javaclient.shared.Utils;
import test.scenegraph.app.ControlEventsApp;
import test.scenegraph.app.ControlEventsApp.Controls;
import test.scenegraph.app.ControlEventsApp.EventTypes;
import test.scenegraph.app.ControlEventsTab;

/**
 *
 * @author Aleksandr Sakharuk, rewritten by Victor Shubov
 * // step1: tests work OK in Windows
 * @param <T>
 */
public abstract class EventTestCommon<T extends NodeDock> extends TestBase
{
    
    @Override
    @Before
    public void before()
    {
        super.before();
        sceneDock = new SceneDock(super.scene);
    }
    
    // * Clicks with right mouse button on node
    @Test(timeout = 20000)
    public void onContextMenuRequested()
    {
        test(EventTypes.CONTEXT_MENU_REQUESTED, new Command() {

            @Override
            public void invoke() {
                final Point clickPoint = primeDock.wrap().getClickPoint();
                primeDock.mouse().click(1, clickPoint, Mouse.MouseButtons.BUTTON3);
                try { Thread.sleep(200);} catch(InterruptedException e){}
                primeDock.mouse().click(1, new Point(clickPoint.x - 2, clickPoint.y), Mouse.MouseButtons.BUTTON1);
            }
        });
    }
    
    Command dragFromControlToTarget = new Command() {
        public void invoke() {
            NodeDock dragTarget = new NodeDock(tabDock.asParent(),
                    ControlEventsApp.DRAG_TARGET_ID);
            dnd(primeDock, dragTarget);
        }
    };
    
    @Test(timeout = 30000)
    public void onDragDetected()
    {
        test(EventTypes.DRAG_DETECTED, dragFromControlToTarget);
    }
    
    // * Drags from node to text field.
    // * DRAG_DONE event comes when drag is finished on text field.
    // * Text in text field will change to node's class name.
    @Test(timeout = 20000)
    public void onDragDone()
    {
        test(EventTypes.DRAG_DONE, dragFromControlToTarget);
    }
    
    
    
    // * Moves mouse onto tested node.
    // * Event should come to tested node.
    @Test(timeout = 30000)
    public void onMouseEntered()
    {
        test(EventTypes.MOUSE_ENTERED, new Command() {

            public void invoke() {
                Bounds bounds = primeDock.getBoundsInLocal();
                double x = - ControlEventsApp.INSETS / 2;
                double y = bounds.getHeight() / 2;
                for(; (x <= bounds.getWidth() / 2) && (!gotEvent()); x++)
                {
                    primeDock.mouse().move(new Point(x, y));
                }
            }
        });
    }
    
    
    // * Moves mouse inside of tested node.
    @Test(timeout = 30000)
    public void onMouseMoved()
    {
        test(EventTypes.MOUSE_MOVED, new Command() {

            public void invoke() {
                final Bounds bounds = primeDock.getBoundsInLocal();
                final double y = bounds.getHeight() / 2;
                for(double x = 0; (x < bounds.getWidth()) && (!gotEvent()); x++)
                {
                    primeDock.mouse().move(new Point(x, y));
                }
            }
        });
    }
    
    @Test(timeout = 30000)
    public void onMouseClicked()
    {
        test(EventTypes.MOUSE_CLICKED, new Command() {

            public void invoke() {
                primeDock.mouse().click();
            }
        });
    }
    
    @Test(timeout = 30000)
    public void onMousePressed()
    {
        test(EventTypes.MOUSE_PRESSED, new Command() {

            public void invoke() {
                primeDock.mouse().move();
                primeDock.mouse().press();
                primeDock.mouse().release();
            }
        });
    }
    
    @Test(timeout = 30000)
    public void onMouseReleased()
    {
        test(EventTypes.MOUSE_RELEASED, new Command() {

            public void invoke() {
                primeDock.mouse().move();
                primeDock.mouse().press();
                primeDock.mouse().release();
            }
        });
    }
    
    // * Drags mouse over tested node starting outside of one.
    @Test(timeout = 30000)
    public void onMouseDragOver()
    {
        test(EventTypes.MOUSE_DRAG_OVER, new Command() {

            public void invoke() {
                Bounds bounds = primeDock.getBoundsInLocal();
                double x = bounds.getWidth() + ControlEventsApp.INSETS / 2;
                double y = bounds.getHeight() / 2;
                primeDock.mouse().move(new Point(x, y));
                primeDock.mouse().press();
                for(;( x >= - ControlEventsApp.INSETS / 2) && (!gotEvent()); x--)
                {
                    primeDock.mouse().move(new Point(x, y));
                }
                primeDock.mouse().release();
            }
        });
    }
    
    
    @Test(timeout = 30000)
    public void onAction()
    {
        if (control.getProcessedEvents().contains(ActionEvent.class)) {
            test(EventTypes.ACTION, new Command() {

                public void invoke() {
                    primeDock.mouse().click();
                }
            });
        }
    }
    
    
    private final Command commandPushKey = new Command() {
            public void invoke() {
                
                final Point p1 = primeDock.wrap().getClickPoint();
                final Mouse m = primeDock.mouse();
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        m.move(p1);
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        m.click();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                try { Thread.sleep(20); } catch (InterruptedException e){}
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        primeDock.keyboard().pushKey(Keyboard.KeyboardButtons.A);
                    }
                }.dispatch(Root.ROOT.getEnvironment());
            }};

    @Test(timeout = 10000)
    public void onKeyPressed()
    {
        if (control.getProcessedEvents().contains(KeyEvent.class)) 
        test(EventTypes.KEY_PRESSED, commandPushKey);
    }
    
    @Test(timeout = 10000)
    public void onKeyRelease()
    {
        if(control.getProcessedEvents().contains(KeyEvent.class))
        test(EventTypes.KEY_RELEASED, commandPushKey);
    }
    
    @Test(timeout = 10000)
    public void onKeyTyped()
    {
        if(control.getProcessedEvents().contains(KeyEvent.class))
        test(EventTypes.KEY_TYPED, commandPushKey);
    }

    @Test(timeout = 8000)
    public void onScroll() {
        if (control.getProcessedEvents().contains(ScrollEvent.class)) {
            test(EventTypes.SCROLL, new Command() {

                public void invoke() {
                    new GetAction<Object>() {
                        @Override
                        public void run(Object... os) throws Exception {
                            primeDock.mouse().move();
                            primeDock.mouse().turnWheel(1);
                        }
                    }.dispatch(Root.ROOT.getEnvironment());
                }
            });
        }
    }
    
    // * Releases drag on tested node. Drag starts outside of node.
    @Test(timeout = 20000)
    public void onMouseDragReleased()
    {
        test(EventTypes.MOUSE_DRAG_RELEASED, new Command() {

            public void invoke() {
                final Point p1 = primeDock.wrap().getClickPoint();
                final double pointInX = p1.x;
                final double pointOutX = p1.x - primeDock.getBoundsInParent().getWidth()/2 -  10;//.getWidth() / 2 - 1;
                
                final Mouse m = primeDock.mouse();
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointOutX, p1.y);  
                        m.move(p1);
                        m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                try {Thread.sleep(50);}catch(InterruptedException e){}
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointOutX+6, p1.y);  
                        m.move(p1);
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                try {Thread.sleep(50);}catch(InterruptedException e){}
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointOutX+8, p1.y);  
                        m.move(p1);
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                try {Thread.sleep(50);}catch(InterruptedException e){}
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointInX, p1.y +1);
                        m.move(p1);
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        m.release();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                
            }
        });
    }
    
    
    
    // * Drags mouse onto tested node.
    // * Event should come to tested node.
    @Test(timeout = 30000)
    public void onMouseDragEntered()
    {
        test(EventTypes.MOUSE_DRAG_ENTERED, new Command() {

            public void invoke() {
                final Point p1 = primeDock.wrap().getClickPoint();
                double offset = p1.x - primeDock.wrap().getScreenBounds().getWidth()/2 -  10;//.getWidth() / 2 - 1;
                p1.setLocation(offset, p1.y);
                final Mouse m = primeDock.mouse();
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                      m.move(p1);
                      m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());

                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(p1.x + 4, p1.y);
                        m.move(p1);
                        m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());

                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(p1.x + 12, p1.y);
                        m.move(p1);
                        m.release();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
            }
        });
    }
    
    
    private final Command commandDnDFromDragSourceToPrimeDock = new Command() {
            public void invoke() {
                final NodeDock dragSource = new NodeDock(tabDock.asParent(), 
                        ControlEventsApp.DRAG_FIELD_ID);
                dnd(dragSource, primeDock);
    
}};
    
    // * Drag from text field to node.
    // * Text in node's tooltip will change to text from text field.
    @Test(timeout = 20000)
    public void onDragDroped()
    {
        test(EventTypes.DRAG_DROPPED, commandDnDFromDragSourceToPrimeDock);
    }

    // * Drag from text field to node.
    @Test(timeout = 30000)
    public void onDragEntered()
    {
        test(EventTypes.DRAG_ENTERED, commandDnDFromDragSourceToPrimeDock);
    }
    
    // * Drag from text field to node.
    @Test(timeout = 30000)
    public void onDragEnteredTarget()
    {
        test(EventTypes.DRAG_ENTERED_TARGET, commandDnDFromDragSourceToPrimeDock);
    }
 
    @Test(timeout = 30000)
    public void onDragOver()
    {
        test(EventTypes.DRAG_OVER, commandDnDFromDragSourceToPrimeDock  );
    }
    

    
    
    
    @Test(timeout = 30000)
    public void onMouseDragExited()
    {
        test(EventTypes.MOUSE_DRAG_EXITED, new Command() {

            public void invoke() {
                final Point p1 = primeDock.wrap().getClickPoint();
                final double pointInX = p1.x;
                final double pointOutX = p1.x - primeDock.wrap().getScreenBounds().getWidth()/2 -  14;//.getWidth() / 2 - 1;
                
                final Mouse m = primeDock.mouse();
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointOutX, p1.y);  
                        m.move(p1);
                        m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointOutX+10, p1.y);  
                        m.move(p1);
                        m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());

                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointInX, p1.y);
                        m.move(p1);
                        m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointInX+8, p1.y);
                        m.move(p1);
                        m.press();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
                
                new GetAction<Object>() {
                    @Override
                    public void run(Object... os) throws Exception {
                        p1.setLocation(pointOutX, p1.y);
                        m.move(p1);
                        m.release();
                    }
                }.dispatch(Root.ROOT.getEnvironment());
            }
        });
    }
    

    // * Moves mouse out of tested node.
    // * Event should come to tested node.
    Command dragControlAndExitCommand = new Command() {

        public void invoke() {

            final Point p1 = primeDock.wrap().getClickPoint();
            final double pointOutX = p1.x - primeDock.wrap().getScreenBounds().getWidth() / 2 - 14;//.getWidth() / 2 - 1;
            
            final Mouse m = primeDock.mouse();
            new GetAction<Object>() {
                @Override
                public void run(Object... os) throws Exception {
                    m.move(new Point(p1.x -4 ,p1.y));
                    m.press();
                }
            }.dispatch(Root.ROOT.getEnvironment());
            try {Thread.sleep(50);}catch(InterruptedException e){}
            new GetAction<Object>() {
                @Override
                public void run(Object... os) throws Exception {
                    m.move(p1);
                 //   m.press();
                }
            }.dispatch(Root.ROOT.getEnvironment());
            try {Thread.sleep(50);}catch(InterruptedException e){}
            new GetAction<Object>() {
                @Override
                public void run(Object... os) throws Exception {
                    m.move(new Point(p1.x+4,p1.y));
                //   m.press();
                }
            }.dispatch(Root.ROOT.getEnvironment());
            try {Thread.sleep(50);}catch(InterruptedException e){}
            new GetAction<Object>() {
                @Override
                public void run(Object... os) throws Exception {
                    p1.setLocation(pointOutX, p1.y);
                    m.move(p1);
                    m.release();
                }
            }.dispatch(Root.ROOT.getEnvironment());
        }
    };
    
    
    
    private final Command dndFromDragSourceToCtrlAndBack = new Command() {
            public void invoke() {
                final NodeDock dragSource = new NodeDock(tabDock.asParent(), 
                        ControlEventsApp.DRAG_FIELD_ID);
                dndd(dragSource, primeDock);
    
}};
    
    // * Drags mouse inside of tested node
    @Test(timeout = 30000)
    public void onMouseDraged()
    {
        test(EventTypes.MOUSE_DRAGGED, dragControlAndExitCommand);
    }
    
    @Test(timeout = 30000)
    public void onDragExited()
    {
        test(EventTypes.DRAG_EXITED, dndFromDragSourceToCtrlAndBack);
    }
    
    @Test(timeout = 30000)
    public void onDragExitedTarget()
    {
        test(EventTypes.DRAG_EXITED_TARGET, dndFromDragSourceToCtrlAndBack);
    }
    
    @Test(timeout = 30000)
    public void onMouseExited()
    {
        test(EventTypes.MOUSE_EXITED, dragControlAndExitCommand);
    }
    

    
    protected void setEventType(EventTypes eventType)
    {
        this.eventType = eventType;
    }
    
    
    protected final void test(EventTypes eventType, Command command) {
        selectTab();
        setEventType(eventType);
        if (selectEventType()) {
            command.invoke();
            waitHandler();
        }
    }

    private void selectTab() 
    {
        final TabPaneDock tabPaneDock = new TabPaneDock(sceneDock.asParent());
        tabDock = new TabDock(tabPaneDock.asTabParent(), new LookupCriteria<Tab>() {

            public boolean check(Tab cntrl) {
                String id = cntrl.getId();
              //  System.out.println(cntrl.getId() + " tab found. Looking for " + control.toString() + ".");
                return id.equals(control.toString());
            }
        });
        new GetAction<Object>() {

            @Override
            public void run(Object... os) throws Exception {
                tabPaneDock.control().getSelectionModel().select(tabDock.control());
            }
        }.dispatch(Root.ROOT.getEnvironment());
        primeDock = findPrimeDock();
    }
    
    protected abstract T findPrimeDock();

    boolean selectEventType() {
        eventRadio = new LabeledDock(tabDock.asParent(), RadioButton.class,
                eventType.toString());
        if (null != eventRadio) {
            
            // Yes, I need retry count here. One click via Jemmy is not enough in real world.
            // testcase: run TextFiledEventTest 8 times.
            // (PS: one click using real hand and real mouse - OK)
            
            int retryCount = 0;
            while ((!((RadioButton)eventRadio.control()).isSelected()) && (retryCount++ < 4 )) {
            new GetAction<Object>() {
                @Override
                public void run(Object... os) throws Exception {
                    eventRadio.mouse().click();
                }
            }.dispatch(Root.ROOT.getEnvironment());

            try { Thread.sleep(20); } catch (InterruptedException e){}
            }
            eventRadio.wrap().waitState(new State<Boolean>() {
            public Boolean reached() {
                return ((RadioButton)eventRadio.control()).isSelected();
            }
            
        }, true);            
            
            return true;
        } else {
            return false;
        }
    }

    
    State targetState = new State<String>() {
            public String reached() {
                return eventRadio.control().getStyle();
            }
        };
    
    boolean gotEvent() {
        return ( ControlEventsTab.HANDLED_STYLE.equals(targetState.reached()));
    }
    
    private void waitHandler() 
    {
        eventRadio.wrap().waitState(targetState, ControlEventsTab.HANDLED_STYLE);
        
        // this "sleep" fixes Linux/onKeyPressed behavior, when
        // application exits before key released
        try { Thread.sleep(50); } catch (InterruptedException e){}

    }
    
    protected void setControl(Controls control)
    {
        this.control = control;
    }
    
    protected T getPrimeNodeDock()
    {
        return primeDock;
    }
    
    protected TabDock getActiveTabDock()
    {
        return tabDock;
    }
    
    // Workaround to make drag'n'drop work
    protected void dnd(final NodeDock _ndFrom, final NodeDock _ndTo) {
        final Wrap from = _ndFrom.wrap();
        final Wrap to = _ndTo.wrap();
      
        dnd(from, to);
    }
    
    
    
    protected void dnd(final Wrap from, final Wrap to) 
    {
        final Point to_point = to.getClickPoint();
        //final Point from_point = from.getClickPoint();
	if(!Utils.isWindows())
	{
	    from.drag().dnd(to, to_point);
	    return;
	}
	
        System.err.println("Use glass robot");
        Point abs_from_point = from.getClickPoint(); //new Point(from_point);
        abs_from_point.translate((int)from.getScreenBounds().getX(), (int)from.getScreenBounds().getY());
        Point abs_to_point = new Point(to_point);
        abs_to_point.translate((int)to.getScreenBounds().getX(), (int)to.getScreenBounds().getY());
        if (robot == null) {
            robot = new GetAction<com.sun.glass.ui.Robot>() {
                        @Override
                        public void run(Object... os) throws Exception {
                            setResult(com.sun.glass.ui.Application.GetApplication().createRobot());
                        }
                    }.dispatch(Root.ROOT.getEnvironment()); // can not beDrag sourceDrag source done in static block due to initialization problems on Mac
        }
        
        robot.mouseMove(abs_from_point.x, abs_from_point.y);
        robot.mousePress(1);
        int differenceX = abs_to_point.x - abs_from_point.x;
        int differenceY = abs_to_point.y - abs_from_point.y;
	final int STEPS = differenceX > differenceY ? differenceX : differenceY;
        for (int i = 0; (i <= STEPS) && (!gotEvent()); i++) {
            robot.mouseMove(abs_from_point.x + differenceX * i / STEPS, abs_from_point.y + differenceY * i / STEPS);
        }
        
        robot.mouseRelease(1);
    }
    
    protected void dndd(final NodeDock _ndFrom, final NodeDock _ndTo) {
        final Wrap from = _ndFrom.wrap();
        final Wrap to = _ndTo.wrap();
      
        dndd(from, to);
    }
    
    protected void dndd(final Wrap from, final Wrap to) 
    {
        final Point to_point = to.getClickPoint();
        //final Point from_point = from.getClickPoint();
	if(!Utils.isWindows())
	{
	    from.drag().dnd(to, to_point);
	    return;
	}
        if (robot == null) {
        System.err.println("Use glass robot");
            robot = new GetAction<com.sun.glass.ui.Robot>() {
                        @Override
                        public void run(Object... os) throws Exception {
                            setResult(com.sun.glass.ui.Application.GetApplication().createRobot());
                        }
                    }.dispatch(Root.ROOT.getEnvironment()); // can not beDrag sourceDrag source done in static block due to initialization problems on Mac
        }
	
        Point abs_from_point = from.getClickPoint(); //new Point(from_point);
        abs_from_point.translate((int)from.getScreenBounds().getX(), (int)from.getScreenBounds().getY());
        Point abs_to_point = new Point(to_point);
        abs_to_point.translate((int)to.getScreenBounds().getX(), (int)to.getScreenBounds().getY());
        robot.mouseMove(abs_from_point.x, abs_from_point.y);
        robot.mousePress(1);
        try {Thread.sleep(500);}catch(Exception e){}
        
        int differenceX = abs_to_point.x - abs_from_point.x;
        int differenceY = abs_to_point.y - abs_from_point.y;
	final int STEPS = differenceX > differenceY ? differenceX : differenceY;
        for (int i = 0; (i <= STEPS) && (!gotEvent()); i++) {
            robot.mouseMove(abs_from_point.x + differenceX * i / STEPS, abs_from_point.y + differenceY * i / STEPS);
        }
        try {Thread.sleep(500);}catch(Exception e){}
        for (int i = STEPS; (i >=0) && (!gotEvent()); i--) {
            robot.mouseMove(abs_from_point.x + differenceX * i / STEPS, abs_from_point.y + differenceY * i / STEPS);
        }
        
        robot.mouseRelease(1);
        try {Thread.sleep(500);}catch(Exception e){}
    }
    

    static Robot robot = null;
    static 
    {
        if (Utils.isMacOS()) 
        {
            JemmyUtils.runInOtherJVM(true);
        }
    }
    
    private SceneDock sceneDock;
    private TabDock tabDock;
    private T primeDock; 
    private LabeledDock eventRadio;
    private Controls control;
    private EventTypes eventType;
    
}
