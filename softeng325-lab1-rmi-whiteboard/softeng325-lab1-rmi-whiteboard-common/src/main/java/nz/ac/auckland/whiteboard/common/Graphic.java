package nz.ac.auckland.whiteboard.common;

import java.io.Serializable;

/**
 * Class to represent the state of a Shape object. Graphic instances are 
 * intended to be passed between clients and servers and so Graphic necessarily
 * implements the Serializable interface.
 * 
 */
public class Graphic implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int _x;
	private int _y;
	private int _width;
	private int _height;
	private boolean _isFilled;

	/**
	 * Creates a Graphic object with given values.
	 * @param x the x coordinate of the Graphic.
	 * @param y the y coordinate of the Graphic.
	 * @param width the Graphic's width.
	 * @param height the Graphic's height.
	 * @param isFilled true if the Graphic should be rendered as a solid shape,
	 * false for an outline.
	 */
	public Graphic(int x, int y, int width, int height, boolean isFilled) {
		this._x = x;
		this._y = y;
		this._width = width;
		this._height = height;
		this._isFilled = isFilled;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public int getWidth() {
		return _width;
	}
	
	public int getHeight() {
		return _height;
	}
	
	public boolean isFilled() {
		return _isFilled;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClass().getName());
		buffer.append(":x=");
		buffer.append(_x);
		buffer.append(",y=");
		buffer.append(_y);
		buffer.append(",width=");
		buffer.append(_width);
		buffer.append(",height=");
		buffer.append(_height);
		buffer.append(",isFilled=");
		buffer.append(_isFilled);
		buffer.append("]");
		return buffer.toString();
	}
}
