package utils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RoundedButton extends JButton {

    private Color normalBackground;
    private Color hoverBackground;
    private Color pressedBackground;

    public RoundedButton(String text) {
        super(text);

        // Define button colors
        normalBackground = new Color(0x003366); // Default button color
        hoverBackground = new Color(0x00509d); // Slightly brighter for hover
        pressedBackground = new Color(0x001d3d); // Slightly darker for pressed

        // Set button properties
        setFocusPainted(false); // Removes focus border
        setContentAreaFilled(false); // Transparent background
        setOpaque(false); // Ensures custom painting
        setFont(new Font("Serif", Font.BOLD, 18));
        setForeground(Color.WHITE); // Text color
        setBackground(normalBackground); // Initial background color
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Setting a consistent preferred size
        setPreferredSize(new Dimension(300, 70)); // Button dimensions
        Border roundedBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15); // Padding for text
        setBorder(roundedBorder);

        // Add mouse listeners for hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverBackground); // Change color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalBackground); // Reset to default color
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedBackground); // Change color on click
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverBackground); // Return to hover color when released
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw the button's rounded background
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getBackground());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded corners (20px radius)
        g2d.dispose();

        // Call the superclass to render text
        super.paintComponent(g);
    }
}
