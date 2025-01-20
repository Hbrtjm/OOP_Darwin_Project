package BaseClasses;

import java.util.Objects;
public class Vector2d {
    private final int x;
    private final int y;
    public int getX()
    {
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public  Vector2d(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString()
    {
        return ("(" + String.valueOf(this.x) + "," + String.valueOf(this.y) + ")");
    }
    public boolean precedes(Vector2d other)
    {
        return this.x < other.getX() && this.y < other.getY();
    }
    public boolean follows(Vector2d other)
    {
        return this.x >= other.getX() && this.y >= other.getY();
    }
    public Vector2d add(Vector2d other)
    {
        return new Vector2d(this.x+other.getX(),this.y+other.getY());
    }
    public Vector2d subtract(Vector2d other)
    {
        return new Vector2d(this.x-other.getX(),this.y-other.getY());
    }
    public Vector2d upperRight(Vector2d other)
    {
        return new Vector2d(Math.max(this.x,other.getX()),Math.max(this.y,other.getY()));
    }
    public Vector2d lowerLeft(Vector2d other)
    {
        return new Vector2d(Math.min(this.x,other.getX()),Math.min(this.y,other.getY()));
    }
    public Vector2d opposite()
    {
        return new Vector2d(-this.x,-this.y);
    }
    // "[...] zamiana metody equals"
    @Override
    public boolean equals(Object other)
    {
        if(other == this)
        {
            return true;
        }
        if(!(other instanceof Vector2d vector2dOther))
        {
            return false;
        }
        return this.x == (vector2dOther).getX() && this.y == (vector2dOther).getY();
    }
    // "[...] powinna powodować zmianę metody hashCode"
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}

