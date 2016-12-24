package Common;

import java.io.Serializable;

public class Image implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 646386033302817696L;
	private String _imageName;
	private String[] _themes;
	private String _uploader;
	private String _note;
	private int _width;
	private int _height;
	private int _size;
	private byte[] _imageData;
	
	public Image()
	{
	}
	
	public Image(String imgName, String[] themes, String uploader, String note, int width, int height, byte[] imageData)
	{
		_imageName = imgName;
		_themes = themes;
		_uploader = uploader;
		_note = note;
		_width = width;
		_size = imageData.length;
		_height = height;
		_imageData = imageData.clone();
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(_imageName + "\n");
		sb.append((_themes == null ? 0 : _themes.length) + "\n");
		for (int i = 0; i < _themes.length; ++i)
			sb.append(_themes[i] + "\n");
		sb.append(_uploader + "\n");
		sb.append(_note + "\n");
		sb.append(_width + "\n");
		sb.append(_height + "\n");
		sb.append(_size + "\n");
		
		return sb.toString();
	}

	public String get_imageName() {
		return _imageName;
	}

	public byte[] get_imageData() {
		return _imageData;
	}

	public String get_uploader() {
		return _uploader;
	}
	
	public String[] get_themes() {
		return _themes;
	}
	
}