package BaseClasses;

/**
 * Represents a rectangular boundary defined by lower and upper corner vectors.
 * Provides utility methods for boundary checking and validation.
 */
public record Boundary(Vector2d lower, Vector2d upper) {
    
    /**
     * Constructor with validation.
     * @param lower The lower-left corner of the boundary
     * @param upper The upper-right corner of the boundary
     * @throws IllegalArgumentException if lower or upper is null, or if lower is not actually lower than upper
     */
    public Boundary {
        if (lower == null || upper == null) {
            throw new IllegalArgumentException("Boundary corners cannot be null");
        }
        if (lower.getX() > upper.getX() || lower.getY() > upper.getY()) {
            throw new IllegalArgumentException("Lower bound must be less than or equal to upper bound");
        }
    }
    
    /**
     * Checks if a given position is inside this boundary (inclusive).
     * @param position The position to check
     * @return true if the position is within the boundary, false otherwise
     */
    public boolean isInside(Vector2d position) {
        if (position == null) {
            return false;
        }
        return position.getX() >= lower.getX() && position.getX() <= upper.getX() &&
               position.getY() >= lower.getY() && position.getY() <= upper.getY();
    }
    
    /**
     * Gets the width of the boundary.
     * @return The width (difference between upper and lower x coordinates)
     */
    public int getWidth() {
        return upper.getX() - lower.getX();
    }
    
    /**
     * Gets the height of the boundary.
     * @return The height (difference between upper and lower y coordinates)
     */
    public int getHeight() {
        return upper.getY() - lower.getY();
    }
    
    /**
     * Gets the area of the boundary.
     * @return The area (width * height)
     */
    public int getArea() {
        return getWidth() * getHeight();
    }
    
    /**
     * Checks if this boundary contains another boundary entirely.
     * @param other The other boundary to check
     * @return true if this boundary contains the other boundary
     */
    public boolean contains(Boundary other) {
        if (other == null) {
            return false;
        }
        return isInside(other.lower) && isInside(other.upper);
    }
    
    @Override
    public String toString() {
        return String.format("Boundary[%s -> %s]", lower, upper);
    }
}
