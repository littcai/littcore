package com.litt.core.io;

import java.util.HashMap;

/**
 * 
 * <b>标题:</b>  MIME类型定义类.
 * <pre><b>描述:</b> 
 *	根据后缀名获得对象MIME类型
 * </pre>
 * 
 * @author <a href="mailto:littcai@hotmail.com">空心大白菜</a>
 * @since 2008-07-25
 * @version 1.0
 *
 */
public class MimeType 
{
	/** 隐藏构造函数，避免生成实例 */
	private MimeType(){};	

	/**
	 * 根据后缀名获得对象MIME类型
	 * @param extName 后缀名
	 * @return MIME类型
	 */
	public static String getContentType(String extName)
	{
		Object ret= _MIME_TYPE_MAP.get(extName.toLowerCase());
		if(ret == null)
			ret = "application/octet-stream";
		return ret.toString();
	}
	
	private static final HashMap _MIME_TYPE_MAP = new HashMap();
	
	static {
		_MIME_TYPE_MAP.put("ez", "application/andrew-inset");
		_MIME_TYPE_MAP.put("hqx", "application/mac-binhex40");
		_MIME_TYPE_MAP.put("cpt", "application/mac-compactpro");
		_MIME_TYPE_MAP.put("doc", "application/msword");
		_MIME_TYPE_MAP.put("bin", "application/octet-stream");
		_MIME_TYPE_MAP.put("dms", "application/octet-stream");
		_MIME_TYPE_MAP.put("lha", "application/octet-stream");
		_MIME_TYPE_MAP.put("lzh", "application/octet-stream");
		_MIME_TYPE_MAP.put("exe", "application/octet-stream");
		_MIME_TYPE_MAP.put("class", "application/octet-stream");
		_MIME_TYPE_MAP.put("so", "application/octet-stream");
		_MIME_TYPE_MAP.put("dll", "application/octet-stream");
		_MIME_TYPE_MAP.put("oda", "application/oda");
		_MIME_TYPE_MAP.put("pdf", "application/pdf");
		_MIME_TYPE_MAP.put("ai", "application/postscript");
		_MIME_TYPE_MAP.put("eps", "application/postscript");
		_MIME_TYPE_MAP.put("ps", "application/postscript");
		_MIME_TYPE_MAP.put("smi", "application/smil");
		_MIME_TYPE_MAP.put("smil", "application/smil");
		_MIME_TYPE_MAP.put("mif", "application/vnd.mif");
		_MIME_TYPE_MAP.put("xls", "application/vnd.ms-excel");
		_MIME_TYPE_MAP.put("ppt", "application/vnd.ms-powerpoint");
		_MIME_TYPE_MAP.put("wbxml", "application/vnd.wap.wbxml");
		_MIME_TYPE_MAP.put("wmlc", "application/vnd.wap.wmlc");
		_MIME_TYPE_MAP.put("wmlsc", "application/vnd.wap.wmlscriptc");
		_MIME_TYPE_MAP.put("bcpio", "application/x-bcpio");
		_MIME_TYPE_MAP.put("vcd", "application/x-cdlink");
		_MIME_TYPE_MAP.put("pgn", "application/x-chess-pgn");
		_MIME_TYPE_MAP.put("cpio", "application/x-cpio");
		_MIME_TYPE_MAP.put("csh", "application/x-csh");
		_MIME_TYPE_MAP.put("dcr", "application/x-director");
		_MIME_TYPE_MAP.put("dir", "application/x-director");
		_MIME_TYPE_MAP.put("dxr", "application/x-director");
		_MIME_TYPE_MAP.put("dvi", "application/x-dvi");
		_MIME_TYPE_MAP.put("spl", "application/x-futuresplash");
		_MIME_TYPE_MAP.put("gtar", "application/x-gtar");
		_MIME_TYPE_MAP.put("hdf", "application/x-hdf");
		_MIME_TYPE_MAP.put("js", "application/x-javascript");
		_MIME_TYPE_MAP.put("skp", "application/x-kn");
		_MIME_TYPE_MAP.put("skd", "application/x-kn");
		_MIME_TYPE_MAP.put("skt", "application/x-kn");
		_MIME_TYPE_MAP.put("skm", "application/x-kn");
		_MIME_TYPE_MAP.put("latex", "application/x-latex");
		_MIME_TYPE_MAP.put("nc", "application/x-netcdf");
		_MIME_TYPE_MAP.put("cdf", "application/x-netcdf");
		_MIME_TYPE_MAP.put("sh", "application/x-sh");
		_MIME_TYPE_MAP.put("shar", "application/x-shar");
		_MIME_TYPE_MAP.put("swf", "application/x-shockwave-flash");
		_MIME_TYPE_MAP.put("sit", "application/x-stuffit");
		_MIME_TYPE_MAP.put("sv4cpio", "application/x-sv4cpio");
		_MIME_TYPE_MAP.put("sv4crc", "application/x-sv4crc");
		_MIME_TYPE_MAP.put("tar", "application/x-tar");
		_MIME_TYPE_MAP.put("tcl", "application/x-tcl");
		_MIME_TYPE_MAP.put("tex", "application/x-tex");
		_MIME_TYPE_MAP.put("texinfo", "application/x-texinfo");
		_MIME_TYPE_MAP.put("texi", "application/x-texinfo");
		_MIME_TYPE_MAP.put("t", "application/x-troff");
		_MIME_TYPE_MAP.put("tr", "application/x-troff");
		_MIME_TYPE_MAP.put("roff", "application/x-troff");
		_MIME_TYPE_MAP.put("man", "application/x-troff-man");
		_MIME_TYPE_MAP.put("me", "application/x-troff-me");
		_MIME_TYPE_MAP.put("ms", "application/x-troff-ms");
		_MIME_TYPE_MAP.put("ustar", "application/x-ustar");
		_MIME_TYPE_MAP.put("src", "application/x-wais-source");
		_MIME_TYPE_MAP.put("xhtml", "application/xhtml+xml");
		_MIME_TYPE_MAP.put("xht", "application/xhtml+xml");
		_MIME_TYPE_MAP.put("zip", "application/zip");
		_MIME_TYPE_MAP.put("au", "audio/basic");
		_MIME_TYPE_MAP.put("snd", "audio/basic");
		_MIME_TYPE_MAP.put("mid", "audio/midi");
		_MIME_TYPE_MAP.put("midi", "audio/midi");
		_MIME_TYPE_MAP.put("kar", "audio/midi");
		_MIME_TYPE_MAP.put("mpga", "audio/mpeg");
		_MIME_TYPE_MAP.put("mp2", "audio/mpeg");
		_MIME_TYPE_MAP.put("mp3", "audio/mpeg");
		_MIME_TYPE_MAP.put("aif", "audio/x-aiff");
		_MIME_TYPE_MAP.put("aiff", "audio/x-aiff");
		_MIME_TYPE_MAP.put("aifc", "audio/x-aiff");
		_MIME_TYPE_MAP.put("m3u", "audio/x-mpegurl");
		_MIME_TYPE_MAP.put("ram", "audio/x-pn-realaudio");
		_MIME_TYPE_MAP.put("rm", "audio/x-pn-realaudio");
		_MIME_TYPE_MAP.put("rpm", "audio/x-pn-realaudio-plugin");
		_MIME_TYPE_MAP.put("ra", "audio/x-realaudio");
		_MIME_TYPE_MAP.put("wav", "audio/x-wav");
		_MIME_TYPE_MAP.put("pdb", "chemical/x-pdb");
		_MIME_TYPE_MAP.put("xyz", "chemical/x-xyz");
		_MIME_TYPE_MAP.put("bmp", "image/bmp");
		_MIME_TYPE_MAP.put("gif", "image/gif");
		_MIME_TYPE_MAP.put("ief", "image/ief");
		_MIME_TYPE_MAP.put("jpeg", "image/jpeg");
		_MIME_TYPE_MAP.put("jpg", "image/jpeg");
		_MIME_TYPE_MAP.put("jpe", "image/jpeg");
		_MIME_TYPE_MAP.put("png", "image/png");
		_MIME_TYPE_MAP.put("tiff", "image/tiff");
		_MIME_TYPE_MAP.put("tif", "image/tiff");
		_MIME_TYPE_MAP.put("djvu", "image/vnd.djvu");
		_MIME_TYPE_MAP.put("djv", "image/vnd.djvu");
		_MIME_TYPE_MAP.put("wbmp", "image/vnd.wap.wbmp");
		_MIME_TYPE_MAP.put("ras", "image/x-cmu-raster");
		_MIME_TYPE_MAP.put("pnm", "image/x-portable-anymap");
		_MIME_TYPE_MAP.put("pbm", "image/x-portable-bitmap");
		_MIME_TYPE_MAP.put("pgm", "image/x-portable-graymap");
		_MIME_TYPE_MAP.put("ppm", "image/x-portable-pixmap");
		_MIME_TYPE_MAP.put("rgb", "image/x-rgb");
		_MIME_TYPE_MAP.put("xbm", "image/x-xbitmap");
		_MIME_TYPE_MAP.put("xpm", "image/x-xpixmap");
		_MIME_TYPE_MAP.put("xwd", "image/x-xwindowdump");
		_MIME_TYPE_MAP.put("igs", "model/iges");
		_MIME_TYPE_MAP.put("iges", "model/iges");
		_MIME_TYPE_MAP.put("msh", "model/mesh");
		_MIME_TYPE_MAP.put("mesh", "model/mesh");
		_MIME_TYPE_MAP.put("silo", "model/mesh");
		_MIME_TYPE_MAP.put("wrl", "model/vrml");
		_MIME_TYPE_MAP.put("vrml", "model/vrml");
		_MIME_TYPE_MAP.put("css", "text/css");
		_MIME_TYPE_MAP.put("html", "text/html");
		_MIME_TYPE_MAP.put("htm", "text/html");
		_MIME_TYPE_MAP.put("asc", "text/plain");
		_MIME_TYPE_MAP.put("txt", "text/plain");
		_MIME_TYPE_MAP.put("rtx", "text/richtext");
		_MIME_TYPE_MAP.put("rtf", "text/rtf");
		_MIME_TYPE_MAP.put("sgml", "text/sgml");
		_MIME_TYPE_MAP.put("sgm", "text/sgml");
		_MIME_TYPE_MAP.put("tsv", "text/tab-separated-values");
		_MIME_TYPE_MAP.put("wml", "text/vnd.wap.wml");
		_MIME_TYPE_MAP.put("wmls", "text/vnd.wap.wmlscript");
		_MIME_TYPE_MAP.put("etx", "text/x-setext");
		_MIME_TYPE_MAP.put("xsl", "text/xml");
		_MIME_TYPE_MAP.put("xml", "text/xml");
		_MIME_TYPE_MAP.put("mpeg", "video/mpeg");
		_MIME_TYPE_MAP.put("mpg", "video/mpeg");
		_MIME_TYPE_MAP.put("mpe", "video/mpeg");
		_MIME_TYPE_MAP.put("qt", "video/quicktime");
		_MIME_TYPE_MAP.put("mov", "video/quicktime");
		_MIME_TYPE_MAP.put("mxu", "video/vnd.mpegurl");
		_MIME_TYPE_MAP.put("avi", "video/x-msvideo");
		_MIME_TYPE_MAP.put("movie", "video/x-sgi-movie");
		_MIME_TYPE_MAP.put("ice", "x-conference/x-cooltalk");
	}
	
	
}
