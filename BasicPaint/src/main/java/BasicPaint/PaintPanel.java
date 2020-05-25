/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BasicPaint;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author ZK
 */
public class PaintPanel extends JPanel implements MouseMotionListener, MouseListener{
    private ButtonGroup buttonGroup;
    private JPanel buttonPanel;
    private boolean isDrawing = false;
    private List<DrawingObject> drawing_objects;
    private String tool_mode = "none";
    private int tool_size;
    private Color color_ = Color.BLACK;
    private BufferedImage buffer, lastBuffer;
    private Graphics2D canvas;
    private boolean isDragged = false;
    private int count = 0;
    
    public PaintPanel(){
        // panel backgroud is 214,217,223
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setSize(780, 480);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand() == "Clear") 
                {
                    System.out.println("clear pressed");
                    drawing_objects.clear();
                    buffer = new BufferedImage(780, 480, BufferedImage.TYPE_INT_ARGB);
                    lastBuffer.setData(buffer.getData());
                    System.out.println(drawing_objects.size());
                    repaint();
                }
                else if(e.getActionCommand() == "Color Picker")
                {
                    color_ = JColorChooser.showDialog(buttonPanel, "Color Picker", color_);
                }
                else tool_mode = e.getActionCommand();
            }
        };
        buttonGroup = new ButtonGroup();
        JToggleButton penButton = new JToggleButton("Pen");
        penButton.addActionListener(listener);

        JToggleButton eraserButton = new JToggleButton("Eraser");
        eraserButton.addActionListener(listener);

        JToggleButton fillButton = new JToggleButton("Fill");
        fillButton.addActionListener(listener);

        JToggleButton circleButton = new JToggleButton("Circle");
        circleButton.addActionListener(listener);

        JToggleButton recButton = new JToggleButton("Rectangle");
        recButton.addActionListener(listener);
        
        JButton cPickerButton = new JButton("Color Picker");
        cPickerButton.addActionListener(listener);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(listener);

        buttonGroup.add(penButton);
        buttonPanel.add(penButton);
        buttonGroup.add(eraserButton);
        buttonPanel.add(eraserButton);        
        buttonGroup.add(fillButton);
        buttonPanel.add(fillButton);
        buttonGroup.add(circleButton);
        buttonPanel.add(circleButton);
        buttonGroup.add(recButton);
        buttonPanel.add(recButton);
        buttonPanel.add(cPickerButton);
        buttonPanel.add(clearButton);
        
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.LINE_AXIS));
        sliderPanel.add(Box.createHorizontalStrut(300));
        JLabel label = new JLabel("Tool size: ");
        sliderPanel.add(label);

        JSlider size = new JSlider(JSlider.HORIZONTAL, 5, 30, 10);
        JLabel label1 = new JLabel(" " + Integer.toString(size.getValue()));
        
        size.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                tool_size = ((JSlider)e.getSource()).getValue();
                label1.setText(Integer.toString(tool_size));
            }
            
        });
        sliderPanel.add(size);
        sliderPanel.add(label1);
        sliderPanel.add(Box.createHorizontalStrut(300));
        
        this.add(buttonPanel);
        this.add(sliderPanel);
        this.add(Box.createRigidArea(new Dimension(0, 1000)));
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
        
        tool_size = size.getValue();
        
        drawing_objects = new ArrayList<DrawingObject>();
        
        buffer = new BufferedImage(780, 480, BufferedImage.TYPE_INT_ARGB);
        lastBuffer = new BufferedImage(780, 480, BufferedImage.TYPE_INT_ARGB);
        canvas = buffer.createGraphics();
    }

    //
    // DRAWING METHODS
    //s
    void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for(DrawingObject draw_object : drawing_objects)
        {
            Stroke stroke = new BasicStroke(draw_object.size_, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            g2d.setStroke(stroke);
            g2d.setColor(draw_object.color_);
            List<Point> cur_lp = draw_object.vertices_;
            if(draw_object.type_ == "Circle" || draw_object.type_ == "Rectangle")
            {
                int width = Math.abs(cur_lp.get(1).x - cur_lp.get(0).x);
                int height = Math.abs(cur_lp.get(1).y - cur_lp.get(0).y);

                if(draw_object.type_ == "Circle")
                    g2d.drawOval(cur_lp.get(0).x, cur_lp.get(0).y, width, height);
                else
                    g2d.drawRect(cur_lp.get(0).x, cur_lp.get(0).y, width, height);
            }
            if(draw_object.type_ == "Pen" || draw_object.type_ == "Eraser")
            {
                for(int i = 0; i < cur_lp.size() - 1; i++)
                {
                    Point p1 = cur_lp.get(i);
                    Point p2 = cur_lp.get(i+1);

                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }        
            }
            
        }
    }
    
    private void refreshCanvas(){
        canvas.dispose();
        buffer.getGraphics().dispose();
        buffer.flush();
        buffer = new BufferedImage(780, 480, BufferedImage.TYPE_INT_ARGB);
        buffer.setData(lastBuffer.getData());
        buffer.createGraphics();
        canvas = (Graphics2D)buffer.getGraphics();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g); //To change body of generated methods, choose Tools | Templates.

        drawLines(canvas);
        ((Graphics2D) g).drawImage(buffer, 0, 0, this);
    }
    
    private void floodFill(int x, int y, Color before, Color target){
        if(before.equals(target)) return;

        int col = this.buffer.getRGB(x, y);
        if (x < 0 || x >= this.buffer.getWidth() || y < 0 || y >= this.buffer.getHeight() 
            || col != before.getRGB() || col == target.getRGB())
        {
            return;
        }
//        System.out.println(this.buffer.getRGB(x, y));
        this.buffer.setRGB(x, y, target.getRGB());
//        System.out.println(this.buffer.getRGB(x, y));

        floodFill(x+1, y, before, target);
        floodFill(x-1, y, before, target);
        floodFill(x, y+1, before, target);
        floodFill(x, y-1, before, target);
    }
    
    //
    // MOUSE METHODS
    //
    @Override
    public void mouseDragged(MouseEvent e) {
        isDragged = true;
        if(tool_mode != "none" && tool_mode != "Clear")
        {

           if(tool_mode == "Circle" || tool_mode == "Rectangle")
           {
                refreshCanvas();
                drawing_objects.get(drawing_objects.size() - 1).vertices_.remove(1);
           }
            drawing_objects.get(drawing_objects.size() - 1).vertices_.add(new Point(e.getX(), e.getY()));
        }
        repaint();   
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(tool_mode == "Fill")
        {
            floodFill(e.getX(), e.getY(), new Color(buffer.getRGB(e.getX(), e.getY()), true), color_);
            lastBuffer.setData(buffer.getData());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
//        System.out.println(new Point(10, 10));
        System.out.println();

        if(tool_mode != "none" && tool_mode != "Clear")
        {
            List<Point> points = new ArrayList<Point>();
            points.add(new Point(e.getX(), e.getY()));
            if(tool_mode == "Circle" || tool_mode == "Rectangle")points.add(new Point(e.getX(), e.getY()));
            Color c = (tool_mode == "Eraser")? new Color(214, 217, 223) : color_;
            drawing_objects.add(new DrawingObject(c, tool_size, points, tool_mode));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragged = false;
        if(tool_mode == "Circle" || tool_mode == "Rectangle")
        {
            drawing_objects.get(drawing_objects.size() - 1).vertices_.add(new Point(e.getX(), e.getY()));
            
        }
        repaint();
        lastBuffer.setData(buffer.getData());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    
}
