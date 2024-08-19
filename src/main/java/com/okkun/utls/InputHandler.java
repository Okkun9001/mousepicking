package com.okkun.utls;

import com.okkun.editor_window;

import javafx.event.EventHandler;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class InputHandler implements EventHandler<InputEvent>{

    private boolean W_Pressed, S_Pressed, A_Pressed, D_Pressed;
    private boolean UP_Pressed, DOWN_Pressed, LEFT_Pressed, RIGHT_Pressed;
    private boolean Q_Pressed, E_Pressed;
    private boolean T_Pressed, G_Pressed;
    private boolean SPACE_Pressed;
    private boolean mouseClickedLeft, mouseClickedRight;

    private Vector2D mousePosition;

    public InputHandler(editor_window window) {
        window.addEventHandler(InputEvent.ANY, this);
    }

    @Override
    public void handle(InputEvent event) {
        switch (event.getEventType().getName()) {
            case "KEY_PRESSED":
                keyPressed((KeyEvent)event);
                break;
            case "KEY_RELEASED":
                keyReleased((KeyEvent)event);
                break;
            case "MOUSE_CLICKED":
                mouseClicked((MouseEvent)event);
                break;
            case "MOUSE_MOVED":
                mouseMoved((MouseEvent)event);
                break;
            default:
                break;
        }
    }

    private void mouseMoved(MouseEvent event) {
        mousePosition = new Vector2D(event.getX(), event.getY());
    }

    private void mouseClicked(MouseEvent event) {
        switch (event.getButton()) {
            case PRIMARY:
                mousePosition = new Vector2D(event.getX(), event.getY());
                mouseClickedLeft = true;
                break;
            case SECONDARY:
                mousePosition = new Vector2D(event.getX(), event.getY());
                mouseClickedRight = true;    
                break;
        
            default:
                break;
        }
        
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case W:
                W_Pressed = true;
                break;
            case S:
                S_Pressed = true;
                break;
            case A:
                A_Pressed = true;
                break;
            case D:
                D_Pressed = true;
                break;
            case SPACE:
                SPACE_Pressed = true;
                break;
            case UP:
                UP_Pressed = true;
                break;
            case DOWN:
                DOWN_Pressed = true;
                break;
            case LEFT:
                System.out.println("left pressed");
                LEFT_Pressed = true;
                break;
            case RIGHT:
                System.out.println("right pressed");
                RIGHT_Pressed = true;
                break;
            case E:
                E_Pressed = true;
                break;
            case Q:
                Q_Pressed = true;
                break;
            case T:
                T_Pressed = true;
                break;
            case G:
                G_Pressed = true;
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        
        switch (e.getCode()) {
            case W:
                W_Pressed = false;
                break;
            case S:
                S_Pressed = false;
                break;
            case A:
                A_Pressed = false;
                break;
            case D:
                D_Pressed = false;
                break;
            case SPACE:
                SPACE_Pressed = false;
                break;
            case UP:
                UP_Pressed = false;
                break;
            case DOWN:
                DOWN_Pressed = false;
                break;
            case LEFT:
                System.out.println("left released");
                LEFT_Pressed = false;
                break;
            case RIGHT:
                System.out.println("right released");
                RIGHT_Pressed = false;
                break;
            case E:
                E_Pressed = false;
                break;
            case Q:
                Q_Pressed = false;
                break;
            case T:
                T_Pressed = false;
                break;
            case G:
                G_Pressed = false;
                break;
            default:
                break;
        }
        
    }
    
    public Vector2D getMousePosition() { return mousePosition; }
    public void setLMB_Clicked(boolean b) { mouseClickedLeft = b; }
    public void setRMB_Clicked(boolean b) { mouseClickedRight = b; }
    public void setSpacePressed(boolean b) { SPACE_Pressed = b; }

    public boolean SPACE_Pressed() { return SPACE_Pressed; }
    public boolean W_Pressed() { return W_Pressed; }
    public boolean S_Pressed() { return S_Pressed; }
    public boolean A_Pressed() { return A_Pressed; }
    public boolean D_Pressed() { return D_Pressed; }
    public boolean UP_Pressed() { return UP_Pressed; }
    public boolean DOWN_Pressed() { return DOWN_Pressed; }
    public boolean LEFT_Pressed() { return LEFT_Pressed; }
    public boolean RIGHT_Pressed() { return RIGHT_Pressed; }
    public boolean E_Pressed() { return E_Pressed; }
    public boolean Q_Pressed() { return Q_Pressed; }
    public boolean T_Pressed() { return T_Pressed; }
    public boolean G_Pressed() { return G_Pressed; }
    public boolean LMB_Clicked() { return mouseClickedLeft; }
    public boolean RMB_Clicked() { return mouseClickedRight; }


}
