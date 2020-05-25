/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BasicPaint;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author ZK
 */
public class DrawingObject {
    public Color color_;
    public int size_;
    public List<Point> vertices_;
    public String type_;
    
    public DrawingObject(Color color, int size, List<Point> vertices, String type){
        color_ = color;
        size_ = size;
        vertices_ = vertices;
        type_ = type;
    }
}
